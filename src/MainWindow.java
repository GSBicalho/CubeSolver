import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class MainWindow extends Application{
	@Override
	public void start(Stage stage) throws Exception {
		//Carrega a janela
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Windows/CubeWindow.fxml"));
		Parent root = (Parent)loader.load();
		CubeWindowController mwc = (CubeWindowController)loader.getController();

		Scene scene = new Scene(root);
		scene.setCursor(Cursor.DEFAULT);
		
		stage.sizeToScene();
		
		stage.setTitle("Cube Solver");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent we) {
				
			}
		});
		
		//Exibe a janela
		stage.show();
		
	}

	public static void main(String[] args) {
		Application.launch(MainWindow.class, args);
	}
}
