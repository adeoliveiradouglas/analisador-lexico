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
	
	public List<Token> analisar() throws FileNotFoundException{
		Scanner arquivo = new Scanner(new FileReader(caminhoArquivo));
		String palavra;
		int posicao = 0; // posicao do token
		boolean cadeiaCaracteres = false;
		String processando = "";
		
		while (arquivo.hasNext()) {
//			le ate o proximo espaco
			palavra = arquivo.next();

			char c;
			
 			for (int i = 0; i < palavra.length(); ++i) {
//				le cada char da palavra 
				c = palavra.charAt(i);

				if(c == '\'') {
					if(!cadeiaCaracteres) {
						tokens.add(new Token("\'", "Simbolo especial", posicao++));
						cadeiaCaracteres = true;
					} else {
						cadeiaCaracteres = false;
						tokens.add(new Token(processando, "Cadeia de caracteres", posicao++));
						tokens.add(new Token("\'", "Simbolo especial", posicao++));
						processando = "";
					}
					
				}
				else if (isSimboloEspecial(""+c) && !cadeiaCaracteres) {
//					se o char lido for um caractere especial
					String aux,
					       simbol = c+"";
					try {
						aux = ""+palavra.charAt(i+1);
						
						if(isSimboloEspecial(c+aux)) {
							simbol = c+aux; 
						}
							
					} catch (StringIndexOutOfBoundsException e) {}
					
					if (processando.length() > 0) {
//						salva o que está armazenado em processando caso haja algo
						if (isPalavraReservada(processando))
							tokens.add(new Token(processando, "Palavra reservada", posicao++));
						else
							tokens.add(new Token(processando, "Identificador", posicao++));
						
						processando = "";
					}

					tokens.add(new Token(simbol, "Simbolo especial", posicao++));
				} else {
//					concatena o char com os ja lidos para formar um identificador
					processando = processando + c;
				}
			} 			

			if (processando.length() > 0 && !cadeiaCaracteres) {
				if (isPalavraReservada(processando))
					tokens.add(new Token(processando, "Palavra reservada", posicao++));
				else
//				salva o que está armazenado em processando caso haja algo
					tokens.add(new Token(processando, "Identificador", posicao++));
				
				processando = "";
			}
		}
		
		arquivo.close();
		return tokens;
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
