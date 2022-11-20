public class Eye extends Monster {
    public Eye() {
        super();

        sprite = new Image("oeil.png");
    }

    @Override
    public void update(double deltaTime) {
        vy += ACCELERATION_GRAVITY * deltaTime;

        // Position
        x += deltaTime * vx;
        y += deltaTime * vy;
    }
}
