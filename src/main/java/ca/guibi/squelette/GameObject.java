package ca.guibi.squelette;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class GameObject {
    protected GameObject() {
        x = 0;
        y = 0;
        height = 0;
        width = 0;
    }

    protected GameObject(double x, double y, double height, double width) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }

    protected double height;
    protected double width;

    protected double x;
    protected double y;

    protected double vx = 0;
    protected double vy = 0;

    protected Image sprite;

    public abstract boolean isDead();

    public abstract void update(double deltaTime);

    public void draw(GraphicsContext graphics) {
        if (sprite != null) {
            graphics.drawImage(sprite, 0, 0, sprite.getWidth(), sprite.getHeight(), x, y, width, height);
        }
    }
}
