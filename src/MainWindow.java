import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainWindow extends Application{
	@Override
	public void start(Stage stage) throws Exception {
		//Carrega a janela
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Windows/CubeWindow.fxml"));
		Parent root = loader.load();
		CubeWindowController mwc = loader.getController();

		Scene scene = new Scene(root);
		scene.setCursor(Cursor.DEFAULT);

		stage.sizeToScene();
		
		stage.setTitle("Cube Solver");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.focusedProperty().addListener((ov, t, t1) -> {
			scene.setCursor(Cursor.DEFAULT);
		});
		stage.setOnCloseRequest(we -> Platform.exit());

		//Exibe a janela
		stage.show();
		
	}

	public static void main(String[] args) {
		Application.launch(MainWindow.class, args);

//		Cube cube = new Cube();
//		System.out.println(cube.reverseCommands("R F2 R' L B2 R"));
//		cube.executeCommands("F2 R2 F2 L2 D2 B2 D' F2 R2 U' F2 L D R2 U F' U L D' F R2");
//		cube.executeCommands("L R U B F2 L");
//		System.out.println("Solver: " + Solver.solveCubeSimpleSearch(cube, 6));
	}
}
