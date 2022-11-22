package ca.guibi.squelette;

import javafx.scene.image.Image;

public class Mouth extends Monster {
    private final double AMPLITUDE = 50;
    private double time;
    private double yBase;

    public Mouth(int level) {
        super(level);

        vy = 0;
        yBase = y;
        time = random.nextDouble(0, 10);
        sprite = new Image("bouche.png");
        if (!isGoingRight) {
            sprite = ImageHelpers.flop(sprite);
        }
    }

    @Override
    public void update(double deltaTime) {
        time += deltaTime;

        x += deltaTime * vx;
        y = yBase + AMPLITUDE * Math.sin(5 * time);
    }
}
