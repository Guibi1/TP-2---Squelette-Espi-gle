package ca.guibi.squelette;

import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Window extends Application {
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        showMenu();

        // General
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Squelette Espiègle");
        primaryStage.getIcons().add(new Image("logo.png"));
        primaryStage.show();
    }

    void showMenu() {
        Button buttonPlay = new Button("Jouer !");
        buttonPlay.setOnAction((event) -> showGame());
        Button buttonInfo = new Button("Infos");
        buttonInfo.setOnAction((event) -> showCredits());

        VBox root = new VBox(buttonPlay, buttonInfo);
        root.setBackground(new Background(List.of(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)),
                List.of(new BackgroundImage(new Image("logo.png"), BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER, BackgroundSize.DEFAULT))));
        root.setSpacing(10);
        root.setPadding(new Insets(160, 0, 0, 0));
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root);
        scene.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                System.exit(0);
            }
        });

        if (animationTimer != null) {
            animationTimer.stop();
        }

        stage.setScene(scene);
    }

    void showGame() {
        Game game = new Game();

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        Pane pane = new Pane(canvas);
        pane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(pane);
        scene.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                showMenu();
            } else {
                game.keyPressed(event);
            }
        });
        scene.setOnKeyReleased(game::keyReleased);

        // Animation
        animationTimer = new AnimationTimer() {
            private long lastTime = System.nanoTime();

            @Override
            public void handle(long now) {
                game.update((now - lastTime) * 1e-9);
                lastTime = now;

                // Draw
                canvas.getGraphicsContext2D().clearRect(0, 0, WIDTH, HEIGHT);
                game.draw(canvas.getGraphicsContext2D());
            }
        };

        animationTimer.start();
        stage.setScene(scene);
    }

    void showCredits() {
        VBox root = new VBox();
        Scene scene = new Scene(root);
        scene.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                showMenu();
            }
        });

        if (animationTimer != null) {
            animationTimer.stop();
        }
        stage.setScene(scene);
    }

    Stage stage;
    AnimationTimer animationTimer;
}
