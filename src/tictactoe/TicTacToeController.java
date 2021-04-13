package tictactoe;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/*
 * Program Name: TicTacToeController.java
 * Name: Simon Orr
 * Date: January 5th, 2021
 * Description: TicTacToe game in JavaFX with the Extras: manual timer and a score of how many times X and O individually have won
 */

public class TicTacToeController {
    @FXML Button b1;
    @FXML Button b2;
    @FXML Button b3;
    @FXML Button b4;
    @FXML Button b5;
    @FXML Button b6;
    @FXML Button b7;
    @FXML Button b8;
    @FXML Button b9;
    
    @FXML TextArea textX;
    @FXML TextArea textO;
    @FXML TextArea timerText;
    
    @FXML GridPane gameBoard;
    
	public boolean isFirstPlayer = true, gameOver = false, countReached3 = true;
	public int count, countO, turn = 0, choice = 0, replacementCount, replacementCount2 = 0, coCount = 0, coOCount = 0, tempY = -2, tempX = -2, tempOY = -2, tempOX = -2, scoreX = 0, scoreO = 0, oChoice = 0, tempText;
	public Button[][] buttonArr = new Button[3][3]; // array of the tictactoe board useful later
	public int[] coordinate = new int[8], tempCoordinate = new int[10]; 
	public int[] coordinateO = new int[8], tempCoordinateO = new int[10]; // initialize variables for finding a win with the Os
	public String gameMode = ""; 
	public Stage secondaryStage; // initialize a stage for later use
	public long startTime = System.currentTimeMillis(); // initialize a variable for the time it is now to calculate how long the player has been playing tictactoe

