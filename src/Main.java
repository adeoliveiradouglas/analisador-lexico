import java.io.FileNotFoundException;
import java.util.List;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		Analisador lexico = new Analisador("area.txt");
		
		List<Token> tokens = lexico.analisar();
		
		for(Token t: tokens)
			System.out.println(t);
	}


}