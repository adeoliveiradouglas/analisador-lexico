import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Analisador {
	List<String> simbolosEspeciais;
	List<String> palavrasReservadas;
	String caminhoArquivo = "sourcecode.txt";
	List<Token> tokens = new ArrayList<>();

	public List<Token> analisar() throws FileNotFoundException {
		Scanner arquivo = new Scanner(new FileReader(caminhoArquivo));
		String palavra;
		int posicao = 0; // posicao do token
		boolean cadeiaCaracteres = false/*
										 * , inteiro = false, real = false, flutuante = false
										 */;
		String processando = "";

		while (arquivo.hasNext()) {
//			le ate o proximo espaco
			palavra = arquivo.next();

			char c = 0;

			for (int i = 0; i < palavra.length(); ++i) {
//				le cada char da palavra 
				c = palavra.charAt(i);

				if (!cadeiaCaracteres) {
					if (c == '\'') {
						tokens.add(new Token("\'", "Simbolo especial", posicao++));
						cadeiaCaracteres = true;
					} else if (isSimboloEspecial(String.valueOf(c))) {
//						se o char lido for um caractere especial
						String aux, simbol = String.valueOf(c);
						try {
							aux = "" + palavra.charAt(i + 1);

							if (isSimboloEspecial(c + aux)) {
								simbol = c + aux;
								++i;
							}

						} catch (StringIndexOutOfBoundsException e) {}

						if (processando.length() > 0) {
							if (isPalavraReservada(processando)) 
								tokens.add(new Token(processando, "Palavra reservada", posicao++));
							else
								tokens.add(new Token(processando, "Identificador", posicao++));
							
							processando = "";
						}
						
						tokens.add(new Token(simbol, "Simbolo especial", posicao++));
					} /*
						 * else if (isDigito(c)) {
						 * 
						 * }
						 */
					else {
//						concatena o char com os ja lidos para formar um identificador
						processando = processando + c;
					}
				} else {
					if (c != '\'') {
//						concatena o char com os ja lidos para formar um identificador
						processando = processando + c;
					} else {
						cadeiaCaracteres = false;
						tokens.add(new Token(processando, "Cadeia de caracteres", posicao++));
						tokens.add(new Token("\'", "Simbolo especial", posicao++));
						processando = "";
						/*} else if (c == ' ') {
						processando = processando + ' ';*/
					}
				}

			}
			
			if(cadeiaCaracteres) {
				processando = processando + " ";
			}else
				if (processando.length() > 0) {
//					salva o que est� armazenado em processando caso haja algo
					if (isPalavraReservada(processando))
						tokens.add(new Token(processando, "Palavra reservada", posicao++));
					else
						tokens.add(new Token(processando, "Identificador", posicao++));
	
					processando = "";
				}

		}

		if (processando.length() > 0 && !cadeiaCaracteres) {
			if (isPalavraReservada(processando))
				tokens.add(new Token(processando, "Palavra reservada", posicao++));
			else
//				salva o que est� armazenado em processando caso haja algo
				tokens.add(new Token(processando, "Identificador", posicao++));

			processando = "";
		}

		arquivo.close();
		return tokens;

	}

	private boolean isDigito(char c) {
		try {
			Integer.parseInt(String.valueOf(c));
			
			return true;
		} catch(Exception e) {}
		
		return false;
	}

	public boolean isPalavraReservada(String c) {
		c = c.toLowerCase();

		if (palavrasReservadas.indexOf(c) >= 0)
			return true;

		return false;
	}

	public boolean isSimboloEspecial(String c) {
		if (simbolosEspeciais.indexOf(c) >= 0)
			return true;

		return false;
	}

	public List<String> carregarTabelaSimbolosEspeciais() throws FileNotFoundException {
		List<String> simbolosEspeciais = new ArrayList<>();
		String caminhoArquivo = "simbolosespeciais.txt";

		Scanner arquivo = new Scanner(new FileReader(caminhoArquivo));

		String lido;
		do {
			lido = arquivo.next();

			simbolosEspeciais.add(lido);

		} while (arquivo.hasNext());

		arquivo.close();
		return simbolosEspeciais;
	}

	public static List<String> carregarTabelaPalavrasReservadas() throws FileNotFoundException {
		List<String> palavrasReservadas = new ArrayList<>();
		String caminhoArquivo = "palavrasreservadas.txt";

		Scanner arquivo = new Scanner(new FileReader(caminhoArquivo));

		do {
			palavrasReservadas.add(arquivo.next());
		} while (arquivo.hasNext());

		arquivo.close();
		return palavrasReservadas;
	}

	public Analisador(String caminhoArquivo) throws FileNotFoundException {
		this.simbolosEspeciais = carregarTabelaSimbolosEspeciais();
		this.palavrasReservadas = carregarTabelaPalavrasReservadas();
		this.caminhoArquivo = caminhoArquivo;
	}

	public Analisador() throws FileNotFoundException {
		this.simbolosEspeciais = carregarTabelaSimbolosEspeciais();
		this.palavrasReservadas = carregarTabelaPalavrasReservadas();
	}
}
