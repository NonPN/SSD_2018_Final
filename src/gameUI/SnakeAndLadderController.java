package gameUI;

import java.util.Optional;

import gameLogic.Game;
import gameLogic.Player;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class SnakeAndLadderController {

	@FXML
	Label playerNameLabel;
	@FXML
	Label playerTurnLabel;
	@FXML
	Button rollButton;
	@FXML
	TextField diceOutputNumberText;
	@FXML
	Label playerPosition;
	@FXML
	Label specialBlockLabel;
	@FXML
	ImageView dieImage;
	@FXML
	ImageView player1Image;

	private Game game;
	private Stage stage;
	private AnimationTimer timer;
	private PLayerMovePiece playerMove;

	public void setGame(Game game) {
		this.game = game;
	}

	public void initialize() {
		rollButton.setOnAction(this::onRollButtonClicked);
		playerPosition.setText("Your position: " + 1);
	}

	public void onRollButtonClicked(ActionEvent event) {
		int face = game.currentPlayerRollDie();
		dieImage.setImage(new Image("/res/face" + face + ".png"));
		diceOutputNumberText.setText(face + "");
		Player cur = game.currentPlayer();
		int curPos = game.currentPlayerPosition() + 1;

		for (int i = 0; i < face; i++) {
			if (face - i == 1) {
				System.out.println("5555");
				String status = game.currentPlayerMovePiece(1);
				specialBlockLabel.setText(status);

			} else {
				System.out.println("1111");
				game.currentPlayerOnMovePiece(1);
			}
			if (game.currentPlayer().getName().equals("Player1")) {
				playerMove = new PLayerMovePiece(game, cur, player1Image);
				new Thread(playerMove).start();
			}
		}
		int newPos = game.getPlayerPosition(cur) + 1;
		playerPosition.setText(cur.getName() + " " + curPos + "->" + newPos);
		if (game.isEnded()) {
			gameEndAlert();
		}
	}

	public void gameEndAlert() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog with Custom Actions");
		alert.setHeaderText("Look, a Confirmation Dialog with Custom Actions");
		alert.setContentText("Choose your option.");

		ButtonType buttonTypeOne = new ButtonType("Play again");
		ButtonType buttonTypeThree = new ButtonType("Replay!");
		ButtonType buttonTypeTwo = new ButtonType("Back to Home");

		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeThree, buttonTypeTwo);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeOne) {
			game = new Game(game.getNumPlayer());
			initialize();
		} else if (result.get() == buttonTypeThree) {
			alert.setHeaderText("Not finish!!");
		} else if (result.get() == buttonTypeTwo) {
			backToHome();
		}
	}

	public void backToHome() {

		stage = (Stage) playerNameLabel.getScene().getWindow();
		try {

			FXMLLoader chooseGameLoader = new FXMLLoader(getClass().getResource("GameModeUI.fxml"));
			Parent chooseGameRoot = chooseGameLoader.load();
			Scene chooseGameScene = new Scene(chooseGameRoot);

			stage.setTitle("Choose Game Mode");
			stage.setScene(chooseGameScene);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void playerMove() {
		timer = new AnimationTimer() {

			@Override
			public void handle(long now) {
				player1Image.setTranslateX(player1Image.getTranslateX() + 5);
				player1Image.setY(player1Image.getY());
				System.out.println(player1Image.getTranslateX());
			}
		};
		timer.start();
	}
}
