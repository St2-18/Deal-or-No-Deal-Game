package com.mycompany.dealornodeal;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class DealOrNoDeal extends Application {
    private Game game;
    private Label infoLabel;
    private VBox leftAmountBox;
    private VBox rightAmountBox;
    private boolean playerBoxChosen = false;
    private int playerBoxIndex = -1;
    private String playerName = "";
    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        askForPlayerName();
        initializeGame();
    }

    private void askForPlayerName() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Player Name");
        dialog.setHeaderText("Enter your name to start the game:");
        dialog.setContentText("Name:");

        Optional<String> result = dialog.showAndWait();
        playerName = result.orElse("").trim();
        if (playerName.isEmpty()) {
            playerName = "Player1";
        }
    }

    private void initializeGame() {
        game = new Game();
        playerBoxChosen = false;
        playerBoxIndex = -1;

        // Create UI components
        HBox prizeBoard = createPrizeBoard();
        GridPane boxGrid = createBoxGrid();

        infoLabel = new Label("Select your personal box to keep for the final decision.");
        infoLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        BorderPane root = new BorderPane();
        root.setCenter(boxGrid);
        root.setLeft(leftAmountBox);
        root.setRight(rightAmountBox);
        root.setBottom(infoLabel);

        Scene scene = new Scene(root, 900, 500);
        primaryStage.setTitle("Deal or No Deal");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createBoxGrid() {
        GridPane grid = new GridPane();
        int totalBoxes = 22;
        int columns = 6;

        for (int i = 0; i < totalBoxes; i++) {
            Button boxButton = new Button("Box " + (i + 1));
            boxButton.setPrefWidth(80);
            boxButton.setPrefHeight(50);
            final int boxIndex = i;

            boxButton.setOnAction(e -> {
                if (!playerBoxChosen) {
                    playerBoxChosen = true;
                    playerBoxIndex = boxIndex;
                    boxButton.setText("Your Box");
                    boxButton.setDisable(true);
                    infoLabel.setText("Personal box selected. Now, open " + game.getBoxesToOpenThisRound() + " boxes for Round 1.");
                    return;
                }
                if (boxIndex == playerBoxIndex) return;
                if (!game.isBoxOpened(boxIndex)) {
                    game.openBox(boxIndex);
                    int prize = game.getPrize(boxIndex);
                    boxButton.setText("$" + prize);
                    boxButton.setDisable(true);
                    updatePrizeBoard(prize);

                    int remaining = game.getBoxesToOpenThisRound() - game.getBoxesOpenedThisRound();
                    if (remaining > 0) {
                        infoLabel.setText("Round " + game.getCurrentRound() +
                                          " - Open " + remaining + " more box" + (remaining > 1 ? "es" : ""));
                    } else {
                        if (game.getCurrentRound() < 4) {
                            showBankOffer();
                        } else if (game.getCurrentRound() == 4) {
                            showBankOffer();
                        } else if (game.getCurrentRound() == 5) {
                            promptFinalChoice();
                        }
                    }
                }
            });

            grid.add(boxButton, i % columns, i / columns);
        }
        grid.setHgap(10);
        grid.setVgap(10);
        return grid;
    }

    private HBox createPrizeBoard() {
        List<Integer> sortedPrizes = game.getSortedPrizes();
        leftAmountBox = new VBox(5);
        rightAmountBox = new VBox(5);
        String boxStyle = "-fx-font-size: 16px; -fx-border-color: black; -fx-border-width: 2px; -fx-padding: 5px;";

        for (int i = 0; i < 11; i++) {
            Label label = new Label("$" + sortedPrizes.get(i));
            label.setStyle(boxStyle);
            leftAmountBox.getChildren().add(label);
        }
        for (int i = 11; i < 22; i++) {
            Label label = new Label("$" + sortedPrizes.get(i));
            label.setStyle(boxStyle);
            rightAmountBox.getChildren().add(label);
        }

        return new HBox(20, leftAmountBox, rightAmountBox);
    }

    private void updatePrizeBoard(int revealedPrize) {
        updateLabelStyle(leftAmountBox, revealedPrize);
        updateLabelStyle(rightAmountBox, revealedPrize);
    }

    private void updateLabelStyle(VBox vbox, int prize) {
        for (javafx.scene.Node node : vbox.getChildren()) {
            if (node instanceof Label) {
                Label label = (Label) node;
                if (label.getText().equals("$" + prize)) {
                    label.setStyle("-fx-font-size: 16px; -fx-text-fill: grey; -fx-border-color: transparent; -fx-background-color: lightgrey;");
                }
            }
        }
    }

    private void showBankOffer() {
        double offer = game.getBankOffer();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Banker's Offer");
        alert.setHeaderText("The Banker offers you: $" + (int) offer);
        alert.setContentText("Do you accept the deal?");

        ButtonType dealButton = new ButtonType("Deal");
        ButtonType noDealButton = new ButtonType("No Deal");
        alert.getButtonTypes().setAll(dealButton, noDealButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == dealButton) {
            endGame((int) offer);
        } else {
            game.nextRound();
            infoLabel.setText("Round " + game.getCurrentRound() +
                              " - Open " + game.getBoxesToOpenThisRound() + " boxes");
        }
    }

    private void promptFinalChoice() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Final Decision");
        alert.setHeaderText("Final Stage: You have 2 boxes remaining.");
        alert.setContentText("Do you want to keep your personal box or swap with the other?");

        ButtonType keepButton = new ButtonType("Keep");
        ButtonType swapButton = new ButtonType("Swap");
        alert.getButtonTypes().setAll(keepButton, swapButton);

        Optional<ButtonType> result = alert.showAndWait();
        int finalPrize = (result.isPresent() && result.get() == swapButton) ? getOtherFinalPrize() : game.getPrize(playerBoxIndex);

        endGame(finalPrize);
    }

    private void endGame(int winningAmount) {
        DatabaseManager.saveGameResult(playerName, winningAmount);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("You won $" + winningAmount);
        alert.setContentText("Do you want to play again?");

        ButtonType playAgain = new ButtonType("Play Again");
        ButtonType quit = new ButtonType("Quit");
        alert.getButtonTypes().setAll(playAgain, quit);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == playAgain) {
            initializeGame();  // Reset game
        } else {
            primaryStage.close();
        }
    }

    private int getOtherFinalPrize() {
        for (int i = 0; i < 22; i++) {
            if (i != playerBoxIndex && !game.isBoxOpened(i)) {
                return game.getPrize(i);
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
