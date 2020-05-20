import java.awt.*;
import java.util.Random;

/**
	Esta classe representa a bola usada no jogo. A classe princial do jogo (Pong)
	instancia um objeto deste tipo quando a execução é iniciada.
*/

public class Ball {

	/**
		Construtor da classe Ball. Observe que quem invoca o construtor desta classe define a velocidade da bola 
		(em pixels por millisegundo), mas não define a direção deste movimento. A direção do movimento é determinada 
		aleatóriamente pelo construtor.

		@param cx coordenada x da posição inicial da bola (centro do retangulo que a representa).
		@param cy coordenada y da posição inicial da bola (centro do retangulo que a representa).
		@param width largura do retangulo que representa a bola.
		@param height altura do retangulo que representa a bola.
		@param color cor da bola.
		@param speed velocidade da bola (em pixels por millisegundo).
	*/

	private double cx;
	private double cy;
	private double width;
	private double height;
	private Color color;
	private double speed;
	private double speedX;
	private double speedY;

	public Ball(double cx, double cy, double width, double height, Color color, double speed){
		
		this.cx = cx;
		this.cy = cy;
		this.width = width;
		this.height = height;
		this.color = color;
		this.speed = speed;

		//instância um objeto da classe Random
		Random gerador = new Random();
		//Gera um numero aleatório entre 0 e 3 e armazena na variável randomNum
		int randomNum = gerador.nextInt(4);

		//Caso 1, numero = 0
		if(randomNum == 0){
			this.speedX = Math.abs(speed);
			this.speedY = Math.abs(speed);
		//Caso 2, numero = 1
		}else if(randomNum == 1){
			this.speedX = -Math.abs(speed);
			this.speedY = -Math.abs(speed);
		//Caso 3, numero = 2
		}else if(randomNum == 2){
			this.speedX = -Math.abs(speed);
			this.speedY = Math.abs(speed);
		//Caso 4, numero = 3
		}else if(randomNum == 3){
			this.speedX = Math.abs(speed);
			this.speedY = -Math.abs(speed);
		}
	}

	/**
		Método chamado sempre que a bola precisa ser (re)desenhada.
	*/

	public void draw(){

		GameLib.setColor(this.color);
		GameLib.fillRect(this.cx, this.cy, this.width, this.height);
	}

	/**
		Método chamado quando o estado (posição) da bola precisa ser atualizado.
		
		@param delta quantidade de millisegundos que se passou entre o ciclo anterior de atualização do jogo e o atual.
	*/

	public void update(long delta){
		this.cx += this.speedX * (delta / 2);
		this.cy += this.speedY * (delta / 2);
	}

	/**
		Método chamado quando detecta-se uma colisão da bola com um jogador.
	
		@param playerId uma string cujo conteúdo identifica um dos jogadores.
	*/

	public void onPlayerCollision(String playerId){

		if(playerId.equals("Player 1")){

			// Muda a direção do eixo X para positivo (direita)
			this.speedX = Math.abs(this.speed);
		}else if(playerId.equals("Player 2")){

			// Muda a direção do eixo X para negativo (esquerda)
			this.speedX = -Math.abs(this.speed);
		}
	}

	/**
		Método chamado quando detecta-se uma colisão da bola com uma parede.

		@param wallId uma string cujo conteúdo identifica uma das paredes da quadra.
	*/

	public void onWallCollision(String wallId){

		if(wallId.equals("Left")){

			// Muda a direção do eixo X para positivo (direita)
			this.speedX = Math.abs(this.speed);
		}else if(wallId.equals("Right")){

			// Muda a direção do eixo X para negativo (esquerda)
			this.speedX = -Math.abs(this.speed);
		}else if(wallId.equals("Top")){

			// Muda a direção do eixo Y para positivo (cima)
			this.speedY = Math.abs(this.speed);
		}else if(wallId.equals("Bottom")){

			// Muda a direção do eixo X para negativo (baixo)
			this.speedY = -Math.abs(this.speed);
		}
	}

	/**
		Método que verifica se houve colisão da bola com uma parede.

		@param wall referência para uma instância de Wall contra a qual será verificada a ocorrência de colisão da bola.
		@return um valor booleano que indica a ocorrência (true) ou não (false) de colisão.
	*/
	
	public boolean checkCollision(Wall wall){

		// Menor Coordenada de X da parede
		int paredeMinX = (int) (wall.getCx() - (wall.getWidth() / 2)); 
		// Maior Coordenada de X da parede
		int paredeMaxX = (int) (wall.getCx() + (wall.getWidth() / 2));
		// Menor Coordenada de Y da parede
		int paredeMinY = (int) (wall.getCy() - (wall.getHeight() / 2)); 
		// Maior Coordenada de Y da parede
		int paredeMaxY = (int) (wall.getCy() + (wall.getHeight() / 2));

		//Verifica se as coordenadas da bola estão nas coordenadas da parede
		if((this.cx >  paredeMinX) && (this.cx <  paredeMaxX) && (this.cy > paredeMinY) && (this.cy < paredeMaxY)){
			
			//Se estão, chama o método onWallColision passando o ID da parede e retorna true 
			onWallCollision(wall.getId());
			return true;
		}

		//Se não estão no domínio da parede, retorna false
		return false;
	}

	/**
		Método que verifica se houve colisão da bola com um jogador.

		@param player referência para uma instância de Player contra o qual será verificada a ocorrência de colisão da bola.
		@return um valor booleano que indica a ocorrência (true) ou não (false) de colisão.
	*/	

	public boolean checkCollision(Player player){

		// Maior Coordenada de Y do jogador
		int playerHeightMax = (int) (player.getCy() + (player.getHeight() / 2));
		// Menor Coordenada de Y do jogador
		int playerHeightMin = (int) (player.getCy() - (player.getHeight() / 2));
		// Maior Coordenada de X do jogador
		int playerWidthMax = (int) (player.getCx() + (player.getWidth() / 2));
		// Menor Coordenada de X do jogador
		int playerWidthMin = (int) (player.getCx() - (player.getWidth() / 2));

		//Verifica se as coordenadas da bola estão nas coordenadas do jogador
		if((this.cx > playerWidthMin) && (this.cx < playerWidthMax) && (this.cy > playerHeightMin) && (this.cy < playerHeightMax)){

			//Se estão, chama o método onPlayerColision passando o ID do jogador e retorna true 
			onPlayerCollision(player.getId());
			return true;
		}

		//Se não estão no domínio do jogador, retorna false
		return false;
	}

	/**
		Método que devolve a coordenada x do centro do retângulo que representa a bola.
		@return o valor double da coordenada x.
	*/
	
	public double getCx(){

		return this.cx;
	}

	/**
		Método que devolve a coordenada y do centro do retângulo que representa a bola.
		@return o valor double da coordenada y.
	*/

	public double getCy(){

		return this.cy;
	}

	/**
		Método que devolve a velocidade da bola.
		@return o valor double da velocidade.

	*/

	public double getSpeed(){

		return this.speed;
	}

}
