package ca.guibi.squelette;

import javafx.scene.image.Image;

public class Eye extends Monster {
    private double time;

    public Eye(int level) {
        super(level);

        vx = 1.3 * vx;
        vy = 0;
        sprite = new Image("oeil.png");
        time = random.nextDouble(0, 10);
    }

    @Override
    public void update(double deltaTime) {
        time += deltaTime;

        if (time % .75 < .5) {
            x += deltaTime * vx;
        } else {
            x -= deltaTime * vx;
        }
    }
}
