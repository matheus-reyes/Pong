import java.awt.*;

/**
	Esta classe representa um placar no jogo. A classe princial do jogo (Pong)
	instancia dois objeto deste tipo, cada um responsável por gerenciar a pontuação
	de um player, quando a execução é iniciada.
*/

public class Score {

	/**
		Construtor da classe Score.

		@param playerId uma string que identifica o player ao qual este placar está associado.
	*/

	private String playerId;
	private int score;

	public Score(String playerId){
		
		this.playerId = playerId;
		this.score = 0;
	}

	/**
		Método de desenho do placar.
	*/

	public void draw(){

		if(this.playerId == "Player 1"){
			GameLib.drawText(this.playerId + ": " + this.getScore(), 70, GameLib.ALIGN_LEFT);
		}else if(this.playerId == "Player 2"){
			GameLib.drawText(this.playerId + ": " + this.getScore(), 70, GameLib.ALIGN_RIGHT);
		}		
	}

	/**
		Método que incrementa em 1 unidade a contagem de pontos.
	*/

	public void inc(){

		this.score += 1;
	}

	/**
		Método que devolve a contagem de pontos mantida pelo placar.

		@return o valor inteiro referente ao total de pontos.
	*/

	public int getScore(){

		return this.score;
	}
}
