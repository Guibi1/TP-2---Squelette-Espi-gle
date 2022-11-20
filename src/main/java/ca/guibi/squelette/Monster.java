package ca.guibi.squelette;

import java.util.Random;

import javafx.scene.image.Image;

public class Monster extends GameObject {
    private final int ACCELERATION_GRAVITY = 100;

    private static Random random = new Random();

    private final double radius;
    private boolean isGoingRight;

    public Monster(int level) {
        isGoingRight = random.nextBoolean();

        radius = random.nextDouble(20, 51);
        height = radius * 2;
        width = radius * 2;
        x = isGoingRight ? -width : Window.WIDTH + width;
        y = random.nextDouble(Window.HEIGHT / 5, Window.HEIGHT / 5 * 4) - (height / 2);

        vy = -random.nextInt(100, 201);
        vx = (100 * Math.pow(level, 0.33) + 200) * (isGoingRight ? 1 : -1);

        sprite = ImageHelpers.colorize(new Image("monstres/" + random.nextInt(8) + ".png"),
                ImageHelpers.couleurAuHasard());
    }

    public boolean isDead() {
        return (isGoingRight && x >= Window.WIDTH) || (!isGoingRight && x + width <= 0);
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public void update(double deltaTime) {
        vy += ACCELERATION_GRAVITY * deltaTime;

        // Position
        x += deltaTime * vx;
        y += deltaTime * vy;
    }
}
