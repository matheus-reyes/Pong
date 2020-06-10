import java.awt.Color;
import java.lang.reflect.*;
import java.util.*;

/**
	Classe que gerencia uma ou mais bolas presentes em uma partida. Esta classe é a responsável por instanciar 
	e gerenciar a bola principal do jogo (aquela que existe desde o ínicio de uma partida), assim como eventuais 
	bolas extras que apareçam no decorrer da partida. Esta classe também deve gerenciar a interação da(s) bola(s)
	com os alvos, bem como a aplicação dos efeitos produzidos para cada tipo de alvo atingido.
*/

public class BallManager {

	/**
		Atributo privado que representa a bola principal do jogo.	
	*/

	private IBall theBall = null;

	/**
		Atributo privado que representa o tipo (classe) das instâncias de bola que serão criadas por esta classe.
	*/

	private Class<?> ballClass = null;

	/**
		Atributo privado que representa a velocidade da bola antes do boost.	
	*/

	private double defaultSpeed;

	/**
		Lista Ligada de bolas duplicadas	
	*/

	private Queue <IBall> duplicateBalls = new LinkedList <IBall> ();

	/**
		Lista Ligada de bolas duplicadas	
	*/

	private Iterator <IBall> iteratorBalls = duplicateBalls.iterator();

	/**
		Construtor da classe BallManager.
		
		@param className nome da classe que define o tipo das instâncias de bola que serão criadas por esta classe. 
	*/

	public BallManager(String className){

		try{
			ballClass = Class.forName(className);
		}
		catch(Exception e){

			System.out.println("Classe '" + className + "' não reconhecida... Usando 'Ball' como classe padrão.");
			ballClass = Ball.class;
		}
	}

	/**
		Recebe as componetes x e y de um vetor, e devolve as componentes x e y do vetor normalizado (isto é, com comprimento igual a 1.0).
	
		@param x componente x de um vetor que representa uma direção.
		@param y componente y de um vetor que represetna uma direção.

		@return array contendo dois valores double que representam as componentes x (índice 0) e y (índice 1) do vetor normalizado (unitário).
	*/
	private double [] normalize(double x, double y){

		double length = Math.sqrt(x * x + y * y);

		return new double [] { x / length, y / length };
	}
	
	/**
		Cria uma instancia de bola, a partir do tipo (classe) cujo nome foi passado ao construtor desta classe.
		O vetor direção definido por (vx, vy) não precisa estar normalizado. A implemntação do método se encarrega
		de fazer a normalização.

		@param cx coordenada x da posição inicial da bola (centro do retangulo que a representa).
		@param cy coordenada y da posição inicial da bola (centro do retangulo que a representa).
		@param width largura do retangulo que representa a bola.
		@param height altura do retangulo que representa a bola.
		@param color cor da bola.
		@param speed velocidade da bola (em pixels por millisegundo).
		@param vx componente x do vetor (não precisa ser unitário) que representa a direção da bola.
		@param vy componente y do vetor (não precisa ser unitário) que representa a direção da bola.
	*/

	private IBall createBallInstance(double cx, double cy, double width, double height, Color color, double speed, double vx, double vy){

		IBall ball = null;
		double [] v = normalize(vx, vy);

		try{
			Constructor<?> constructor = ballClass.getConstructors()[0];
			ball = (IBall) constructor.newInstance(cx, cy, width, height, color, speed, v[0], v[1]);
			defaultSpeed = speed;
		}
		catch(Exception e){

			System.out.println("Falha na instanciação da bola do tipo '" + ballClass.getName() + "' ... Instanciando bola do tipo 'Ball'");
			ball = new Ball(cx, cy, width, height, color, speed, v[0], v[1]);
		}

		return ball;
	} 

	/**
		Cria a bola principal do jogo. Este método é chamado pela classe Pong, que contem uma instância de BallManager.

		@param cx coordenada x da posição inicial da bola (centro do retangulo que a representa).
		@param cy coordenada y da posição inicial da bola (centro do retangulo que a representa).
		@param width largura do retangulo que representa a bola.
		@param height altura do retangulo que representa a bola.
		@param color cor da bola.
		@param speed velocidade da bola (em pixels por millisegundo).
		@param vx componente x do vetor (não precisa ser unitário) que representa a direção da bola.
		@param vy componente y do vetor (não precisa ser unitário) que representa a direção da bola.
	*/

	public void initMainBall(double cx, double cy, double width, double height, Color color, double speed, double vx, double vy){

		theBall = createBallInstance(cx, cy, width, height, color, speed, vx, vy);
	}

	/**
		Método que desenha todas as bolas gerenciadas pela instância de BallManager.
		Chamado sempre que a(s) bola(s) precisa ser (re)desenhada(s).
	*/

	public void draw(){

		theBall.draw();

		//Percorre todas as bolas e as redesenha
		for (IBall balls : duplicateBalls){
			balls.draw();
		}
	}
	
	/**
		Método que atualiza todas as bolas gerenciadas pela instância de BallManager, em decorrência da passagem do tempo.
		
		@param delta quantidade de millisegundos que se passou entre o ciclo anterior de atualização do jogo e o atual.
	*/