	public void buttonClickHandler(ActionEvent evt) {
		// store the button that the user just clicked
		Button clickedButton = (Button) evt.getTarget();
		String buttonLabel = clickedButton.getText();
		// set the entire buttonArray 
		buttonArr[0][0] = b1;
		buttonArr[0][1] = b2;
		buttonArr[0][2] = b3;
		buttonArr[1][0] = b4;
		buttonArr[1][1] = b5;
		buttonArr[1][2] = b6;
		buttonArr[2][0] = b7;
		buttonArr[2][1] = b8;
		buttonArr[2][2] = b9;
		// add a style to the X and O win counters
		textX.getStyleClass().add("textArea"); 
		textO.getStyleClass().add("textArea");
		// set the objects textX and textO to their corresponding scores
		textX.setText(Integer.toString(scoreX));
		textO.setText(Integer.toString(scoreO));
		// set the coordinate array and O coordinate to -1 for calculating which player won
		for(int i = 0; i < 6; i++) {
			coordinate[i] = -1;
			coordinateO[i] = -1;
		}
		// 
		if(!gameOver && (gameMode == "multiplayer" || gameMode == "Easy Singleplayer" || gameMode == "Hard Singleplayer")) {
			if ("".equals(buttonLabel) && isFirstPlayer) {
				clickedButton.setText("X");
				isFirstPlayer = false;
				find3InARow();
			} else if("".equals(buttonLabel) && !isFirstPlayer && gameMode == "multiplayer") {
				clickedButton.setText("O");
				isFirstPlayer = true;
				find3InARow();
			} 
			if(!isFirstPlayer && gameMode != "multiplayer") {
				if(!isFirstPlayer && gameMode == "Easy Singleplayer") {
					int i = Integer.parseInt(Long.toString(Math.round(Math.random()*2)));
					int n = Integer.parseInt(Long.toString(Math.round(Math.random()*2)));
					if(buttonArr[i][n].getText() == "") {
						buttonArr[i][n].setText("O");
						isFirstPlayer = true;
						find3InARow();
					} 
				} else if(!isFirstPlayer && gameMode == "Hard Singleplayer") {
					if(turn == 0) { // if it is the first move for the bot, choose between these moves
						if(buttonArr[1][1].getText() != "X") {
							buttonArr[1][1].setText("O"); // set the center piece to O in the case that it is not X
							isFirstPlayer = true; // give the turn to the player
							find3InARow(); // check if there is a win happening
							choice = 1; // set the choice variable to the first choice on turn 0 
						} else {
							buttonArr[2][2].setText("O"); // if the center piece is taken on move 1, open with a corner piece move.
							isFirstPlayer = true;
							find3InARow();
							choice = 2; // set the choice variable to the second choice on turn 0 
						}
						turn++;
					} else if (turn == 1 && choice == 1) {
						// after using the best opening moves, try to find the best following moves due to what squares are open
						if(!buttonArr[0][1].getText().equals("X")) {
							buttonArr[0][1].setText("O");
							isFirstPlayer = true;
							find3InARow();
						} else if (buttonArr[2][1].getText() == ""){
							buttonArr[2][1].setText("O");
							isFirstPlayer = true;
							find3InARow();
						} else if (buttonArr[0][0].getText() == "") {
							buttonArr[0][0].setText("O");
							isFirstPlayer = true;
							find3InARow();
						} else if (buttonArr[2][0].getText() == "") {
							buttonArr[2][0].setText("O");
							isFirstPlayer = true;
							find3InARow();
						} else if (buttonArr[0][2].getText() == "") {
							buttonArr[0][2].setText("O");
							isFirstPlayer = true;
							find3InARow();
						} else if (buttonArr[2][2].getText() == "") {
							buttonArr[2][2].setText("O");
							isFirstPlayer = true;
							find3InARow();
						}
						turn++;
					} else if (turn == 1 && choice == 2) {
						// figure out what buttons on the side/top/bottom buttons have been marked
						if(buttonArr[1][0].getText().equals("X") && buttonArr[1][2].getText() == "") {
							buttonArr[1][2].setText("O");
						} else if(buttonArr[0][1].getText().equals("X") && buttonArr[2][1].getText() == "") {
							buttonArr[2][1].setText("O");
						} else if(buttonArr[2][1].getText().equals("X") && buttonArr[0][1].getText() == "") {
							buttonArr[0][1].setText("O");
						} else if(buttonArr[1][2].getText().equals("X") && buttonArr[1][0].getText() == "") {
							buttonArr[1][0].setText("O");
						}
						// arguably the best spot on the board is the center spot, figure out if there is a possibility to mark that spot
						if(buttonArr[0][0].getText().equals("")) {
							buttonArr[0][0].setText("O");
							isFirstPlayer = true;
							find3InARow();
						} else if (buttonArr[1][2].getText() == ""){
							buttonArr[1][2].setText("O");
							isFirstPlayer = true;
							find3InARow();
						}
						turn++;
					}	

					if(coordinateO[1] == coordinateO[3]) {
						if(coordinate[0] == 1 && coordinate[2] == 1+1) {
							buttonArr[0+2][coordinate[2]].setText("O");
						}
					}
		}
				// check if the corners were clicked and if they were, make the side/top pieces beside them O for each case (if b1 was clicked check if b4 (the side piece next to it) is able to be taken. If not, take the top piece (the other side piece) if available)
				if(buttonArr[1][1].getText() == "X") {
					if(clickedButton == b1 || clickedButton == b3 || clickedButton == b7 || clickedButton == b9) { // check if any of the corners were clicked
						if(clickedButton == b1 && b4.getText() != "" && oChoice != 2) { 
							b4.setText("O");
							oChoice = 1;
						} else if (b2.getText() == "" && oChoice != 1){
							b2.setText("O");
							oChoice = 2;
						}
						if(clickedButton == b3 && b6.getText() != "" && oChoice != 4) {
							b6.setText("O");
							oChoice = 3;
						} else if(b2.getText() != "" && oChoice != 3) {
							b2.setText("O");
							oChoice = 4;
						}
						if(clickedButton == b7 && b4.getText() != "" && oChoice != 6) {
							b8.setText("O");
							oChoice = 5;
						} else if(b8.getText() != "" && oChoice != 5) {
							b4.setText("O");
							oChoice = 6;
						}
						if(clickedButton == b9 && b8.getText() != "" && oChoice != 8) {
							b6.setText("O");
							oChoice = 7;
						} else if(b6.getText() != "" && oChoice != 7) {
							b8.setText("O");
							oChoice = 8;
						}
					}
					isFirstPlayer = true; // change turns
				}
			}
			}
		}
	
	
	private boolean find3InARow() {
		// Row 1
		// in the for loop below, you go through the entire board, check if there are squares where X is checked. If so, put the x and y value of that square into the coordinate array
			for(int i = 0; i<3; i++) {
				for(int n = 0; n<3; n++) {
					if(buttonArr[i][n].getText() == "X" && count == 0) {
						if(coordinate[0] == -1) {
							coordinate[0] = i;
							coordinate[1] = n;
							count++;
						} else {
							coordinate[6] = i;
							coordinate[7] = n;
						}
					} else if(buttonArr[i][n].getText() == "X" && count == 1) {
						coordinate[2] = i;
						coordinate[3] = n;
						count++;
					} else if(buttonArr[i][n].getText() == "X" && count == 2) {
						if(coordinate[4] == -1) {
							coordinate[4] = i;
							coordinate[5] = n;
						} else {
							coordinate[6] = i;
							coordinate[7] = n;
						}
						count = 0;
						countReached3 = true;
					}
					
				}
			}
			// in these if statements, the computer will check whether or not the X coordinates match up of all of the X values, and then it will check if the Y values match up, and then check if the diagonals for example ((0,0), (1,1), (2,2)) are taken for both possible diagonals
			if((coordinate[0] == coordinate[2] && coordinate[2] == coordinate[4]) ||(coordinate[0] == coordinate[2] && coordinate[2] == coordinate[4]) || (coordinate[1] == coordinate[3] && coordinate[3] == coordinate[5]) || (coordinate[0]+1 == coordinate[2] && coordinate[1]+1 == coordinate[3] && coordinate[2]+1 == coordinate[4] && coordinate[3]+1 == coordinate[5]) || (coordinate[0]-1 == coordinate[2] && coordinate[1] == coordinate[3]-1 && coordinate[2]-1 == coordinate[4] && coordinate[3] == coordinate[5]-1)) {
				if(coordinate[0] != -1 && coordinate[1] != -1 && coordinate[2] != -1 && coordinate[3] != -1 && coordinate[4] != -1 && coordinate[5] != -1) {
					highlightWinningCombo(buttonArr[coordinate[0]][coordinate[1]], buttonArr[coordinate[2]][coordinate[3]], buttonArr[coordinate[4]][coordinate[5]]);
					gameOver = true;
					scoreX += 1;
				}
			} else if (countReached3){ // in the case that the game is not over, check if there are at least 3 x,y values in the coordinate array
				for(int i = 0; i<8; i++) {
					tempCoordinate[i] = coordinate[i]; // put all values of the coordinate array in a temporary array
				}
				if(replacementCount > 1) {
					// since the game is not over, you want to check whether or not there is a possibility of any 3 of the 4 marks are in a winning pattern, so move the 4th and 5th element of the array (the third mark's x and y values) to the front of the array and move the front to the back
					tempCoordinate[4] = coordinate[0];
					tempCoordinate[5] = coordinate[1];
					tempCoordinate[0] = coordinate[4];
					tempCoordinate[1] = coordinate[5];
					for(int i = 0; i<8; i++) {
						coordinate[i] = tempCoordinate[i]; // make the entire coordinate array the temporary array that you just used to manipulate the array
					}
				}
				replacementCount++;
			} 
			if(!gameOver && coordinate[6] != -1 && coordinate[4] != -1 && coordinate[2] != -1 && coordinate[0] != -1) { // check if all the elements of the array have been taken by x and y values of the marks on the tictactoe board
				int b = coordinate[7];
				// in the for loop below, go through all the x and y values of the marks and check if they're equal to the last x and y values of the array and if they're not, set the first x and y values that aren't to them and then break. The purpose of this is to try to go through the entire array and check if there are any possibilities of the marks being in the wrong order in the array but in the right order on the board to win
				for(int i = 1; i < 7; i+=2) {
					if(coordinate[i] != b && coCount != 0) {
						coordinate[i-1] = coordinate[6];
						coordinate[i] = coordinate[7];
						break;
					}
				}
				coCount++;
			}
			for(int j = 1; j < 7; j+=2) { // go through the x values of the coordinate array and look for a winning case for the columns
				if(coordinate[j] != -1 && countReached3) {
					if(tempY == coordinate[j]) {
						tempX = tempY;
						if(tempX == tempY && tempX == coordinate[j]) {
							if(tempY > 0 && tempX > 0 && j > 0 && coordinate[tempY-1] > 0  && coordinate[tempY] > -1 && coordinate[tempX-1] > 0 && turn >= 5) {
								highlightWinningCombo(buttonArr[coordinate[tempY-1]][coordinate[tempY]], buttonArr[coordinate[tempX-1]][coordinate[tempX]], buttonArr[coordinate[j-1]][coordinate[j]]);
								scoreX += 1;
							}
						}
					}
				}
				tempY = j;
			}
			// set the temporary variables to something negative that is not -1 to reset the variables
			tempY = -2; 
			tempX = -2;
			// copy of last portion of code just renamed variables to fit the needs of the O wincase
			// in the for loop below, you go through the entire board, check if there are squares where O is checked. If so, put the x and y value of that square into the coordinate array
			for(int i = 0; i<3; i++) {
				for(int n = 0; n<3; n++) {
					if(buttonArr[i][n].getText() == "O" && countO == 0) {
						if(coordinateO[0] == -1) {
							coordinateO[0] = i;
							coordinateO[1] = n;
							countO++;
						} else {
							coordinateO[6] = i;
							coordinateO[7] = n;
						}
					} else if(buttonArr[i][n].getText() == "O" && countO == 1) {
						coordinateO[2] = i;
						coordinateO[3] = n;
						countO++;
					} else if(buttonArr[i][n].getText() == "O" && countO == 2) {
						if(coordinateO[4] == -1) {
							coordinateO[4] = i;
							coordinateO[5] = n;
						} else {
							coordinateO[6] = i;
							coordinateO[7] = n;
						}
						countO = 0;
						countReached3 = true;
					}
					
				}
			}
			// in these if statements, the computer will check whether or not the x coordinates match up of all of the X values, and then it will check if the y values match up, and then check if the diagonals for example ((0,0), (1,1), (2,2)) are taken for both possible diagonals
			if((coordinateO[0] == coordinateO[2] && coordinateO[2] == coordinateO[4]) ||(coordinateO[0] == coordinateO[2] && coordinateO[2] == coordinateO[4]) || (coordinateO[1] == coordinateO[3] && coordinateO[3] == coordinateO[5]) || (coordinateO[0]+1 == coordinateO[2] && coordinateO[1]+1 == coordinateO[3] && coordinateO[2]+1 == coordinateO[4] && coordinateO[3]+1 == coordinateO[5]) || (coordinateO[0]-1 == coordinateO[2] && coordinateO[1] == coordinateO[3]-1 && coordinateO[2]-1 == coordinateO[4] && coordinateO[3] == coordinateO[5]-1)) {
				if(coordinateO[0] != -1 && coordinateO[1] != -1 && coordinateO[2] != -1 && coordinateO[3] != -1 && coordinateO[4] != -1 &&  coordinateO[5] != -1) {
					highlightWinningCombo(buttonArr[coordinateO[0]][coordinateO[1]], buttonArr[coordinateO[2]][coordinateO[3]], buttonArr[coordinateO[4]][coordinateO[5]]);
					gameOver = true;
					scoreO++;
				}
			} else if (countReached3){ // in the case that the game is not over, check if there are at least 3 x,y values in the coordinate array
				for(int i = 0; i<8; i++) {
					tempCoordinate[i] = coordinateO[i]; // put all values of the coordinate array in a temporary array
				}
				if(replacementCount2 > 1) { 
					// since the game is not over, you want to check whether or not there is a possibility of any 3 of the 4 marks are in a winning pattern, so move the 4th and 5th element of the array (the third mark's x and y values) to the front of the array and move the front to the back
					tempCoordinate[4] = coordinateO[0];
					tempCoordinate[5] = coordinateO[1];
					tempCoordinate[0] = coordinateO[4];
					tempCoordinate[1] = coordinateO[5];
					for(int i = 0; i<8; i++) { 
						coordinateO[i] = tempCoordinate[i]; // make the entire coordinate array the temporary array that you just used to manipulate the array
					}
				}
				replacementCount2++;
			} 
			if(!gameOver && coordinateO[6] != -1 && coordinateO[4] != -1 && coordinateO[2] != -1 && coordinateO[0] != -1) { // check if all the elements of the array have been taken by x and y values of the marks on the tictactoe board
				int b = coordinateO[7];
				// in the for loop below, go through all the x and y values of the marks and check if they're equal to the last x and y values of the array and if they're not, set the first x and y values that aren't to them and then break. The purpose of this is to try to go through the entire array and check if there are any possibilities of the marks being in the wrong order in the array but in the right order on the board to win
				for(int i = 1; i < 7; i+=2) {
					if(coordinateO[i] != b && coCount != 0) {
						coordinateO[i-1] = coordinateO[6];
						coordinateO[i] = coordinateO[7];
						break;
					}
				}
				coCount++;
			}
			for(int j = 1; j < 7; j+=2) { // go through the x values of the coordinate array and look for a winning case for the columns
				if(coordinateO[j] != -1 && countReached3) {
					if(tempOY == coordinateO[j]) {
						tempOX = tempOY;
						if(tempOX == tempOY && tempOX == coordinateO[j]) {
							if(tempOY > 0 && tempOX > 0 && j > 0 && coordinateO[tempOY-1] > 0  && coordinateO[tempOY] > -1 && coordinateO[tempOX-1] > 0 && turn >= 5) {
								highlightWinningCombo(buttonArr[coordinateO[tempOY-1]][coordinateO[tempOY]], buttonArr[coordinateO[tempOX-1]][coordinateO[tempOX]], buttonArr[coordinateO[j-1]][coordinateO[j]]);
							}
						}
					}
				}
				tempOY = j;
			}
			// set the temporary variables to something negative to rest them to be able to use them again
			tempOY = -2;
			tempOX = -2; 
		return true;
	}
	
