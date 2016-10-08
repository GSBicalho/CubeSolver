
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;

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
	@FXML public Button btn_solve;

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

	Cursor paintBucketCursor, eyedropperCursor;

	Alert loadingAlert = null;

	public void initialize() {
		c = new Cube();
		ckb_clockwise.setSelected(true);
		System.out.println(c);
		
		gc = cv_canvas.getGraphicsContext2D();
		
		Image paintBucketImage = new Image("Cursors/paint_bucket.png");
		paintBucketCursor = new ImageCursor(paintBucketImage, paintBucketImage.getWidth()/2, paintBucketImage.getHeight()/2);

		Image eyedropperBucketImage = new Image("Cursors/eyedropper.png");
		eyedropperCursor = new ImageCursor(eyedropperBucketImage, eyedropperBucketImage.getWidth()/2, eyedropperBucketImage.getHeight()/2);

		createLoadingAlert();

		c.draw(gc);
	}
	
	protected void createLoadingAlert(){
		loadingAlert = new Alert(Alert.AlertType.NONE);
		loadingAlert.setHeaderText(null);
		loadingAlert.setTitle("Loading");
		loadingAlert.setContentText(null);

		Label l = new Label("Loading Answer...          ");
		l.setFont(Font.font(14));

		VBox vb = new VBox();
		vb.getChildren().add(l);
		vb.setStyle("-fx-alignment:center");

		ProgressIndicator loadingIndicator = new ProgressIndicator();

		HBox hb = new HBox();
		hb.getChildren().add(loadingIndicator);
		hb.getChildren().add(new Label("          "));
		hb.getChildren().add(vb);

		loadingAlert.getDialogPane().setContent(hb);

		loadingAlert.getButtonTypes().add(0, new ButtonType("Cancel", ButtonData.CANCEL_CLOSE));
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
				c = new Cube(sides);
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

	@FXML protected void btnSolveButton(ActionEvent event){


		Task<String> t = new Task<String>(){
			{
                setOnSucceeded(workerStateEvent -> {
                	tf_command.setText(getValue());
                    loadingAlert.close();
                    c.draw(gc);
                });

                setOnFailed(workerStateEvent -> getException().printStackTrace());
            }

			@Override
			protected String call() throws Exception {
				return solve();
			}};

		Thread loadingThread = new Thread(t, "cube-solver");
        loadingThread.setDaemon(true);
        loadingThread.start();

		loadingAlert.showAndWait();
	}

	private String solve(){
		return Solver.solveCubeSimpleSearch(c, 4);
	}

	private void setPaintingMode(boolean value){
		isPainting = value;

		btn_front.setDisable(value);
		btn_back.setDisable(value);
		btn_left.setDisable(value);
		btn_right.setDisable(value);
		btn_up.setDisable(value);
		btn_down.setDisable(value);
		btn_solve.setDisable(value);
		tf_command.setDisable(value);

		if(!value){
			btn_reset.setText("Reset");
			btn_paint.setText("Paint");

			c.draw(gc);
		}else{
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

	@FXML protected void canvasOnMouseExited(MouseEvent event) {}

	@FXML protected void canvasOnMouseMoved(MouseEvent event) {
		//System.out.println(event.getX() + " " + event.getY());
		if(isPainting){
			Color[][][] sides = {paintFront, paintBack, paintLeft, paintRight, paintUp, paintDown};
			ClickCoord cc = getClickCoord((int)event.getX(), (int)event.getY());

			if(cc != null){
				if(cc.i == 1 && cc.j == 1){
					if(!cv_canvas.getScene().getCursor().equals(eyedropperCursor)){
						cv_canvas.getScene().setCursor(eyedropperCursor);
					}
				}else{
					if(!cv_canvas.getScene().getCursor().equals(paintBucketCursor)){
						cv_canvas.getScene().setCursor(paintBucketCursor);
					}
				}
			}else{
				if(!cv_canvas.getScene().getCursor().equals(Cursor.DEFAULT)){
					cv_canvas.getScene().setCursor(Cursor.DEFAULT);
				}
			}
		}
	}

	@FXML protected void canvasOnMouseClicked(MouseEvent event) {
		if(isPainting){
			ClickCoord cc = getClickCoord((int)event.getX(), (int)event.getY());
			Color[][][] sides = {paintFront, paintBack, paintLeft, paintRight, paintUp, paintDown};

			if(cc != null){
				if(cc.i == 1 && cc.j == 1){
					currentColor = sides[cc.side][1][1];
				}else{
					sides[cc.side][cc.i][cc.j] = currentColor;
				}
			}

			drawPaint();
		}
	}

	private class ClickCoord{
		int side;
		int i, j;

		public ClickCoord(int side, int i, int j){
			this.side = side;
			this.i = i;
			this.j = j;
		}
	}

	protected ClickCoord getClickCoord(int x, int y){

		int side = -1;
		int foundI = -1, foundJ = -1;

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
				
				if(startX < x && x < startX + sizeOfSquare &&
				   startY < y && y < startY + sizeOfSquare){
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
				
				if(startX < x && x < startX + sizeOfSquare &&
				   startY < y && y < startY + sizeOfSquare){
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
				
				if(startX < x && x < startX + sizeOfSquare &&
				   startY < y && y < startY + sizeOfSquare){
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
				
				if(p.contains(new Point2D(x, y))){
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
				
				if(p.contains(new Point2D(x, y))){
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
				
				if(startX < x && x < startX + sizeOfSquare &&
				   startY < y && y < startY + sizeOfSquare){
					side = SIDE_BACK;
					foundI = j;
					foundJ = i;
				}
			}
		}

		if(side >= 0){
			return new ClickCoord(side, foundI, foundJ);
		}
		return null;
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