	public void update(long delta){
	
		theBall.update(delta);

		//Percorre todas as bolas e as atualiza
		for (IBall balls : duplicateBalls){
			balls.update(delta);
		}

	}
	
	/**
		Método que processa as colisões entre as bolas gerenciadas pela instância de BallManager com uma parede.

		@param wall referência para uma instância de Wall para a qual será verificada a ocorrência de colisões.
		@return um valor int que indica quantas bolas colidiram com a parede (uma vez que é possível que mais de 
		uma bola tenha entrado em contato com a parede ao mesmo tempo).
	*/

	public int checkCollision(Wall wall){

		int hits = 0;

		if(theBall.checkCollision(wall)) hits++;

		//Percorre todas as bolas e trata as colisões com a parede e soma um hit
		for (IBall balls : duplicateBalls){
			if(balls.checkCollision(wall)) hits++;
		}

		return hits;
	}

	/**
		Método que processa as colisões entre as bolas gerenciadas pela instância de BallManager com um player.

		@param player referência para uma instância de Player para a qual será verificada a ocorrência de colisões.
	*/
	
	public void checkCollision(Player player){

		theBall.checkCollision(player);

		//Percorre todas as bolas e trata as colisões com o jogador
		for (IBall balls : duplicateBalls){
			balls.checkCollision(player);
		}

	}

	/**
		Método que processa as colisões entre as bolas gerenciadas pela instância de BallManager com um alvo.

		@param target referência para uma instância de Target para a qual será verificada a ocorrência de colisões.
	*/

	public void checkCollision(Target target){

		//Se a bola principal realizou colisão com algum alvo
		if(theBall.checkCollision(target)){

			
			//Se o alvo for BoostTarget
			if(target instanceof BoostTarget){
				
				//Se a bola tem a velocidade padrão, ou seja, ainda não teve a velocidade aumentada
				if(theBall.getSpeed() == defaultSpeed){

					//Altera a velocidade da bola de acordo com a constante BOOST_FACTOR
					theBall.setSpeed(defaultSpeed * BoostTarget.BOOST_FACTOR);

					//Inicializa o timer
					timerBoost();
				}


			//Se o alvo for DuplicatorTarget
			}else if(target instanceof DuplicatorTarget){

				//Define os eixos aleatóriamente
				double vx = 0.85 + Math.random() * 0.15;
				double vy = Math.sqrt(1.0 - vx * vx);
				if(Math.random() < 0.5) vx = -vx;

				//Cria uma instância de uma nova bola
				IBall newBall = (IBall) createBallInstance(theBall.getCx(), theBall.getCy(), theBall.getWidth(), theBall.getHeight(), Color.RED, defaultSpeed, vx, vy);

				//Adiciona a nova bola na fila
				duplicateBalls.add(newBall);

				//Inicia o timer
				timerDuplicator();

			}
			
		}

		// //Percorre todas as bolas
		// while (iteratorBalls.hasNext()){
		// 	IBall bola = (IBall) iteratorBalls.next();
		// 	//Se alguma bola colidiu com um alvo
		// 	if(bola.checkCollision(target)){
				
		// 		//Se o alvo for BoostTarget
		// 		if(target instanceof BoostTarget){
				
				
				
		// 		//Se o alvo for DuplicatorTarget
		// 		}else if(target instanceof DuplicatorTarget){
					
		// 			//Define os eixos aleatóriamente
		// 			double vx = 0.85 + Math.random() * 0.15;
		// 			double vy = Math.sqrt(1.0 - vx * vx);
		// 			if(Math.random() < 0.5) vx = -vx;

		// 			//Cria uma instância de uma nova bola
		// 			IBall newBallDuplicated = (IBall) createBallInstance(theBall.getCx(), theBall.getCy(), theBall.getWidth(), theBall.getHeight(), Color.RED, defaultSpeed, vx, vy);

		// 			//Adiciona a nova bola na fila
		// 			duplicateBalls.add(newBallDuplicated);

		// 			//Inicia o timer
		// 			timerDuplicator();

		// 		}
		// 	}
		// }
	}

	/**
		Método que inicia um timer e altera a velocidade da bola quando o timer chega a constante BOOST_DURATION
	*/

	private void timerBoost(){

		Timer timer = new Timer();

		TimerTask task = new TimerTask(){

			//Altera a velocidade da bola de volta a velocidade inicial
			public void run(){
				theBall.setSpeed(defaultSpeed);
			}
		};

		//Armazena o tempo em milisegundos da constante
		long delay = BoostTarget.BOOST_DURATION;

		// Executa o timer depois de passar o tempo
		timer.schedule(task, delay);
	}

	/**
		Método que inicia um timer e exclui uma bola duplicada quando passado o tempo de duração dela
	*/

	private void timerDuplicator(){

		Timer timer = new Timer();

		TimerTask task = new TimerTask(){

			//Exclui a primeira bola criada
			public void run(){
				duplicateBalls.poll();
			}
		};

		//Armazena o tempo em milisegundos da constante
		long delay = DuplicatorTarget.EXTRA_BALL_DURATION;

		// Executa o timer depois de passar o tempo
		timer.schedule(task, delay);
	}
}