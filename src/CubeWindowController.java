import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class CubeWindowController {
	@FXML public CheckBox ckb_clockwise;
	@FXML public Canvas cv_canvas;
	@FXML public TextField tf_command;
	
	@FXML public Button btn_front;
	@FXML public Button btn_back;
	@FXML public Button btn_left;
	@FXML public Button btn_right;
	@FXML public Button btn_up;
	@FXML public Button btn_down;
	
	@FXML public Button btn_reset;
	@FXML public Button btn_paint;
	
	Cube c;
	GraphicsContext gc;
	boolean isPainting = false;
	
	public Color[][] paintFront;
	public Color[][] paintBack;
	public Color[][] paintLeft;
	public Color[][] paintRight;
	public Color[][] paintUp;
	public Color[][] paintDown;
	

	int hOffset = 10;
	int vOffset = 10;
	int hPadding = 4;
	int vPadding = 4;
	
	int hDiag = 21;
	int vDiag = 21;
	
	int sizeOfSquare = 35;
	
	Color currentColor = Cube.FRONT_COLOR;
	
	public void initialize() {
		Color[][][] aux = {
				{
					{Cube.FRONT_COLOR, Cube.LEFT_COLOR, Cube.FRONT_COLOR},
					{Cube.DOWN_COLOR, Cube.LEFT_COLOR, Cube.FRONT_COLOR},
					{Cube.FRONT_COLOR, Cube.LEFT_COLOR, Cube.RIGHT_COLOR}
				},
				{
					{Cube.LEFT_COLOR, Cube.BACK_COLOR, Cube.BACK_COLOR},
					{Cube.RIGHT_COLOR, Cube.FRONT_COLOR, Cube.FRONT_COLOR},
					{Cube.BACK_COLOR, Cube.RIGHT_COLOR, Cube.UP_COLOR}
				},
				{
					{Cube.DOWN_COLOR, Cube.DOWN_COLOR, Cube.FRONT_COLOR},
					{Cube.BACK_COLOR, Cube.DOWN_COLOR, Cube.UP_COLOR},
					{Cube.RIGHT_COLOR, Cube.LEFT_COLOR, Cube.BACK_COLOR}
				},
				{
					{Cube.UP_COLOR, Cube.BACK_COLOR, Cube.BACK_COLOR},
					{Cube.FRONT_COLOR, Cube.UP_COLOR, Cube.FRONT_COLOR},
					{Cube.DOWN_COLOR, Cube.RIGHT_COLOR, Cube.RIGHT_COLOR}
				},
				{
					{Cube.UP_COLOR, Cube.UP_COLOR, Cube.DOWN_COLOR},
					{Cube.DOWN_COLOR, Cube.RIGHT_COLOR, Cube.UP_COLOR},
					{Cube.RIGHT_COLOR, Cube.LEFT_COLOR, Cube.UP_COLOR}
				},
				{
					{Cube.LEFT_COLOR, Cube.UP_COLOR, Cube.LEFT_COLOR},
					{Cube.RIGHT_COLOR, Cube.BACK_COLOR, Cube.BACK_COLOR},
					{Cube.LEFT_COLOR, Cube.DOWN_COLOR, Cube.DOWN_COLOR}
				}
			};
		
		try {
			c = new Cube(aux);
		} catch (Cube.InvalidCubeException e) {
			e.printStackTrace();
		}
		//c = new Cube();
		ckb_clockwise.setSelected(true);
		System.out.println(c);
		
		gc = cv_canvas.getGraphicsContext2D();
		
		c.draw(gc);
	}
	
	@FXML protected void btnUpButton(ActionEvent event) {
		c.turnUp(ckb_clockwise.isSelected());
		System.out.println(c);
		c.draw(gc);
	}
	
	@FXML protected void btnDownButton(ActionEvent event) {
		c.turnDown(ckb_clockwise.isSelected());
		System.out.println(c);
		c.draw(gc);
	}
	
	@FXML protected void btnLeftButton(ActionEvent event) {
		c.turnLeft(ckb_clockwise.isSelected());
		System.out.println(c);
		c.draw(gc);
	}
	
	@FXML protected void btnRightButton(ActionEvent event) {
		c.turnRight(ckb_clockwise.isSelected());
		System.out.println(c);
		c.draw(gc);
	}
	
	@FXML protected void btnFrontButton(ActionEvent event) {
		c.turnFront(ckb_clockwise.isSelected());
		System.out.println(c);
		c.draw(gc);
	}
	
	@FXML protected void btnBackButton(ActionEvent event) {
		c.turnBack(ckb_clockwise.isSelected());
		System.out.println(c);
		c.draw(gc);
	}
	
	@FXML protected void btnResetButton(ActionEvent event) {
		if(!isPainting){
			c.reset();
			System.out.println(c);
			c.draw(gc);
		}else{
			try{
				Color[][][] sides = {paintFront, paintBack, paintLeft, paintRight, paintUp, paintDown};
				Cube newCube = new Cube(sides);
				c = newCube;
				setPaintingMode(false);
			}catch(Cube.InvalidCubeException e){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Invalid Cube!");
				alert.setContentText("The Cube presented is invalid.\nEither it's faces are impossible or not all of them are painted!");

				alert.showAndWait();
			}
		}
	}
	
	private void setPaintingMode(boolean value){
		if(!value){
			isPainting = false;
			
			btn_front.setDisable(false);
			btn_back.setDisable(false);
			btn_left.setDisable(false);
			btn_right.setDisable(false);
			btn_up.setDisable(false);
			btn_down.setDisable(false);
			tf_command.setDisable(false);
			
			btn_reset.setText("Reset");
			btn_paint.setText("Paint");
			
			c.draw(gc);
		}else{
			isPainting = true;
			
			btn_front.setDisable(true);
			btn_back.setDisable(true);
			btn_left.setDisable(true);
			btn_right.setDisable(true);
			btn_up.setDisable(true);
			btn_down.setDisable(true);
			tf_command.setDisable(true);
			
			btn_reset.setText("Confirm");
			btn_paint.setText("Cancel");
			
			paintFront = createCenterOnlyColorMatrix(Cube.FRONT_COLOR);
			paintBack = createCenterOnlyColorMatrix(Cube.BACK_COLOR);
			paintLeft = createCenterOnlyColorMatrix(Cube.LEFT_COLOR);
			paintRight = createCenterOnlyColorMatrix(Cube.RIGHT_COLOR);
			paintUp = createCenterOnlyColorMatrix(Cube.UP_COLOR);
			paintDown = createCenterOnlyColorMatrix(Cube.DOWN_COLOR);
			
			drawPaint();
		}
	}
	
	@FXML protected void btnPaintButton(ActionEvent event) {
		setPaintingMode(!isPainting);
	}
	
	public Color[][] createCenterOnlyColorMatrix(Color center){
		Color[][] m = new Color[3][3];
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				m[i][j] = Cube.ERROR_COLOR;
			}
		}
		m[1][1] = center;
		return m;
	}
	
	@FXML protected void actionEvent_Command(ActionEvent event) {
		c.executeCommands(tf_command.getText());
		tf_command.setText("");
		System.out.println(c);
		c.draw(gc);
	}
	
	@FXML protected void canvasOnMouseMoved(MouseEvent event) {
		//System.out.println(event.getX() + " " + event.getY());
	}
	
	@FXML protected void canvasOnMouseClicked(MouseEvent event) {
		int side = -1;
		int foundI = -1, foundJ = -1;
		
		Color[][][] sides = {paintFront, paintBack, paintLeft, paintRight, paintUp, paintDown};
		final int SIDE_FRONT = 0;
		final int SIDE_BACK = 1;
		final int SIDE_LEFT = 2;
		final int SIDE_RIGHT = 3;
		final int SIDE_UP = 4;
		final int SIDE_DOWN = 5;
		
		//front side
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				int startX = hPadding + hOffset + sizeOfSquare * (3 + i);
				int startY = vOffset + sizeOfSquare*(4 + j);
				
				if(startX < event.getX() && event.getX() < startX + sizeOfSquare &&
				   startY < event.getY() && event.getY() < startY + sizeOfSquare){
					side = SIDE_FRONT;
					foundI = j;
					foundJ = i;
				}
			}
		}
		
		//left side
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				int startX = hOffset + sizeOfSquare * i;
				int startY = vOffset + sizeOfSquare*(4 + j);
				
				if(startX < event.getX() && event.getX() < startX + sizeOfSquare &&
				   startY < event.getY() && event.getY() < startY + sizeOfSquare){
					side = SIDE_LEFT;
					foundI = j;
					foundJ = i;
				}
			}
		}
		
		//down side
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				int startX = hPadding + hOffset + sizeOfSquare * (3 + i);
				int startY = vPadding + vOffset + sizeOfSquare*(4 + 3 + j);
				
				if(startX < event.getX() && event.getX() < startX + sizeOfSquare &&
				   startY < event.getY() && event.getY() < startY + sizeOfSquare){
					side = SIDE_DOWN;
					foundI = j;
					foundJ = i;
				}
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
				Double[] xAndY = {xs[0], ys[0], xs[1], ys[1], xs[2], ys[2], xs[3], ys[3]};
				Polygon p = new Polygon();
				p.getPoints().addAll(xAndY);
				
				if(p.contains(new Point2D(event.getX(), event.getY()))){
					side = SIDE_UP;
					foundI = i;
					foundJ = j;
				}
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
				Double[] xAndY = {xs[0], ys[0], xs[1], ys[1], xs[2], ys[2], xs[3], ys[3]};
				Polygon p = new Polygon();
				p.getPoints().addAll(xAndY);
				
				if(p.contains(new Point2D(event.getX(), event.getY()))){
					side = SIDE_RIGHT;
					foundI = j;
					foundJ = i;
				}
			}
		}
		
		//back side
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				int startX = hPadding*3 + hOffset + sizeOfSquare * (6 + i) + hDiag * 3;
				int startY = vPadding + vOffset + sizeOfSquare*(2 + j) + 2;
				
				if(startX < event.getX() && event.getX() < startX + sizeOfSquare &&
				   startY < event.getY() && event.getY() < startY + sizeOfSquare){
					side = SIDE_BACK;
					foundI = j;
					foundJ = i;
				}
			}
		}
		
		if(side >= 0){
			if(foundI == 1 && foundJ == 1){
				currentColor = sides[side][1][1];
			}else{
				sides[side][foundI][foundJ] = currentColor;
			}
		}
		
		drawPaint();
	}

	protected void drawPaint(){
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
		
		gc.setFill(Color.BLACK);
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(2f);
		
		//front side
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				gc.setFill(paintFront[j][i]);
				gc.fillRect(hPadding + hOffset + sizeOfSquare * (3 + i), vOffset + sizeOfSquare*(4 + j), sizeOfSquare, sizeOfSquare);
				gc.setStroke(Color.BLACK);
				gc.strokeRect(hPadding + hOffset + sizeOfSquare * (3 + i),vOffset + sizeOfSquare*(4 + j), sizeOfSquare, sizeOfSquare);
			}
		}
		
		//left side
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				gc.setFill(paintLeft[j][i]);
				gc.fillRect(hOffset + sizeOfSquare * i, vOffset + sizeOfSquare*(4 + j), sizeOfSquare, sizeOfSquare);
				gc.setStroke(Color.BLACK);
				gc.strokeRect(hOffset + sizeOfSquare * i, vOffset + sizeOfSquare*(4 + j), sizeOfSquare, sizeOfSquare);
			}
		}
		
		//down side
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				gc.setFill(paintDown[j][i]);
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
				gc.setFill(paintUp[i][j]);
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
				gc.setFill(paintRight[j][i]);
				gc.fillPolygon(xs, ys, 4);
				gc.setStroke(Color.BLACK);
				gc.strokePolygon(xs, ys, 4);
			}
		}
		
		//back side
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				gc.setFill(paintBack[j][i]);
				gc.fillRect(hPadding*3 + hOffset + sizeOfSquare * (6 + i) + hDiag * 3, vPadding + vOffset + sizeOfSquare*(2 + j) + 2, sizeOfSquare, sizeOfSquare);
				gc.setStroke(Color.BLACK);
				gc.strokeRect(hPadding*3 + hOffset + sizeOfSquare * (6 + i) + hDiag * 3,vPadding +  vOffset + sizeOfSquare*(2 + j) + 2, sizeOfSquare, sizeOfSquare);
			}
		}
		
		
		//draw current color square
		gc.setFill(currentColor);
		gc.fillRect(hPadding + hOffset + sizeOfSquare * 7 + 5, vPadding + vOffset + sizeOfSquare*(4 + 3), 60, 60);
		gc.strokeRect(hPadding + hOffset + sizeOfSquare * 7 + 5, vPadding + vOffset + sizeOfSquare*(4 + 3), 60, 60);
		
		gc.setFill(Color.BLACK);
		gc.fillText("Current Color", hPadding + hOffset + sizeOfSquare * 7, vOffset + sizeOfSquare*(4 + 3));
	}
}
