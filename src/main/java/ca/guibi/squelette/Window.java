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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
                if (!game.update((now - lastTime) * 1e-9)) {
                    showMenu();
                }

                // Draw
                canvas.getGraphicsContext2D().clearRect(0, 0, WIDTH, HEIGHT);
                game.draw(canvas.getGraphicsContext2D());

                lastTime = now;
            }
        };

        animationTimer.start();
        stage.setScene(scene);
    }

    void showCredits() {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root);
        scene.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                showMenu();
            }
        });

        int padding = 72;
        root.setPadding(new Insets(padding));
        root.setSpacing(24);

        Text title = new Text("Squelette Espiègle");
        title.setFont(new Font(48));

        HBox person1Box = new HBox();
        person1Box.setAlignment(Pos.CENTER);
        Text person1Text = new Text("Par");
        person1Text.setFont(new Font(24));
        Text person1 = new Text("Stéphenne Laurent");
        person1.setFont(new Font(32));
        person1.setFill(ImageHelpers.couleurAuHasard());
        person1Box.setSpacing(24);
        person1Box.getChildren().addAll(person1Text, person1);

        HBox person2Box = new HBox();
        person2Box.setAlignment(Pos.CENTER);
        Text person2Text = new Text("et");
        person2Text.setFont(new Font(24));
        Text person2 = new Text("Cédric Bélanger-St-Pierre");
        person2.setFont(new Font(32));
        person2.setFill(ImageHelpers.couleurAuHasard());
        person2Box.setSpacing(24);
        person2Box.getChildren().addAll(person2Text, person2);

        Text information = new Text(
                "Travail remis à Nicolas Hurtubise. Graphismes adaptés de https://game-icons.net/. Développé dans le cadre du cours 420-203-RE. Développement de programmes dans un environnement graphique, au Collège de Bois-de-Boulogne");
        information.setWrappingWidth(Window.WIDTH - padding * 2);

        Button back = new Button("Retour");
        back.setOnAction((event) -> {
            showMenu();
        });

        root.getChildren().addAll(title, person1Box, person2Box, information, back);

        if (animationTimer != null) {
            animationTimer.stop();
        }

        stage.setScene(scene);
    }

    Stage stage;
    AnimationTimer animationTimer;
}
