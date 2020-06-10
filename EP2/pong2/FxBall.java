public class FxBall extends Ball implements IBall{

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
	}
}