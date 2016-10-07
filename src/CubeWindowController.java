import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class CubeWindowController {
	@FXML public CheckBox ckb_clockwise;
	@FXML public Canvas cv_canvas;
	@FXML public TextField tf_command;
	
	Cube c;
	GraphicsContext gc;
	
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
		
		c = new Cube(aux);
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
		c.reset();
		System.out.println(c);
		c.draw(gc);
	}
	
	@FXML protected void actionEvent_Command(ActionEvent event) {
		c.executeCommands(tf_command.getText());
		tf_command.setText("");
		System.out.println(c);
		c.draw(gc);
	}
}
