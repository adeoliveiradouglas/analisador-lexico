import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	static List<Character> simbolosEspeciais = new ArrayList<Character>();
	static List<String> palavrasReservadas = new ArrayList<>();

	@SuppressWarnings("resource")
	public static void main(String[] args) throws FileNotFoundException {
		List<Token> tokens = new ArrayList<>();

		carregarTabelaSimbolosEspeciais();
		carregarTabelaPalavrasReservadas();

		String caminhoArquivo = "sourcecode.txt";

		Scanner arquivo = new Scanner(new FileReader(caminhoArquivo));
		String palavra;
		int posicao = 0; // posicao do token

		while (arquivo.hasNext()) {
//			le ate o proximo espaco
			palavra = arquivo.next();

			char c;
			String processando = "";

 			for (int i = 0; i < palavra.length(); ++i) {
//				le cada char da palavra 
				c = palavra.charAt(i);

				if (isSimboloEspecial(c)) {
//					se o char lido for um caractere especial

					if (processando.length() > 0) {
//						salva o que está armazenado em processando caso haja algo
						if (isPalavraReservada(processando))
							tokens.add(new Token(processando, "Palavra reservada", posicao++));
						else
							tokens.add(new Token(processando, "Identificador", posicao++));
						
						processando = "";
					}

					tokens.add(new Token("" + c, "Simbolo especial", posicao++));
				} else if (isPalavraReservada(processando)) {
					tokens.add(new Token(processando, "Palavra reservada", posicao++));
					processando = "";
				} else {
//					concatena o char com os ja lidos para formar um identificador
					processando = processando + c;
				}
			}

			if (processando.length() > 0) {
				if (isPalavraReservada(processando))
					tokens.add(new Token(processando, "Palavra reservada", posicao++));
				else
//				salva o que está armazenado em processando caso haja algo
					tokens.add(new Token(processando, "Identificador", posicao++));
			}
		}
		
		for(Token t: tokens)
			System.out.println(t);
	}

	public static boolean isPalavraReservada(String c) {
		if (palavrasReservadas.indexOf(c) >= 0)
			return true;

		return false;
	}

	public static boolean isSimboloEspecial(char c) {
		if (simbolosEspeciais.indexOf(c) >= 0)
			return true;

		return false;
	}

	@SuppressWarnings("resource")
	public static void carregarTabelaSimbolosEspeciais() throws FileNotFoundException {
		String caminhoArquivo = "simbolosespeciais.txt";

		Scanner arquivo = new Scanner(new FileReader(caminhoArquivo));

		char lido;
		do {
			lido = arquivo.next().charAt(0);

//			if(null != lido)
			simbolosEspeciais.add(lido);

		} while (arquivo.hasNext());
	}

	@SuppressWarnings("resource")
	public static void carregarTabelaPalavrasReservadas() throws FileNotFoundException {
		String caminhoArquivo = "palavrasreservadas.txt";

		Scanner arquivo = new Scanner(new FileReader(caminhoArquivo));

		do {
			palavrasReservadas.add(arquivo.next());
		} while (arquivo.hasNext());
	}
}