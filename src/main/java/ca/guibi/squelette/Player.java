package ca.guibi.squelette;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class Player extends GameObject {
    public Player() {
        super(300, 344, 96, 48);
        sprite = spriteStable;
    }

    private final int ACCELERATION_MOVE = 1000;
    private final int ACCELERATION_GRAVITY = 1200;

    private final int JUMP_SPEED = 600;
    private final int MAX_SPEED = 300;

    private ArrayList<KeyCode> pressedDirection = new ArrayList<>();
    private int directionMultiplier = 1;

    private int health = 3;

    private Image spriteStable = new Image("squelette/stable.png");
    private Image[] sprites = { new Image("squelette/marche1.png"), new Image("squelette/marche2.png") };
    private boolean flipSprites;

    public void keyPressed(KeyCode code) {
        if (pressedDirection.contains(code)) {
            return;
        }

        if (code == KeyCode.LEFT || code == KeyCode.RIGHT) {
            pressedDirection.add(0, code);
        } else if ((code == KeyCode.UP) && y == Game.FLOOR_HEIGHT - height) {
            vy -= JUMP_SPEED;
        }
    }

    public void keyReleased(KeyCode code) {
        pressedDirection.remove(code);
    }

    public int getHealth() {
        return health;
    }

    @Override
    public void update(double deltaTime) {
        // Speed X
        if (pressedDirection.size() == 0) {
            if (vx != 0) {
                vx -= directionMultiplier * ACCELERATION_MOVE * deltaTime;
                if (vx * directionMultiplier < 0) {
                    vx = 0;
                }
            }
        } else {
            directionMultiplier = pressedDirection.get(0) == KeyCode.RIGHT ? 1 : -1;
            vx += directionMultiplier * ACCELERATION_MOVE * deltaTime;
        }

        if (vx * directionMultiplier > MAX_SPEED) {
            vx = directionMultiplier * MAX_SPEED;
        }

        // Speed Y
        vy += ACCELERATION_GRAVITY * deltaTime;

        // Position
        x += deltaTime * vx;
        y += deltaTime * vy;

        // X CLipping
        if (x + width >= Window.WIDTH) {
            x = Window.WIDTH - width;
            vx = -1 * Math.abs(vx);
        } else if (x <= 0) {
            x = 0;
            vx = Math.abs(vx);
        }

        // Y Clipping
        if (y + height >= Game.FLOOR_HEIGHT) {
            y = Game.FLOOR_HEIGHT - height;
            vy = 0;
        } else if (y <= 0) {
            y = 0;
            vy = -1 * Math.abs(vx);
        }

        // Set sprite
        if (vx == 0) {
            sprite = spriteStable;
        } else {
            double frameRate = 10 * 1e-9;
            int frame = (int) Math.floor(System.nanoTime() * frameRate);
            sprite = sprites[frame % sprites.length];

            flipSprites = vx < 0;
        }

        if (flipSprites) {
            sprite = ImageHelpers.flop(sprite);
        }
    }
}
