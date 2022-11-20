package ca.guibi.squelette;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Game {
    private static Random random = new Random();
    public static final int FLOOR_HEIGHT = Window.HEIGHT - 45;

    private final Image healthSprite = new Image("squelette.png");

    private double deltaTimeLevelText = 0;
    private double deltaTimeMonster = 0;
    private double deltaTimeSpecialMonster = 0;
    private double lastTimeMagic = 0;

    private Player player = new Player();
    private ArrayList<Magic> magics = new ArrayList<>();
    private ArrayList<Monster> monsters = new ArrayList<>();

    private int level = 1;
    private int score = 0;

    private void createMonster() {
        createMonster(false);
    }

    private void createMonster(boolean special) {
        Monster monster;

        if (!special) {
            monster = new Monster(level);
        } else if (random.nextBoolean()) {
            monster = new Eye(level);
        } else {
            monster = new Mouth(level);
        }

        monsters.add(monster);
    }

    private void createMagic() {
        if (System.nanoTime() > lastTimeMagic + (0.6 * 1e9)) {
            magics.add(new Magic(player.x + player.width / 2, player.y));
            lastTimeMagic = System.nanoTime();
        }
    }

    private void levelUp() {
        level += 1;
        deltaTimeLevelText = 0;
        deltaTimeMonster = 0;
        deltaTimeSpecialMonster = 0;
    }

    private boolean collision(GameObject o1, double r1, GameObject o2, double r2) {
        return Math.sqrt(Math.pow((o2.x + r2) - (o1.x + r1), 2) + Math.pow((o2.y + r2) - (o1.y + r1), 2)) <= r1 + r2;
    }

    private double getTextWidth(String text, Font font) {
        Text t = new Text(text);
        t.setFont(font);
        return t.getBoundsInLocal().getWidth();
    }

    public boolean update(double deltaTime) {
        deltaTimeLevelText += deltaTime;
        if (player.isDead()) {
            return deltaTimeLevelText < 3;
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
                player.loseHealth();
                if (player.isDead()) {
                    deltaTimeLevelText = 0;
                }

                monsters.remove(i);
                i--;
            }
        }

        for (int i = 0; i < magics.size(); i++) {
            for (int j = 0; j < monsters.size(); j++) {
                if (collision(monsters.get(j), monsters.get(j).getRadius(), magics.get(i), magics.get(i).getRadius())) {
                    score += 1;
                    if (score % 5 == 0) {
                        levelUp();
                    }

                    monsters.remove(j);
                    j--;
                }
            }
        }

        return true;
    }

    public void draw(GraphicsContext context) {
        context.setFill(Color.WHITE);
        context.setFont(new Font("Arial", 30));
        context.fillText(String.valueOf(score),
                (Window.WIDTH - getTextWidth(String.valueOf(score), context.getFont())) / 2, 50);

        double healthSize = 30;
        double offset = Window.WIDTH / 2 - player.getHealth() * (healthSize / 2);
        for (int i = 0; i < player.getHealth(); i++) {
            context.drawImage(healthSprite, 0, 0, healthSprite.getWidth(), healthSprite.getHeight(),
                    offset + i * healthSize,
                    80, healthSize, healthSize);
        }

        if (player.isDead()) {
            context.setFill(Color.RED);
            context.setFont(new Font("Arial", 60));
            context.fillText("FIN DE PARTIE", (Window.WIDTH - getTextWidth("FIN DE PARTIE", context.getFont())) / 2,
                    200);
        } else if (deltaTimeLevelText < 3) {
            context.setFill(Color.WHITE);
            context.setFont(new Font("Arial", 30));
            context.fillText("Niveau " + level, (Window.WIDTH - getTextWidth("Niveau " + level, context.getFont())) / 2,
                    200);
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
        if (player.isDead()) {
            return;
        }

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
                levelUp();
                break;
            case J:
                score += 1;
                break;
            case K:
                player.addHealth();
                break;
            case L:
                player.kill();
            default:
                break;
        }
    }

    public void keyReleased(KeyEvent event) {
        player.keyReleased(event.getCode());
    }
}
