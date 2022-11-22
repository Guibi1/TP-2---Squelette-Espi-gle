package ca.guibi.squelette;

import javafx.scene.image.Image;

public class Eye extends Monster {
    private double time;

    private final Image normal;
    private final Image flipped;

    public Eye(int level) {
        super(level);

        vx = 1.3 * vx;
        vy = 0;
        time = random.nextDouble(0, 10);
        if (isGoingRight) {
            normal = new Image("oeil.png");
            flipped = ImageHelpers.flop(normal);
        } else {
            flipped = new Image("oeil.png");
            normal = ImageHelpers.flop(flipped);
        }
    }

    @Override
    public void update(double deltaTime) {
        time += deltaTime;

        if (time % .75 < .5) {
            sprite = normal;
            x += deltaTime * vx;
        } else {
            sprite = flipped;
            x -= deltaTime * vx;
        }
    }
}
