package ca.guibi.squelette;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Magic extends GameObject {
    private final int K = 30;
    private final int Q = 10;

    private final int MAX_SPEED = 50;

    private final int radius = 35;
    private final Color color = ImageHelpers.couleurAuHasard();
    private final Random random = new Random();

    private ArrayList<Particule> movingParticules = new ArrayList<>();
    private ArrayList<Particule> particules = new ArrayList<>();

    Magic(double x, double y) {
        super(x, y, 70, 70);
        vy = -300;

        for (int i = 0; i < 15; i++) {
            Particule p = new Particule(random.nextDouble(-radius / 2, radius / 2),
                    random.nextDouble(-radius / 2, radius / 2));
            particules.add(p);
            movingParticules.add(p);
        }

        final int points = 100;
        for (int i = 0; i < points; i++) {
            double a = 2 * Math.PI / points * i;
            Particule p = new Particule(Math.cos(a) * radius, Math.sin(a) * radius);
            particules.add(p);
        }
    }

    @Override
    public boolean isDead() {
        return (x + height) < -100;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public void update(double deltaTime) {
        for (Particule particule : movingParticules) {
            for (Particule p : particules) {
                if (particule.equals(p)) {
                    continue;
                }

                double x = p.x - particule.x;
                double y = p.y - particule.y;
                double distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
                if (distance < 0.01) {
                    distance = 0.01;
                }
                double force = (K * Q * Q) / Math.pow(distance, 2);

                particule.vx += force * x / distance * deltaTime;
                particule.vy += force * y / distance * deltaTime;
            }

            if (particule.vx > MAX_SPEED) {
                particule.vx = MAX_SPEED;
            } else if (particule.vx < -MAX_SPEED) {
                particule.vx = -MAX_SPEED;
            }

            if (particule.vy > MAX_SPEED) {
                particule.vy = MAX_SPEED;
            } else if (particule.vy < -MAX_SPEED) {
                particule.vy = -MAX_SPEED;
            }

            particule.x += particule.vx * deltaTime;
            particule.y += particule.vy * deltaTime;

            double distance = Math.sqrt(Math.pow(particule.x, 2) + Math.pow(particule.y, 2));
            if (distance > radius) {
                particule.x = 0;
                particule.y = 0;
                particule.vx = 0;
                particule.vy = 0;
            }
        }

        y += vy * deltaTime;
    }

    @Override
    public void draw(GraphicsContext graphics) {
        graphics.setFill(color);
        for (Particule p : movingParticules) {
            graphics.fillOval(x + p.x - p.radius, y + p.y - p.radius, p.radius * 2, p.radius * 2);
        }
    }

    private class Particule {
        double radius = 5;

        double x = 0;
        double y = 0;

        double vx = 0;
        double vy = 0;

        Particule(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
