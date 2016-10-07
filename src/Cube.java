import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Cube {
	public final Color FRONT_COLOR = Color.RED;
	public final Color BACK_COLOR = Color.ORANGE;
	public final Color LEFT_COLOR = Color.BLUE;
	public final Color RIGHT_COLOR = Color.GREEN;
	public final Color UP_COLOR = Color.YELLOW;
	public final Color DOWN_COLOR = Color.WHITE;
	
	enum Square{
		F1, F2, F3,
		F4, F5, F6,
		F7, F8, F9,
		
		B1, B2, B3,
		B4, B5, B6,
		B7, B8, B9,
		
		L1, L2, L3,
		L4, L5, L6,
		L7, L8, L9,
		
		R1, R2, R3,
		R4, R5, R6,
		R7, R8, R9,
		
		U1, U2, U3,
		U4, U5, U6,
		U7, U8, U9,
		
		D1, D2, D3,
		D4, D5, D6,
		D7, D8, D9,
	}
	
	private final Square[][] INIT_FRONT = {{Square.F1, Square.F2, Square.F3},
			 							  {Square.F4, Square.F5, Square.F6},
			 							  {Square.F7, Square.F8, Square.F9}};
	private final Square[][] INIT_BACK =  {{Square.B1, Square.B2, Square.B3},
										  {Square.B4, Square.B5, Square.B6},
										  {Square.B7, Square.B8, Square.B9}};
	private final Square[][] INIT_LEFT =  {{Square.L1, Square.L2, Square.L3},
										  {Square.L4, Square.L5, Square.L6},
										  {Square.L7, Square.L8, Square.L9}};
	private final Square[][] INIT_RIGHT = {{Square.R1, Square.R2, Square.R3},
										  {Square.R4, Square.R5, Square.R6},
										  {Square.R7, Square.R8, Square.R9}};
	private final Square[][] INIT_UP =    {{Square.U1, Square.U2, Square.U3},
									   	  {Square.U4, Square.U5, Square.U6},
									   	  {Square.U7, Square.U8, Square.U9}};
	private final Square[][] INIT_DOWN =  {{Square.D1, Square.D2, Square.D3},
										  {Square.D4, Square.D5, Square.D6},
										  {Square.D7, Square.D8, Square.D9}};
	

	public Side up = new Side();
	private Side down = new Side();
	private Side left = new Side();
	private Side right = new Side();
	private Side front = new Side();
	private Side back = new Side();
	
	public class Side{
		Square[][] m = new Square[3][3];
	}
	
	public void reset(){
		for(int i = 0; i < 3; i++) for(int j = 0; j < 3; j++) up.m[i][j] = INIT_UP[i][j];
		for(int i = 0; i < 3; i++) for(int j = 0; j < 3; j++) down.m[i][j] = INIT_DOWN[i][j];
		
		for(int i = 0; i < 3; i++) for(int j = 0; j < 3; j++) left.m[i][j] = INIT_LEFT[i][j];
		for(int i = 0; i < 3; i++) for(int j = 0; j < 3; j++) right.m[i][j] = INIT_RIGHT[i][j];
		
		for(int i = 0; i < 3; i++) for(int j = 0; j < 3; j++) front.m[i][j] = INIT_FRONT[i][j];
		for(int i = 0; i < 3; i++) for(int j = 0; j < 3; j++) back.m[i][j] = INIT_BACK[i][j];
	}
	
	private boolean matrixContains(Square[][] m, Square s){
		for(int i = 0; i < m.length; i++){
			for(int j = 0; j < m[i].length; j++){
				if(m[i][j] == s) return true;
			}
		}
		
		return false;
	}
	
	public Color getSquareColor(Square s){
		if(matrixContains(INIT_UP, s)) 		return UP_COLOR;
		if(matrixContains(INIT_DOWN, s)) 	return DOWN_COLOR;
		
		if(matrixContains(INIT_LEFT, s)) 	return LEFT_COLOR;
		if(matrixContains(INIT_RIGHT, s))	return RIGHT_COLOR;
		
		if(matrixContains(INIT_FRONT, s)) 	return FRONT_COLOR;
		if(matrixContains(INIT_BACK, s)) 	return BACK_COLOR;
		
		return Color.BLACK;
	}
	
	public void rotateMatrix(Side side, boolean clockwise){
		if(clockwise){
			Square aux = side.m[0][0];
			side.m[0][0] = side.m[0][2];
			side.m[0][2] = side.m[2][2];
			side.m[2][2] = side.m[2][0];
			side.m[2][0] = aux;
			
			aux = side.m[0][1];
			side.m[0][1] = side.m[1][2];
			side.m[1][2] = side.m[2][1];
			side.m[2][1] = side.m[1][0];
			side.m[1][0] = aux;
		}else{
			rotateMatrix(side, true);
			rotateMatrix(side, true);
			rotateMatrix(side, true);
		}
	}
	
	private void passFirstLine(Side from, Side to){
		to.m[0][0] = from.m[0][0];
		to.m[0][1] = from.m[0][1];
		to.m[0][2] = from.m[0][2];
	}
	
	private void rotateInCircle(Side u, Side d, Side l, Side r, boolean clockwise){
		/*
		rotateMatrix(r, false);
		rotateMatrix(l, true);
		rotateMatrix(u, true);
		rotateMatrix(u, true);
		*/
		
		if(clockwise){
			Square[] aux = {d.m[0][0], d.m[0][1], d.m[0][2]};
			passFirstLine(r, d);
			passFirstLine(u, r);
			passFirstLine(l, u);
			l.m[0][0] = aux[0];
			l.m[0][1] = aux[1];
			l.m[0][2] = aux[2];
		}else{
			Square[] aux = {d.m[0][0], d.m[0][1], d.m[0][2]};
			passFirstLine(l, d);
			passFirstLine(u, l);
			passFirstLine(r, u);
			r.m[0][0] = aux[0];
			r.m[0][1] = aux[1];
			r.m[0][2] = aux[2];
		}
		/*
		rotateMatrix(u, true);
		rotateMatrix(u, true);
		rotateMatrix(l, false);
		rotateMatrix(r, true);
		*/
	}
	
	public void turnFront(boolean clockwise){
		rotateMatrix(front, !clockwise);
		
		rotateMatrix(right, false);
		rotateMatrix(left, true);
		rotateMatrix(up, true);
		rotateMatrix(up, true);
		
		rotateInCircle(up, down, left, right, clockwise);
		
		rotateMatrix(right, true);
		rotateMatrix(left, false);
		rotateMatrix(up, true);
		rotateMatrix(up, true);
	}
	
	public void turnBack(boolean clockwise){
		rotateMatrix(back, !clockwise);
		
		rotateMatrix(right, true);
		rotateMatrix(left, false);
		rotateMatrix(down, true);
		rotateMatrix(down, true);
		rotateInCircle(up, down, right, left, clockwise);
		rotateMatrix(right, false);
		rotateMatrix(left, true);
		rotateMatrix(down, true);
		rotateMatrix(down, true);
	}
	
	public void turnUp(boolean clockwise){
		rotateMatrix(up, !clockwise);
		
		rotateInCircle(back, front, left, right, clockwise);
	}
	
	public void turnDown(boolean clockwise){
		rotateMatrix(down, !clockwise);
		
		rotateMatrix(front, true);
		rotateMatrix(front, true);
		rotateMatrix(back, true);
		rotateMatrix(back, true);
		rotateMatrix(left, true);
		rotateMatrix(left, true);
		rotateMatrix(right, true);
		rotateMatrix(right, true);
		rotateInCircle(front, back, left, right, clockwise);
		rotateMatrix(front, true);
		rotateMatrix(front, true);
		rotateMatrix(back, true);
		rotateMatrix(back, true);
		rotateMatrix(left, true);
		rotateMatrix(left, true);
		rotateMatrix(right, true);
		rotateMatrix(right, true);
	}
	
	public void turnLeft(boolean clockwise){
		rotateMatrix(left, !clockwise);
		
		rotateMatrix(up, false);
		rotateMatrix(down, false);
		rotateMatrix(front, false);
		rotateMatrix(back, true);
		rotateInCircle(up, down, back, front, clockwise);
		rotateMatrix(up, true);
		rotateMatrix(down, true);
		rotateMatrix(front, true);
		rotateMatrix(back, false);
	}
	
	public void turnRight(boolean clockwise){
		rotateMatrix(right, !clockwise);
		
		rotateMatrix(up, true);
		rotateMatrix(down, true);
		rotateMatrix(front, true);
		rotateMatrix(back, false);
		rotateInCircle(up, down, front, back, clockwise);
		rotateMatrix(up, false);
		rotateMatrix(down, false);
		rotateMatrix(front, false);
		rotateMatrix(back, true);
	}
	
	public Cube (){
		reset();
	}
	
	public String getSquareColorName(Square s){
		if(matrixContains(INIT_UP, s)) 		return "Y";
		if(matrixContains(INIT_DOWN, s)) 	return "W";
		
		if(matrixContains(INIT_LEFT, s)) 	return "B";
		if(matrixContains(INIT_RIGHT, s))	return "G";
		
		if(matrixContains(INIT_FRONT, s)) 	return "R";
		if(matrixContains(INIT_BACK, s)) 	return "O";
		
		return "X";
	}
	
	public String toString(){
		String result = "{\n";
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				result += getSquareColorName(left.m[i][j]) + ", ";
			}
			result += "\n";
		}
		return result + "}";
	}
	
	public void executeCommands(String c){
		
	}
	
	public void draw(GraphicsContext gc){
		int hOffset = 10;
		int vOffset = 10;
		int hPadding = 4;
		int vPadding = 4;
		
		int hDiag = 21;
		int vDiag = 21;
		
		int sizeOfSquare = 35;
		
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
		
		gc.setFill(Color.BLACK);
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(2f);
		
		//front side
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				gc.setFill(getSquareColor(front.m[j][i]));
				gc.fillRect(hPadding + hOffset + sizeOfSquare * (3 + i), vOffset + sizeOfSquare*(4 + j), sizeOfSquare, sizeOfSquare);
				gc.setStroke(Color.BLACK);
				gc.strokeRect(hPadding + hOffset + sizeOfSquare * (3 + i),vOffset + sizeOfSquare*(4 + j), sizeOfSquare, sizeOfSquare);
			}
		}
		
		//left side
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				gc.setFill(getSquareColor(left.m[j][i]));
				gc.fillRect(hOffset + sizeOfSquare * i, vOffset + sizeOfSquare*(4 + j), sizeOfSquare, sizeOfSquare);
				gc.setStroke(Color.BLACK);
				gc.strokeRect(hOffset + sizeOfSquare * i, vOffset + sizeOfSquare*(4 + j), sizeOfSquare, sizeOfSquare);
			}
		}
		
		//down side
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				gc.setFill(getSquareColor(down.m[j][i]));
				gc.fillRect(hPadding + hOffset + sizeOfSquare * (3 + i), vPadding + vOffset + sizeOfSquare*(4 + 3 + j), sizeOfSquare, sizeOfSquare);
				gc.setStroke(Color.BLACK);
				gc.strokeRect(hPadding + hOffset + sizeOfSquare * (3 + i),vPadding +  vOffset + sizeOfSquare*(4 + 3 + j), sizeOfSquare, sizeOfSquare);
			}
		}
		
		//up side
		for(int i = 0; i < 3; i++){
			int initialPointX = hPadding + hOffset + sizeOfSquare * 3 + hDiag * 3 + 1;
			int initialPointY = - vPadding + vOffset + sizeOfSquare*4 - 3 * vDiag;
			
			for(int j = 0; j < 3; j++){
				double[] xs = {
						initialPointX + sizeOfSquare * j - hDiag * i,
						initialPointX + sizeOfSquare * (j + 1)  - hDiag * i,
						initialPointX + sizeOfSquare * (j + 1) - hDiag * (i + 1),
						initialPointX + sizeOfSquare * j - hDiag * (i + 1),
				}; 
				double[] ys = {
						initialPointY + vDiag * i,
						initialPointY + vDiag * i,
						initialPointY + vDiag * (i + 1),
						initialPointY + vDiag * (i + 1)
				};
				gc.setFill(getSquareColor(up.m[i][j]));
				gc.fillPolygon(xs, ys, 4);
				gc.setStroke(Color.BLACK);
				gc.strokePolygon(xs, ys, 4);
			}
		}
		
		//right side
		for(int i = 0; i < 3; i++){
			int initialPointX = hPadding*2 + hOffset + sizeOfSquare * 6;
			int initialPointY = vOffset + sizeOfSquare*4;
			
			for(int j = 0; j < 3; j++){
				double[] ys = {
						initialPointY + sizeOfSquare * j - vDiag * i,
						initialPointY + sizeOfSquare * (j + 1) - vDiag * i,
						initialPointY + sizeOfSquare * (j + 1) - vDiag * (i + 1),
						initialPointY + sizeOfSquare * j - vDiag * (i + 1),
				}; 
				double[] xs = {
						initialPointX + hDiag * i,
						initialPointX + hDiag * i,
						initialPointX + hDiag * (i + 1),
						initialPointX + hDiag * (i + 1)
				};
				gc.setFill(getSquareColor(right.m[j][i]));
				gc.fillPolygon(xs, ys, 4);
				gc.setStroke(Color.BLACK);
				gc.strokePolygon(xs, ys, 4);
			}
		}
		
		//back side
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				gc.setFill(getSquareColor(back.m[j][i]));
				gc.fillRect(hPadding*3 + hOffset + sizeOfSquare * (6 + i) + hDiag * 3, vPadding + vOffset + sizeOfSquare*(2 + j) + 2, sizeOfSquare, sizeOfSquare);
				gc.setStroke(Color.BLACK);
				gc.strokeRect(hPadding*3 + hOffset + sizeOfSquare * (6 + i) + hDiag * 3,vPadding +  vOffset + sizeOfSquare*(2 + j) + 2, sizeOfSquare, sizeOfSquare);
			}
		}
	}
}
