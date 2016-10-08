import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
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
		stage.focusedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1)
			{
				scene.setCursor(Cursor.DEFAULT);
			}
		});
		stage.setOnCloseRequest(we -> Platform.exit());

		//Exibe a janela
		stage.show();
		
	}

	public static void main(String[] args) {
		Application.launch(MainWindow.class, args);
	}
}
