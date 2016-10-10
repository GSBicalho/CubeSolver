import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cube {
	public static final Color FRONT_COLOR = Color.RED;
	public static final Color BACK_COLOR = Color.ORANGE;
	public static final Color LEFT_COLOR = Color.BLUE;
	public static final Color RIGHT_COLOR = Color.GREEN;
	public static final Color UP_COLOR = Color.YELLOW;
	public static final Color DOWN_COLOR = Color.WHITE;

	public static final Color ERROR_COLOR = Color.DARKGRAY;


	public boolean solved() {
		return this.equals(new Cube());
	}

	public static enum Square{
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
	
	private static final Square[][] INIT_FRONT = {{Square.F1, Square.F2, Square.F3},
			 							  {Square.F4, Square.F5, Square.F6},
			 							  {Square.F7, Square.F8, Square.F9}};
	private static final Square[][] INIT_BACK =  {{Square.B1, Square.B2, Square.B3},
										  {Square.B4, Square.B5, Square.B6},
										  {Square.B7, Square.B8, Square.B9}};
	private static final Square[][] INIT_LEFT =  {{Square.L1, Square.L2, Square.L3},
										  {Square.L4, Square.L5, Square.L6},
										  {Square.L7, Square.L8, Square.L9}};
	private static final Square[][] INIT_RIGHT = {{Square.R1, Square.R2, Square.R3},
										  {Square.R4, Square.R5, Square.R6},
										  {Square.R7, Square.R8, Square.R9}};
	private static final Square[][] INIT_UP =    {{Square.U1, Square.U2, Square.U3},
									   	  {Square.U4, Square.U5, Square.U6},
									   	  {Square.U7, Square.U8, Square.U9}};
	private static final Square[][] INIT_DOWN =  {{Square.D1, Square.D2, Square.D3},
										  {Square.D4, Square.D5, Square.D6},
										  {Square.D7, Square.D8, Square.D9}};
	
	public Side up = new Side();
	public Side down = new Side();
	public Side left = new Side();
	public Side right = new Side();
	public Side front = new Side();
	public Side back = new Side();
	
	public static class InvalidCubeException extends Exception{}
	
	public static class Side{
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
	
	private static boolean matrixContains(Square[][] m, Square s){
		for (Square[] aM : m) {
			for (Square anAM : aM) {
				if (anAM == s) return true;
			}
		}
		
		return false;
	}
	
	public static Color getSquareColor(Square s){
		if(matrixContains(INIT_UP, s)) 		return UP_COLOR;
		if(matrixContains(INIT_DOWN, s)) 	return DOWN_COLOR;
		
		if(matrixContains(INIT_LEFT, s)) 	return LEFT_COLOR;
		if(matrixContains(INIT_RIGHT, s))	return RIGHT_COLOR;
		
		if(matrixContains(INIT_FRONT, s)) 	return FRONT_COLOR;
		if(matrixContains(INIT_BACK, s)) 	return BACK_COLOR;
		
		return Color.BLACK;
	}
	
	private static void rotateSide(Side side, boolean clockwise){
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
	
	private static void passFirstLine(Side from, Side to){
		to.m[0][0] = from.m[0][0];
		to.m[0][1] = from.m[0][1];
		to.m[0][2] = from.m[0][2];
	}
	
	private static void rotateInCircle(Side u, Side d, Side l, Side r, boolean clockwise){
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
	
	public Cube turnFront(boolean clockwise){
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

		return this;
	}
	
	public Cube turnBack(boolean clockwise){
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

		return this;
	}
	
	public Cube turnUp(boolean clockwise){
		rotateSide(up, !clockwise);
		
		rotateInCircle(back, front, left, right, clockwise);

		return this;
	}
	
	public Cube turnDown(boolean clockwise){
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

		return this;
	}
	
	public Cube turnLeft(boolean clockwise){
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

		return this;
	}
	
	public Cube turnRight(boolean clockwise){
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

		return this;
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
	
	private static Color[][] rotate2ColorMatrix(Color[][] m){
		return rotateColorMatrix(rotateColorMatrix(m, true), true);
	}
	
	private static Color[][] rotateColorMatrix(Color[][] m, boolean clockwise){
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
	
	private static Square getCorrespondingSquare(Color[][] m, Color[][] u, Color[][] d, Color[][] l, Color[][] r, int i, int j){
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
	
	public static String getSquareColorName(Square s){
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

	public static String reverseCommands(String c){

		Pattern pattern = Pattern.compile("([lrfbduLRFDBU][2']?)");
		Matcher matcher = pattern.matcher(c);

		StringBuilder reverse = new StringBuilder("");

		while(matcher.find())
		{
			String current = matcher.group(1);
			if (current.contains("'")){
				reverse.insert(0, current.replaceAll("'", ""));
			} else if (current.contains("2")) {
				reverse.insert(0, current);
			} else {
				reverse.insert(0, current + "'");
			}
			matcher.start();
		}

		return reverse.toString();

	}

	public Cube executeCommands(String c){
		Pattern pattern = Pattern.compile("([lrfbduLRFDBU][2']?)");
		Matcher matcher = pattern.matcher(c);
		
		
		while(matcher.find())
		{	
		    executeCommand(matcher.group(1));
		    matcher.start();
		}


		return this;
	}
	
	public Cube executeCommand(String c){
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

		return this;
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
	
	public static enum CornerCubeletName{
		FLU((byte)0), FRU((byte)1), FLD((byte)2), FRD((byte)3), BRU((byte)4), BLU((byte)5), BRD((byte)6), BLD((byte)7);
		
		public byte v;
		CornerCubeletName(byte v){
			this.v = v;
		}
	}
	
	public static enum CornerCubeletRotation{
		R1((byte)0), R2((byte)1), R3((byte)2);
		
		public byte v;
		CornerCubeletRotation(byte v){
			this.v = v;
		}
	}
	
	public static class CornerCubelet{
		public CornerCubeletName name;
		public CornerCubeletRotation rotation;
		
		public CornerCubelet(CornerCubeletName name, CornerCubeletRotation rotation){
			this.name = name;
			this.rotation = rotation;
		}
	}
	
	public CornerCubelet[] getCornerCubelets(){
		CornerCubelet[] m = new CornerCubelet[8];
		
		int[][] corners = {{0,0},{0,2},{2,0},{2,2}};
		
		for(int i = 0; i < 4; i++){
			Square s = front.m[corners[i][0]][corners[i][1]];
			m[i] = new CornerCubelet(squareToCornerCubelet(s), null);

			if(matrixContains(INIT_FRONT, s) || matrixContains(INIT_BACK, s)){
				m[i].rotation = CornerCubeletRotation.R1;
			}else if(matrixContains(INIT_LEFT, s) || matrixContains(INIT_RIGHT, s)){
				m[i].rotation = CornerCubeletRotation.R2;
			}else{
				m[i].rotation = CornerCubeletRotation.R3;
			}
		}
		
		for(int i = 0; i < 4; i++){
			Square s = back.m[corners[i][0]][corners[i][1]];
			m[i + 4] = new CornerCubelet(squareToCornerCubelet(s), null);
			
			if(matrixContains(INIT_FRONT, s) || matrixContains(INIT_BACK, s)){
				m[i + 4].rotation = CornerCubeletRotation.R1;
			}else if(matrixContains(INIT_LEFT, s) || matrixContains(INIT_RIGHT, s)){
				m[i + 4].rotation = CornerCubeletRotation.R2;
			}else{
				m[i + 4].rotation = CornerCubeletRotation.R3;
			}
		}
		
		return m;
	}
	
	public static CornerCubeletName squareToCornerCubelet(Square s){
		if(s == Square.U1 || s == Square.F1 || s == Square.L3) return CornerCubeletName.FLU;
		if(s == Square.U3 || s == Square.R1 || s == Square.F3) return CornerCubeletName.FRU;
		if(s == Square.U7 || s == Square.B1 || s == Square.R3) return CornerCubeletName.BRU;
		if(s == Square.U9 || s == Square.L1 || s == Square.B3) return CornerCubeletName.BLU;
		
		if(s == Square.D1 || s == Square.B9 || s == Square.L7) return CornerCubeletName.BLD;
		if(s == Square.D3 || s == Square.F9 || s == Square.R7) return CornerCubeletName.FRD;
		if(s == Square.D7 || s == Square.L9 || s == Square.F7) return CornerCubeletName.FLD;
		if(s == Square.D9 || s == Square.R9 || s == Square.B7) return CornerCubeletName.BRD;
		
		return null;
	}

	public static enum EdgeCubeletName{
		FU((byte)0), FL((byte)1), FR((byte)2), FD((byte)3),
		BU((byte)4), BR((byte)5), BL((byte)6), BD((byte)7),
		MLU((byte)8), MLD((byte)9), MRU((byte)10), MRD((byte)11);
		
		byte v;
		EdgeCubeletName(byte v){
			this.v = v;
		}
	}
	
	public static enum EdgeCubeletRotation{
		R1((byte)0), R2((byte)1);
		byte v;
		EdgeCubeletRotation(byte v){
			this.v = v;
		}
	}
	
	public static class EdgeCubelet{
		public EdgeCubeletName name;
		public EdgeCubeletRotation rotation;
		
		public EdgeCubelet(EdgeCubeletName name, EdgeCubeletRotation rotation){
			this.name = name;
			this.rotation = rotation;
		}
	}
	
	public static EdgeCubeletName squareToEdgeCubelet(Square s){
		if(s == Square.F2 || s == Square.U8) return EdgeCubeletName.FU;
		if(s == Square.F4 || s == Square.L6) return EdgeCubeletName.FL;
		if(s == Square.F6 || s == Square.R4) return EdgeCubeletName.FR;
		if(s == Square.F8 || s == Square.D2) return EdgeCubeletName.FD;
		
		if(s == Square.U4 || s == Square.L2) return EdgeCubeletName.MLU;
		if(s == Square.U6 || s == Square.R2) return EdgeCubeletName.MRU;
		if(s == Square.D4 || s == Square.L8) return EdgeCubeletName.MLD;
		if(s == Square.D6 || s == Square.R8) return EdgeCubeletName.MRD;
		
		if(s == Square.B2 || s == Square.U2) return EdgeCubeletName.BU;
		if(s == Square.B4 || s == Square.R6) return EdgeCubeletName.BR;
		if(s == Square.B6 || s == Square.L4) return EdgeCubeletName.BL;
		if(s == Square.B8 || s == Square.D8) return EdgeCubeletName.BD;
		
		return null;
	}
	
	public EdgeCubelet[] getEdgeCubelets(){
		EdgeCubelet[] m = new EdgeCubelet[12];
		
		ArrayList<EdgeCubeletName> frontCubelets = new ArrayList<>();
		Collections.addAll(frontCubelets,
				EdgeCubeletName.FD, EdgeCubeletName.FL,
				EdgeCubeletName.FR, EdgeCubeletName.FU
		);
		
		ArrayList<EdgeCubeletName> middleCubelets = new ArrayList<EdgeCubeletName>();
		Collections.addAll(middleCubelets, new EdgeCubeletName[]{EdgeCubeletName.MLD, EdgeCubeletName.MRD, EdgeCubeletName.MLU, EdgeCubeletName.MRU});
		
		ArrayList<EdgeCubeletName> backCubelets = new ArrayList<>();
		Collections.addAll(backCubelets,
				EdgeCubeletName.BD, EdgeCubeletName.BL,
				EdgeCubeletName.BR, EdgeCubeletName.BU
		);
		
		int[][] edgesFrontAndBack = {{0,1},{1,0},{1,2},{2,1}};
		int[][] edgesSides = {{0, 1}, {2, 1}};
		
		for(int i = 0; i < 4; i++){
			Square s = front.m[edgesFrontAndBack[i][0]][edgesFrontAndBack[i][1]];
			m[i] = new EdgeCubelet(squareToEdgeCubelet(s), null);

			if(matrixContains(INIT_FRONT, s) || matrixContains(INIT_BACK, s)){
				m[i].rotation = EdgeCubeletRotation.R1;
			}else if(middleCubelets.contains(m[i].name) && (matrixContains(INIT_LEFT, s) || matrixContains(INIT_RIGHT, s))){
				m[i].rotation = EdgeCubeletRotation.R1;
			}else{
				m[i].rotation = EdgeCubeletRotation.R2;
			}
		}
		
		for(int i = 0; i < 4; i++){
			Square s = back.m[edgesFrontAndBack[i][0]][edgesFrontAndBack[i][1]];
			m[i + 4] = new EdgeCubelet(squareToEdgeCubelet(s), null);

			if(matrixContains(INIT_FRONT, s) || matrixContains(INIT_BACK, s)){
				m[i + 4].rotation = EdgeCubeletRotation.R1;
			}else if(middleCubelets.contains(m[i + 4].name) && (matrixContains(INIT_LEFT, s) || matrixContains(INIT_RIGHT, s))){
				m[i + 4].rotation = EdgeCubeletRotation.R1;
			}else{
				m[i + 4].rotation = EdgeCubeletRotation.R2;
			}
		}
		
		for(int i = 0; i < 2; i++){
			Square s = left.m[edgesSides[i][0]][edgesSides[i][1]];
			m[i + 8] = new EdgeCubelet(squareToEdgeCubelet(s), null);

			if(matrixContains(INIT_FRONT, s) || matrixContains(INIT_BACK, s)){
				m[i + 8].rotation = EdgeCubeletRotation.R1;
			}else if(middleCubelets.contains(m[i + 8].name) && (matrixContains(INIT_LEFT, s) || matrixContains(INIT_RIGHT, s))){
				m[i + 8].rotation = EdgeCubeletRotation.R1;
			}else{
				m[i + 8].rotation = EdgeCubeletRotation.R2;
			}
		}
		
		for(int i = 0; i < 2; i++){
			Square s = right.m[edgesSides[i][0]][edgesSides[i][1]];
			m[i + 10] = new EdgeCubelet(squareToEdgeCubelet(s), null);

			if(matrixContains(INIT_FRONT, s) || matrixContains(INIT_BACK, s)){
				m[i + 10].rotation = EdgeCubeletRotation.R1;
			}else if(middleCubelets.contains(m[i + 10].name) && (matrixContains(INIT_LEFT, s) || matrixContains(INIT_RIGHT, s))){
				m[i + 10].rotation = EdgeCubeletRotation.R1;
			}else{
				m[i + 10].rotation = EdgeCubeletRotation.R2;
			}
		}
		
		return m;
	}

	public static Square[] edgeCubeletNameToSquares(EdgeCubeletName ecn){
		switch(ecn){
		case BD:
			return new Square[]{Square.B8, Square.D8};
		case BL:
			return new Square[]{Square.B6, Square.L4};
		case BR:
			return new Square[]{Square.B4, Square.R6};
		case BU:
			return new Square[]{Square.B2, Square.U2};
		case FD:
			return new Square[]{Square.F8, Square.D2};
		case FL:
			return new Square[]{Square.F4, Square.L6};
		case FR:
			return new Square[]{Square.F6, Square.R4};
		case FU:
			return new Square[]{Square.F2, Square.U8};
		case MLD:
			return new Square[]{Square.L8, Square.D4};
		case MRD:
			return new Square[]{Square.R8, Square.D6};
		case MLU:
			return new Square[]{Square.L2, Square.U4};
		case MRU:
			return new Square[]{Square.R2, Square.U6};
		default:
			break;

		}
		return null;
	}

	public static Square[] cornerCubeletNameToSquares(CornerCubeletName ccn){
		switch(ccn){
		case BLD:
			return new Square[]{Square.B9, Square.L7, Square.D1};
		case BLU:
			return new Square[]{Square.B3, Square.L1, Square.U9};
		case BRD:
			return new Square[]{Square.B7, Square.R9, Square.D9};
		case BRU:
			return new Square[]{Square.B1, Square.R3, Square.U7};
		case FLD:
			return new Square[]{Square.F7, Square.L9, Square.D7};
		case FLU:
			return new Square[]{Square.F1, Square.L3, Square.U1};
		case FRD:
			return new Square[]{Square.F9, Square.R7, Square.D3};
		case FRU:
			return new Square[]{Square.F3, Square.R1, Square.U3};
		default:
			break;
		}

		return null;
	}

	private void shapeCornersFromCubelets(CornerCubelet[] cc){
		int[][] corners = {{0, 0}, {0, 2},{2, 0},{2, 2}};
		int[] correctOrder = {0,2, 3, 1};

		for(int i = 0; i < corners.length; i++){
			Square[] m = cornerCubeletNameToSquares(cc[correctOrder[i]].name);
			front.m[0][0] = m[cc[correctOrder[i]].rotation.v];

			m = getUpAndLeftSquare(front.m[0][0]);
			up.m[2][0] = m[0];
			left.m[0][2] = m[1];
			turnFront(true);
		}

		for(int i = 0; i < corners.length; i++){
			Square[] m = cornerCubeletNameToSquares(cc[correctOrder[i] + corners.length].name);
			back.m[0][0] = m[cc[correctOrder[i] + corners.length].rotation.v];

			m = getUpAndLeftSquare(back.m[0][0]);
			up.m[0][2] = m[0];
			right.m[0][2] = m[1];
			turnBack(true);
		}
	}

	private static Square[] getUpAndLeftSquare(Square s){
		Cube c = new Cube();
		if(matrixContains(INIT_FRONT, s)){
			while(c.front.m[0][0] != s){
				c.turnFront(true);
			}
		}else if(matrixContains(INIT_RIGHT, s)){
			while(c.right.m[0][0] != s){
				c.turnRight(true);
			}
			c.turnUp(true);
		}else if(matrixContains(INIT_LEFT, s)){
			while(c.left.m[0][0] != s){
				c.turnLeft(true);
			}
			c.turnUp(false);
		}else if(matrixContains(INIT_UP, s)){
			while(c.up.m[0][0] != s){
				c.turnUp(true);
			}
			c.turnLeft(true);
		}else if(matrixContains(INIT_DOWN, s)){
			while(c.down.m[0][0] != s){
				c.turnDown(true);
			}
			c.turnLeft(false);
		}else if(matrixContains(INIT_BACK, s)){
			while(c.back.m[0][0] != s){
				c.turnBack(true);
			}
			c.turnUp(true);
			c.turnUp(true);
		}

		return new Square[]{c.up.m[2][0], c.left.m[0][2]};
	}

	private void shapeEdgesFromCubelets(EdgeCubelet[] ec){
		int[] correctOrder = {0, 1, 3, 2};

		ArrayList<EdgeCubeletName> middleCubelets = new ArrayList<EdgeCubeletName>();
		Collections.addAll(middleCubelets, new EdgeCubeletName[]{EdgeCubeletName.MLD, EdgeCubeletName.MRD, EdgeCubeletName.MLU, EdgeCubeletName.MRU});


		for(int i = 0; i < 4; i++){
			Square[] m = edgeCubeletNameToSquares(ec[correctOrder[i]].name);

			front.m[0][1] = m[ec[correctOrder[i]].rotation.v];
			up.m[2][1] = m[(ec[correctOrder[i]].rotation.v + 1) % 2];

			turnFront(true);
		}

		for(int i = 0; i < 4; i++){
			Square[] m = edgeCubeletNameToSquares(ec[correctOrder[i] + 4].name);
			back.m[0][1] = m[ec[correctOrder[i] + 4].rotation.v];
			up.m[0][1] = m[(ec[correctOrder[i] + 4].rotation.v + 1) % 2];

			turnBack(true);
		}

		for(int i = 0; i < 2; i++){
			Square[] m = edgeCubeletNameToSquares(ec[i + 8].name);
			left.m[0][1] = m[ec[i + 8].rotation.v];
			up.m[1][0] = m[(ec[i + 8].rotation.v + 1) % 2];

			turnLeft(true);
			turnLeft(true);
		}

		for(int i = 0; i < 2; i++){
			Square[] m = edgeCubeletNameToSquares(ec[i + 10].name);
			right.m[0][1] = m[ec[i + 10].rotation.v];
			up.m[1][2] = m[(ec[i + 10].rotation.v + 1) % 2];

			turnRight(true);
			turnRight(true);
		}
	}

	public void shapeFromCubelets(CornerCubelet[] corners,EdgeCubelet[] edges){
		shapeCornersFromCubelets(corners);
		shapeEdgesFromCubelets(edges);
	}

	
	
	public int getRot0Corners(CornerCubeletName from, CornerCubeletName to){
		if(from == to) return 0;
		
		switch(from){
		case FLU:
			switch(to){
			case FRU: case FLD: case FRD: case BRU: case BLD:
				return 1;
			case BLU: case BRD: 
				return 2;
			}
		case FLD:
			switch(to){
			case FRD: case FLU: case FRU: case BRD: case BLU:
				return 1;
			case BLD: case BRU: 
				return 2;
			}
		case FRD:
			switch(to){
			case FLD: case FRU: case FLU: case BLD: case BRU:
				return 1;
			case BRD: case BLU: 
				return 2;
			}
		case FRU:
			switch(to){
			case FLU: case FRD: case FLD: case BLU: case BRD:
				return 1;
			case BRU: case BLD: 
				return 2;
			}
		}
		
		return 0;
	}

	public int getRot1Corners(CornerCubeletName from, CornerCubeletName to){
		if(from == to) return 2;
		
		switch(from){
		case FLU:
			switch(to){
			case FRU: case BLU:
				return 1;
			case FLD: case FRD: case BRU: case BLD: case BRD:
				return 2;
			}
		case FLD:
			switch(to){
			case FRD: case BLD:
				return 1;
			case FLU: case FRU: case BRD: case BLU: case BRU:
				return 2;
			}
		case FRD:
			switch(to){
			case FLD: case BRD:
				return 1;
			case FRU: case FLU: case BLD: case BRU: case BLU:
				return 2;
			}
		case FRU:
			switch(to){
			case FLU: case BRU:
				return 1;
			case FRD: case FLD: case BLU: case BRD: case BLD:
				return 2;
			}
		}
		
		return 0;
	}

	public int getRot2Corners(CornerCubeletName from, CornerCubeletName to){
		if(from == to) return 2;
		
		switch(from){
		case FLU:
			switch(to){
			case BLU: case FLD:
				return 1;
			case FRU: case FRD: case BRU: case BLD: case BRD:
				return 2;
			}
		case FLD:
			switch(to){
			case BLD: case FLU:
				return 1;
			case FRD: case FRU: case BRD: case BLU: case BRU:
				return 2;
			}
		case FRD:
			switch(to){
			case BRD: case FRU:
				return 1;
			case FLD: case FLU: case BLD: case BLU: case BRU:
				return 2;
			}
		case FRU:
			switch(to){
			case BRU: case FRD:
				return 1;
			case FLD: case FLU: case BLD: case BLU: case BRD:
				return 2;
			}
		}
		
		return 0;
	}


	public int getRot0Edges(EdgeCubeletName from, EdgeCubeletName to){
		if(from == to) return 0;
		
		switch(from){
		case FU:
			switch(to){
			case FL: case FR: case FD: case BU: case MLU: case MRU:
				return 1;
			case BR: case BL: case BD: case MLD: case MRD:
				return 2;
			}
		case FD:
			switch(to){
			case FL: case FR: case FU: case BD: case MLD: case MRD:
				return 1;
			case BR: case BL: case BU: case MLU: case MRU:
				return 2;
			}
		case FL:
			switch(to){
			case FU: case FR: case FD: case BL:
				return 1;
			case BR: case BD: case BU: case MLU: case MRU: case MLD: case MRD:
				return 2;
			}
		case FR:
			switch(to){
			case FU: case FL: case FD: case BR:
				return 1;
			case BL: case BD: case BU: case MRU: case MLU: case MRD: case MLD:
				return 2;
			}
		case BD:
			switch(to){
			case BL: case BR: case BU: case FD: case MLD: case MRD:
				return 1;
			case FR: case FL: case FU: case MLU: case MRU:
				return 2;
			}
		case BL:
			switch(to){
			case BU: case BR: case BD: case FL:
				return 1;
			case FR: case FD: case FU: case MLU: case MRU: case MLD: case MRD:
				return 2;
			}
		case BR:
		case BU:
		
		case MLU:
			switch(to){
			case FU: case BU: case MRU: case MLD:
				return 1;
			case FL: case FR: case FD: case BL: case BD: case BR: case MRD:
				return 2;
			}
		case MRU:
			switch(to){
			case FU: case BU: case MLU: case MRD:
				return 1;
			case FR: case FL: case FD: case BR: case BD: case BL: case MLD:
				return 2;
			}
		case MRD:
			switch(to){
			case FD: case BD: case MLD: case MRU:
				return 1;
			case FR: case FL: case FU: case BR: case BU: case BL: case MLU:
				return 2;
			}
		case MLD:
			switch(to){
			case FD: case BD: case MRD: case MLU:
				return 1;
			case FL: case FR: case FU: case BL: case BU: case BR: case MRU:
				return 2;
			}
		}
		
		return 0;
	}
	
	public int getRot1Edges(EdgeCubeletName from, EdgeCubeletName to){
		if(from == to) return 3;
		
		switch(from){
		case FU:
			switch(to){
			case FL: case FR: case BL: case BR: case MLU: case MRU: case MLD: case MRD:
				return 2;
			case BD: case FD: case BU:
				return 3;
			}
		case FD:
			switch(to){
			case FL: case FR: case BL: case BR: case MLU: case MRU: case MLD: case MRD:
				return 2;
			case BU: case FU: case BD:
				return 3;
			}
		case FL:
			switch(to){
			case FU: case BD: case BU: case FD: case MLU: case MRU: case MLD: case MRD:
				return 2;
			case FR: case BL: case BR:
				return 3;
			}
		case FR:
			switch(to){
			case FU: case BD: case BU: case FD: case MRU: case MLU: case MLD: case MRD:
				return 2;
			case FL: case BL: case BR:
				return 3;
			}
		case BD:
			switch(to){
			case BL: case BR: case FL: case FR: case MLU: case MRU: case MLD: case MRD:
				return 2;
			case FU: case BU: case FD:
				return 3;
			}
		case BL:
			switch(to){
			case FU: case BD: case BU: case FD: case MLU: case MRU: case MLD: case MRD:
				return 2;
			case FR: case FL: case BR:
				return 3;
			}
		case BR:
			
		case BU:
		
		case MLU:
			switch(to){
			case BL: case FL: 
				return 1;
			case FD: case BU: case FU: case BD: case FR: case BR:
				return 2;
			case MRD: case MLD: case MRU:
				return 3;
			}
		case MRU:
			
		case MRD:
			
		case MLD:
			
		}
		
		return 0;
	}
}
