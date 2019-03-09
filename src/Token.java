import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class Token {	
	private String palavra, tipo;
	private int posicao;		
}