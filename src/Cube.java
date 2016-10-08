import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cube {
	public static final Color FRONT_COLOR = Color.RED;
	public static final Color BACK_COLOR = Color.ORANGE;
	public static final Color LEFT_COLOR = Color.BLUE;
	public static final Color RIGHT_COLOR = Color.GREEN;
	public static final Color UP_COLOR = Color.YELLOW;
	public static final Color DOWN_COLOR = Color.WHITE;

	public boolean solved() {
		return this.equals(new Cube());
	}

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
		
		XX
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
	public Side down = new Side();
	public Side left = new Side();
	public Side right = new Side();
	public Side front = new Side();
	public Side back = new Side();
	
	public class InvalidCubeException extends Exception{}
	
	public class Side{
		Square[][] m = new Square[3][3];

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Side)) return false;
			Side other = (Side) obj;
			boolean ret = true;
			for (int i = 0; i < m.length; i++) {
				for (int j = 0, squaresLength = m[i].length; j < squaresLength; j++) {
					ret = ret && (m[i][j].equals(other.m[i][j]));
				}
			}
			return ret;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Cube)) return false;
		Cube other = (Cube) obj;

		boolean ret = up.equals(other.up);
		ret = ret && down.equals(other.down);
		ret = ret && left.equals(other.left);
		ret = ret && right.equals(other.right);
		ret = ret && front.equals(other.front);
		return ret && back.equals(other.back);

	}

	public void reset(){
		for(int i = 0; i < 3; i++)
			System.arraycopy(INIT_UP[i], 0, up.m[i], 0, 3);
		for(int i = 0; i < 3; i++)
			System.arraycopy(INIT_DOWN[i], 0, down.m[i], 0, 3);
		
		for(int i = 0; i < 3; i++)
			System.arraycopy(INIT_LEFT[i], 0, left.m[i], 0, 3);
		for(int i = 0; i < 3; i++)
			System.arraycopy(INIT_RIGHT[i], 0, right.m[i], 0, 3);
		
		for(int i = 0; i < 3; i++)
			System.arraycopy(INIT_FRONT[i], 0, front.m[i], 0, 3);
		for(int i = 0; i < 3; i++)
			System.arraycopy(INIT_BACK[i], 0, back.m[i], 0, 3);
	}
	
	private boolean matrixContains(Square[][] m, Square s){
		for (Square[] aM : m) {
			for (Square anAM : aM) {
				if (anAM == s) return true;
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
	
	private void rotateSide(Side side, boolean clockwise){
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
			rotateSide(side, true);
			rotateSide(side, true);
			rotateSide(side, true);
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
		Side n = clockwise ? l : r;

		Square[] aux = {d.m[0][0], d.m[0][1], d.m[0][2]};
		if(clockwise){
			passFirstLine(r, d);
			passFirstLine(u, r);
			passFirstLine(l, u);
		}else{
			passFirstLine(l, d);
			passFirstLine(u, l);
			passFirstLine(r, u);
		}
		n.m[0][0] = aux[0];
		n.m[0][1] = aux[1];
		n.m[0][2] = aux[2];

		/*
		rotateMatrix(u, true);
		rotateMatrix(u, true);
		rotateMatrix(l, false);
		rotateMatrix(r, true);
		*/
	}
	
	public void turnFront(boolean clockwise){
		rotateSide(front, !clockwise);
		
		rotateSide(right, false);
		rotateSide(left, true);
		rotateSide(up, true);
		rotateSide(up, true);
		
		rotateInCircle(up, down, left, right, clockwise);
		
		rotateSide(right, true);
		rotateSide(left, false);
		rotateSide(up, true);
		rotateSide(up, true);
	}
	
	public void turnBack(boolean clockwise){
		rotateSide(back, !clockwise);
		
		rotateSide(right, true);
		rotateSide(left, false);
		rotateSide(down, true);
		rotateSide(down, true);
		rotateInCircle(up, down, right, left, clockwise);
		rotateSide(right, false);
		rotateSide(left, true);
		rotateSide(down, true);
		rotateSide(down, true);
	}
	
	public void turnUp(boolean clockwise){
		rotateSide(up, !clockwise);
		
		rotateInCircle(back, front, left, right, clockwise);
	}
	
	public void turnDown(boolean clockwise){
		rotateSide(down, !clockwise);
		
		rotateSide(front, true);
		rotateSide(front, true);
		rotateSide(back, true);
		rotateSide(back, true);
		rotateSide(left, true);
		rotateSide(left, true);
		rotateSide(right, true);
		rotateSide(right, true);
		rotateInCircle(front, back, left, right, clockwise);
		rotateSide(front, true);
		rotateSide(front, true);
		rotateSide(back, true);
		rotateSide(back, true);
		rotateSide(left, true);
		rotateSide(left, true);
		rotateSide(right, true);
		rotateSide(right, true);
	}
	
	public void turnLeft(boolean clockwise){
		rotateSide(left, !clockwise);
		
		rotateSide(up, false);
		rotateSide(down, false);
		rotateSide(front, false);
		rotateSide(back, true);
		rotateInCircle(up, down, back, front, clockwise);
		rotateSide(up, true);
		rotateSide(down, true);
		rotateSide(front, true);
		rotateSide(back, false);
	}
	
	public void turnRight(boolean clockwise){
		rotateSide(right, !clockwise);
		
		rotateSide(up, true);
		rotateSide(down, true);
		rotateSide(front, true);
		rotateSide(back, false);
		rotateInCircle(up, down, front, back, clockwise);
		rotateSide(up, false);
		rotateSide(down, false);
		rotateSide(front, false);
		rotateSide(back, true);
	}
	
	public Cube (){
		reset();
	}
	
	//Matrix containing 6 3x3 matrixes
	public Cube(Color[][][] coloredCube) throws InvalidCubeException{
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				front.m[i][j] = Square.XX;
				back.m[i][j] = Square.XX;
				left.m[i][j] = Square.XX;
				right.m[i][j] = Square.XX;
				up.m[i][j] = Square.XX;
				down.m[i][j] = Square.XX;
			}
		}
		
		try{
			Color[][] fM = null, bM = null, lM = null, rM = null, uM = null, dM = null;
			for(int i = 0; i < 6; i++){
				if(coloredCube[i][1][1] == FRONT_COLOR) fM = coloredCube[i]; else
				if(coloredCube[i][1][1] == BACK_COLOR) bM = coloredCube[i]; else
				
				if(coloredCube[i][1][1] == LEFT_COLOR) lM = coloredCube[i]; else
				if(coloredCube[i][1][1] == RIGHT_COLOR) rM = coloredCube[i]; else
				
				if(coloredCube[i][1][1] == UP_COLOR) uM = coloredCube[i]; else
				if(coloredCube[i][1][1] == DOWN_COLOR) dM = coloredCube[i];
			}
			
			
			for(int i = 0; i < 3; i++){
				for(int j = 0; j < 3; j++){
					front.m[i][j] = getCorrespondingSquare(fM, 
										rotate2ColorMatrix(uM), 
										dM, 
										rotateColorMatrix(lM, false), 
										rotateColorMatrix(rM, true), 
										i, j);
					back.m[i][j] = getCorrespondingSquare(bM, 
							uM, 
							rotate2ColorMatrix(dM), 
							rotateColorMatrix(rM, false), 
							rotateColorMatrix(lM, true), 
							i, j);
					left.m[i][j] = getCorrespondingSquare(lM, 
							rotateColorMatrix(uM, true), 
							rotateColorMatrix(dM, true), 
							rotateColorMatrix(bM, false), 
							rotateColorMatrix(fM, true), 
							i, j);
					right.m[i][j] = getCorrespondingSquare(rM, 
							rotateColorMatrix(uM, false), 
							rotateColorMatrix(dM, false), 
							rotateColorMatrix(fM, false), 
							rotateColorMatrix(bM, true), 
							i, j);
					up.m[i][j] = getCorrespondingSquare(uM, 
							bM, 
							fM, 
							lM, 
							rM, 
							i, j);
					down.m[i][j] = getCorrespondingSquare(dM, 
							rotate2ColorMatrix(fM), 
							rotate2ColorMatrix(bM), 
							rotate2ColorMatrix(lM), 
							rotate2ColorMatrix(rM), 
							i, j);
				}
			}
			
			if(matrixContains(front.m, Square.XX) || 
			   matrixContains(back.m, Square.XX) || 
			   matrixContains(left.m, Square.XX) || 
			   matrixContains(right.m, Square.XX) || 
			   matrixContains(up.m, Square.XX) || 
			   matrixContains(down.m, Square.XX)){
				throw new Exception();
			}
			
			ArrayList<Square> testArray = new ArrayList<>();
			Side[] sides = {front, back, left, right, up, down};
			for(Side s : sides){
				for(Square[] aS : s.m){
					for(Square square : aS){
						if(!testArray.contains(square)){
							testArray.add(square);
						}else{
							throw new Exception();
						}
					}
				}
			}
		}catch(Exception e){
			throw new InvalidCubeException();
		}
	}
	
	private Color[][] rotate2ColorMatrix(Color[][] m){
		return rotateColorMatrix(rotateColorMatrix(m, true), true);
	}
	
	private Color[][] rotateColorMatrix(Color[][] m, boolean clockwise){
		Color[][] r = new Color[3][3];
		r[1][1] = m[1][1];
		
		if(clockwise){
			Color aux = m[0][0];
			r[0][0] = m[2][0];
			r[2][0] = m[2][2];
			r[2][2] = m[0][2];
			r[0][2] = aux;
			
			aux = m[0][1];
			r[0][1] = m[1][0];
			r[1][0] = m[2][1];
			r[2][1] = m[1][2];
			r[1][2] = aux;
			
			return r;
		}else{
			r = rotateColorMatrix(m, true);
			r = rotateColorMatrix(r, true);
			r = rotateColorMatrix(r, true);
			return r;
		}
	}
	
	private static Color[][] copyColorMatrix(Color[][] m){
		Color[][] result = new Color[3][3];
		for(int i = 0; i < 3; i++){
			System.arraycopy(m[i], 0, result[i], 0, 3);
		}
		return result;
	}
	
	private Square getCorrespondingSquare(Color[][] m, Color[][] u, Color[][] d, Color[][] l, Color[][] r, int i, int j){
		if(i == 1 && j == 1){
			if(m[1][1] == FRONT_COLOR) 	return Square.F5;
			if(m[1][1] == BACK_COLOR) 	return Square.B5;
			
			if(m[1][1] == LEFT_COLOR) 	return Square.L5;
			if(m[1][1] == RIGHT_COLOR) 	return Square.R5;
			
			if(m[1][1] == UP_COLOR) 	return Square.U5;
			if(m[1][1] == DOWN_COLOR) 	return Square.D5;
		}
		
		m = copyColorMatrix(m);
		
		u = copyColorMatrix(u);
		d = copyColorMatrix(d);
		
		l = copyColorMatrix(l);
		r = copyColorMatrix(r);
		
		//we rotate them, so we only have to check [0,0] and [0,1]
		if(i == 0 && j == 2 || i == 1 && j == 2){
			m = rotateColorMatrix(m, false);
			
			Color[][] aux = u;
			u = r;
			r = d;
			d = l;
			l = aux;
		}else if(i == 2 && j == 0 || i == 1 && j == 0){
			m = rotateColorMatrix(m, true);
			
			Color[][] aux = u;
			u = l;
			l = d;
			d = r;
			r = aux;
		}else if(i == 2 && j == 2 || i == 2 && j == 1){
			m = rotate2ColorMatrix(m);
			
			Color[][] aux = u;
			u = d;
			d = aux;
			aux = r;
			r = l;
			l = aux;
		}
		
		if(i==1 || j == 1){ //se for um dos meios
			Color searched = m[0][1];
			Color upFromSearched = u[0][1];
			if(searched == FRONT_COLOR){
				if(upFromSearched == UP_COLOR) 		return Square.F2;
				if(upFromSearched == LEFT_COLOR) 	return Square.F4;
				if(upFromSearched == RIGHT_COLOR) 	return Square.F6;
				if(upFromSearched == DOWN_COLOR) 	return Square.F8;
			}else if(searched == BACK_COLOR){
				if(upFromSearched == UP_COLOR) 		return Square.B2;
				if(upFromSearched == RIGHT_COLOR) 	return Square.B4;
				if(upFromSearched == LEFT_COLOR) 	return Square.B6;
				if(upFromSearched == DOWN_COLOR) 	return Square.B8;
			}else if(searched == LEFT_COLOR){
				if(upFromSearched == UP_COLOR) 		return Square.L2;
				if(upFromSearched == BACK_COLOR) 	return Square.L4;
				if(upFromSearched == FRONT_COLOR) 	return Square.L6;
				if(upFromSearched == DOWN_COLOR) 	return Square.L8;
			}else if(searched == RIGHT_COLOR){
				if(upFromSearched == UP_COLOR) 		return Square.R2;
				if(upFromSearched == FRONT_COLOR) 	return Square.R4;
				if(upFromSearched == BACK_COLOR) 	return Square.R6;
				if(upFromSearched == DOWN_COLOR) 	return Square.R8;
			}else if(searched == UP_COLOR){
				if(upFromSearched == BACK_COLOR) 	return Square.U2;
				if(upFromSearched == LEFT_COLOR) 	return Square.U4;
				if(upFromSearched == RIGHT_COLOR) 	return Square.U6;
				if(upFromSearched == FRONT_COLOR) 	return Square.U8;
			}else if(searched == DOWN_COLOR){
				if(upFromSearched == FRONT_COLOR) 	return Square.D2;
				if(upFromSearched == LEFT_COLOR) 	return Square.D4;
				if(upFromSearched == RIGHT_COLOR) 	return Square.D6;
				if(upFromSearched == BACK_COLOR) 	return Square.D8;
			}
		}else{ //se for um dos cantos
			Color searched = m[0][0];
			Color upFromSearched = u[0][2];
			
			if(searched == FRONT_COLOR){
				if(upFromSearched == UP_COLOR) 		return Square.F1;
				if(upFromSearched == RIGHT_COLOR) 	return Square.F3;
				if(upFromSearched == LEFT_COLOR) 	return Square.F7;
				if(upFromSearched == DOWN_COLOR) 	return Square.F9;
			}else if(searched == BACK_COLOR){
				if(upFromSearched == UP_COLOR) 		return Square.B1;
				if(upFromSearched == LEFT_COLOR) 	return Square.B3;
				if(upFromSearched == RIGHT_COLOR) 	return Square.B7;
				if(upFromSearched == DOWN_COLOR) 	return Square.B9;
			}else if(searched == LEFT_COLOR){
				if(upFromSearched == UP_COLOR) 		return Square.L1;
				if(upFromSearched == FRONT_COLOR) 	return Square.L3;
				if(upFromSearched == BACK_COLOR) 	return Square.L7;
				if(upFromSearched == DOWN_COLOR) 	return Square.L9;
			}else if(searched == RIGHT_COLOR){
				if(upFromSearched == UP_COLOR) 		return Square.R1;
				if(upFromSearched == BACK_COLOR) 	return Square.R3;
				if(upFromSearched == FRONT_COLOR) 	return Square.R7;
				if(upFromSearched == DOWN_COLOR) 	return Square.R9;
			}else if(searched == UP_COLOR){
				if(upFromSearched == BACK_COLOR) 	return Square.U1;
				if(upFromSearched == RIGHT_COLOR) 	return Square.U3;
				if(upFromSearched == LEFT_COLOR) 	return Square.U7;
				if(upFromSearched == FRONT_COLOR) 	return Square.U9;
			}else if(searched == DOWN_COLOR){
				if(upFromSearched == FRONT_COLOR) 	return Square.D1;
				if(upFromSearched == RIGHT_COLOR) 	return Square.D3;
				if(upFromSearched == LEFT_COLOR) 	return Square.D7;
				if(upFromSearched == BACK_COLOR) 	return Square.D9;
			}
		}
		
		return Square.XX;
	}
	
	public Cube clone(){
		Cube c = new Cube();
		for(int i = 0; i < c.front.m.length; i++){
			for(int j = 0; j < c.front.m[i].length; j++){
				c.front.m[i][j] = front.m[i][j];
				c.back.m[i][j] = back.m[i][j];
				
				c.left.m[i][j] = left.m[i][j];
				c.right.m[i][j] = right.m[i][j];
				
				c.up.m[i][j] = up.m[i][j];
				c.down.m[i][j] = down.m[i][j];
			}
		}
		return c;
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
				result += front.m[i][j] + ", ";
			}
			result += "\n";
		}
		return result + "}";
	}
	
	public void executeCommands(String c){
		Pattern pattern = Pattern.compile("([lrfbduLRFDBU][2']?)");
		Matcher matcher = pattern.matcher(c);
		
		
		while(matcher.find())
		{	
		    executeCommand(matcher.group(1));
		    matcher.start();
		}
		
		System.out.println();
	}
	
	public void executeCommand(String c){
		switch(c.toUpperCase()){
		case "U":  turnUp(true); break;
		case "U'": turnUp(false); break;
		case "U2": turnUp(true); turnUp(true); break;
		
		case "D":  turnDown(true); break;
		case "D'": turnDown(false); break;
		case "D2": turnDown(true); turnDown(true); break;
		
		case "L":  turnLeft(true); break;
		case "L'": turnLeft(false); break;
		case "L2": turnLeft(true); turnLeft(true); break;
		
		case "R":  turnRight(true); break;
		case "R'": turnRight(false); break;
		case "R2": turnRight(true); turnRight(true); break;
		
		case "F":  turnFront(true); break;
		case "F'": turnFront(false); break;
		case "F2": turnFront(true); turnFront(true); break;
		
		case "B":  turnBack(true); break;
		case "B'": turnBack(false); break;
		case "B2": turnBack(true); turnBack(true); break;
		}
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
