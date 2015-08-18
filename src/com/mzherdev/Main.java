package com.mzherdev;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;

import java.awt.*;

/**
 * Created by mzherdev on 18.08.2015.
 */
public class Main extends Application {

    private static final int CELL_SIZE = 64;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage myStage) throws Exception{
        myStage.setTitle("Game 2048");

        FlowPane rootNode = new FlowPane();

        myStage.setResizable(false);
        myStage.setOnCloseRequest(event -> Platform.exit());

        Game game = new Game();
        Scene myScene = new Scene(rootNode, game.getWidth(), game.getHeight());
        myStage.setScene(myScene);

        myScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    game.resetGame();
                }

                if (!game.canMove() || (!game.win && !game.canMove())) {
                    game.lose = true;
                }

                if (!game.win && !game.lose) {
                    switch (event.getCode()) {
                        case LEFT:
                            game.left();
                            break;
                        case RIGHT:
                            game.right();
                            break;
                        case DOWN:
                            game.down();
                            break;
                        case UP:
                            game.up();
                            break;
                    }
                }
                game.relocate(330, 390);
            }
        });

        rootNode.getChildren().add(game);
        myStage.show();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                GraphicsContext gc = game.getGraphicsContext2D();
                gc.setFill(Color.rgb(187, 173, 160, 1.0));
                gc.fillRect(0, 0, game.getWidth(), game.getHeight());

                for(int y = 0; y < 4; y++) {
                    for(int x = 0; x < 4; x++){
                        Cell cell = game.getCells()[x + y * 4];
                        int value = cell.number;
                        int xOffset = offsetCoors(x);
                        int yOffset = offsetCoors(y);

                        gc.setFill(cell.getBackground());
                        gc.fillRoundRect(xOffset, yOffset, CELL_SIZE, CELL_SIZE, 14, 14);
                        gc.setFill(cell.getForeground());

                        final int size = value < 100 ? 32 : value < 1000 ? 28 : 24;
                        gc.setFont(Font.font("Verdana", FontWeight.BOLD, size));
                        gc.setTextAlign(TextAlignment.CENTER);


                        String s = String.valueOf(value);

                        if (value != 0)
                            gc.fillText(s, xOffset + CELL_SIZE / 2, yOffset + CELL_SIZE / 2 - 2);
                        if(game.win || game.lose) {
                            gc.setFill(Color.rgb(255, 255, 255));
                            gc.fillRect(0, 0, game.getWidth(), game.getHeight());
                            gc.setFill(Color.rgb(78, 139, 202));
                            gc.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
                            if(game.win){
                                gc.fillText("You win!", 95, 150);
                            }
                            if(game.lose) {
                                gc.fillText("Game over!", 150, 130);
                                gc.fillText("You lose!", 160, 200);
                            }
                            if(game.win || game.lose) {
                                gc.setFont(Font.font("Verdana", FontWeight.LIGHT, 16));
                                gc.setFill(Color.rgb(128, 128, 128));
                                gc.fillText("Press ESC to play again", 110, 270);
                            }
                        }
                        gc.setFont(Font.font("Verdana", FontWeight.LIGHT, 18));
                        gc.fillText("Score: " + game.score, 200, 350);
                    }
                }
            }
        }.start();
    }

    private static int offsetCoors(int arg) {
        return arg * (16 + 64) + 16;
    }

}