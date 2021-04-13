package tictactoe;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

/*
 * Program Name: Main.java
 * Name: Simon Orr
 * Date: January 5th, 2021
 * Description: TicTacToe game in JavaFX with the Extras: manual timer and a score of how many times X and O individually have won
 */

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("TicTacToe.fxml"));
			Scene scene = new Scene(root,500,670);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