	private void highlightWinningCombo(Button first, Button second, Button third) {
		// add the winning button class to all the buttons
		first.getStyleClass().add("winning-button");
		second.getStyleClass().add("winning-button");
		third.getStyleClass().add("winning-button");
	}
	
	public void menuClickHandler(ActionEvent evt) {
		MenuItem clickedMenu = (MenuItem) evt.getTarget();
		String menuLabel = clickedMenu.getText();
		
		if ("Play".equals(menuLabel)) {
			ObservableList<javafx.scene.Node> buttons = gameBoard.getChildren(); // put all of the buttons in a list
			buttons.forEach(btn -> { // go through the list
				// reset the styles of the buttons
				btn.getStyleClass().remove("winning-button");
				btn.getStyleClass().add("button");
			});
			b1.setText("");
			b2.setText("");
			b3.setText("");
			b4.setText("");
			b5.setText("");
			b6.setText("");
			b7.setText("");
			b8.setText("");
			b9.setText("");
			gameOver = false;
			gameMode = "";
			count = 0;
			
			isFirstPlayer = true; // new game starts with X
		} else if ("Easy Singleplayer".equals(menuLabel)) {
			gameMode = "Easy Singleplayer";
		} else if("Hard Singleplayer".equals(menuLabel)){
			gameMode = "Hard Singleplayer";
		} else if ("Multiplayer".equals(menuLabel)) {
			gameMode = "multiplayer";
		} else if ("Help".equals(menuLabel)) {
			System.out.println("Click a square!");
		} else if ("How To Play".equals(menuLabel)) {
			openHowToWindow();
		} else if("Quit".equals(menuLabel)) {
			// close the program
			Platform.exit();
			System.exit(0);
		} else if ("Timer".equals(menuLabel)) {
			timer();
		}
			
	}
	public void openHowToWindow() {
		try {
			// load the pop up you created
			
			Pane howTo = (Pane)FXMLLoader.load(getClass().getResource("/tictactoe/PopUpPanel.fxml"));
			// create a new scene
			Scene howToScene = new Scene(howTo,250,250);

			// add css to the new scene		
			howToScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			//create new stage to put scene in
			secondaryStage = new Stage(); // make a new stage
			secondaryStage.setScene(howToScene); // set the scene to the scene made earlier
			secondaryStage.setResizable(false); // make the window non resizable 
			secondaryStage.showAndWait();
			} catch(Exception e) {
					e.printStackTrace();
			}
		}
	public void timer() {
		long elapsedTime = System.currentTimeMillis() - startTime; // get the amount of time the user has been playing the game
		long elapsedSeconds = elapsedTime / 1000; // convert the time in milliseconds to seconds
		long secondsDisplay = elapsedSeconds % 60; // make the time in seconds easy to display
		long elapsedMinutes = elapsedSeconds / 60; // convert the time to minutes -- make it possible to display the minutes along with the seconds
		timerText.setText(Long.toString(elapsedMinutes) + ": " + Long.toString(secondsDisplay));
		timerText.getStyleClass().add("textArea"); // add the textArea style to the timerText object
	}
	public void closeCurrentWindow(final ActionEvent evt) {
	    final Node source = (Node) evt.getSource(); // initialize a node to close the window
	    final Stage stage = (Stage) source.getScene().getWindow(); // store the stage that is being used
	    stage.close(); // close the stored stage
	}


}