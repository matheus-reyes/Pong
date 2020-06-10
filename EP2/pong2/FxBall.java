import java.util.*;
import java.awt.Color;

public class FxBall extends Ball implements IBall{

	/**
		Atributo privado que representa uma fila de coordenadas x em que a bola passou
	*/

	private Queue <Double> ballCx = new LinkedList <Double> ();

	/**
		Atributo privado que representa uma fila de coordenadas y em que a bola passou
	*/

	private Queue <Double> ballCy = new LinkedList <Double> ();

	/**
		Atributo privado que representa o início do jogo
	*/

	private boolean inicio = false;

    /**
		Construtor da classe FxBall. Recebe o mesmo conjunto de parâmetros que o construtor da superclasse.

		@param cx coordenada x da posição inicial da bola (centro do retangulo que a representa).
		@param cy coordenada y da posição inicial da bola (centro do retangulo que a representa).
		@param width largura do retangulo que representa a bola.
		@param height altura do retangulo que representa a bola.
		@param color cor da bola.
		@param speed velocidade da bola (em pixels por millisegundo).
		@param vx componente x do vetor unitário (normalizado) que representa a direção da bola.
		@param vy componente y do vetor unitário (normalizado) que representa a direção da bola.
	*/

    public FxBall(double cx, double cy, double width, double height, java.awt.Color color, double speed, double vx, double vy){
		super(cx, cy, width, height, color, speed, vx, vy);
    }

    /**
		Método que redefine o comportamento de desenho definido na superclasse.
	*/

	public void draw(){

		super.draw();

		//Se é a primeira vez que o método draw é chamado, pega as 20 primeiras posições anteriores da bola
		if(inicio == false){
			for(int i = 0; i < 20; i++){
				ballCx.add(super.getCx());
				ballCy.add(super.getCy());
			}
		}

		//Depois define a variável inicio como true para o procedimento anterior não ocorrer novamente
		inicio = true;

		//Elimina as duas últimas posições
		ballCx.poll();
		ballCy.poll();
		//Adiciona as duas últimas posições
		ballCx.add(super.getCx());
		ballCy.add(super.getCy());

		//Iterador da lista de coordenadas x
		Iterator <Double> iteratorCx = ballCx.iterator();

		//Iterador da lista de coordenadas y
		Iterator <Double> iteratorCy = ballCy.iterator();
		
		//Variável que irá alterar a altura do efeito de rastro
		double refinador = 0.0;

		//While que percorre as duas filas
		while (iteratorCx.hasNext() && iteratorCy.hasNext()) {

			//Desenha um retângulo para representar o efeito de rastro passando as coordenadas antigas
			GameLib.fillRect(iteratorCx.next(), iteratorCy.next(), super.getWidth() / 2, (super.getHeight() / 4) + refinador);
			
			//Atualiza o refinador para melhorar a aparência do efeito de rastro
			refinador += 0.8;
		}
		
	}
}