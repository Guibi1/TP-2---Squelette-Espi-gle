package ca.guibi.squelette;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Game {
    public static final int FLOOR_HEIGHT = Window.HEIGHT - 45;

    private final Image healthSprite = new Image("squelette.png");

    private double deltaTimeMonster = 0;
    private double deltaTimeSpecialMonster = 0;
    private double lastTimeMagic = 0;

    private Player player = new Player();
    private ArrayList<Magic> magics = new ArrayList<>();
    private ArrayList<Monster> monsters = new ArrayList<>();

    private int health = 3;
    private int level = 1;
    private int score = 0;

    private void createMonster() {
        createMonster(false);
    }

    private void createMonster(boolean special) {
        if (special) {
            System.out.println("special");
        } else {
            monsters.add(new Monster(level));
        }
    }

    private void createMagic() {
        if (System.nanoTime() > lastTimeMagic + (0.6 * 1e9)) {
            magics.add(new Magic(player.x + player.width / 2, player.y));
            lastTimeMagic = System.nanoTime();
        }
    }

    private boolean collision(GameObject o1, double r1, GameObject o2, double r2) {
        return Math.sqrt(Math.pow((o2.x + r2) - (o1.x + r1), 2) + Math.pow((o2.y + r2) - (o1.y + r1), 2)) <= r1 + r2;
    }

    public void update(double deltaTime) {
        if (player.getHealth() == 0) {
            // TODO Show message to play again or to say the level or to say the player's trash
            return;
        }
        deltaTimeMonster += deltaTime;
        if (deltaTimeMonster >= 3) {
            deltaTimeMonster -= 3;
            createMonster();
        }

        if (level >= 2) {
            deltaTimeSpecialMonster += deltaTime;
            if (deltaTimeSpecialMonster >= 5) {
                deltaTimeSpecialMonster -= 5;
                createMonster(true);
            }
        }

        player.update(deltaTime);

        for (int i = 0; i < magics.size(); i++) {
            magics.get(i).update(deltaTime);
            if (magics.get(i).isDead()) {
                magics.remove(i);
                i--;
            }
        }

        for (int i = 0; i < monsters.size(); i++) {
            monsters.get(i).update(deltaTime);
            if (monsters.get(i).isDead()) {
                health -= 1;
                monsters.remove(i);
                i--;
            }
        }

        for (int i = 0; i < magics.size(); i++) {
            for (int j = 0; j < monsters.size(); j++) {
                if (collision(monsters.get(j), monsters.get(j).getRadius(), magics.get(i), magics.get(i).getRadius())) {
                    score += 1;
                    monsters.remove(j);
                    j--;
                }
            }
        }
    }

    public void draw(GraphicsContext context) {
        context.setFill(Color.WHITE);
        context.setFont(new Font("Arial", 30));
        Text text = new Text(String.valueOf(score));
        text.setFont(context.getFont());
        context.getFont();
        context.fillText(String.valueOf(score), (Window.WIDTH - text.getBoundsInLocal().getWidth()) / 2, 50, 100);

        double healthSize = 30;
        double offset = Window.WIDTH / 2 - health * (healthSize / 2);
        for (int i = 0; i < health; i++) {
            context.drawImage(healthSprite, 0, 0, healthSprite.getWidth(), healthSprite.getHeight(),
                    offset + i * healthSize,
                    80, healthSize, healthSize);
        }

        player.draw(context);

        for (Magic magic : magics) {
            magic.draw(context);
        }

        for (Monster monster : monsters) {
            monster.draw(context);
        }
    }

    public void keyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case SPACE:
                createMagic();
                break;
            case UP:
            case LEFT:
            case RIGHT:
                player.keyPressed(event.getCode());
                break;
            case H:
                level += 1;
                break;
            case J:
                score += 1;
                break;
            case K:
                health += 1;
                break;
            case L:

            default:
                break;
        }
    }

    public void keyReleased(KeyEvent event) {
        player.keyReleased(event.getCode());
    }
}
