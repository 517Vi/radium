public class Sprite {
    private double posX;
    private double posY;
    private int sprite;

    public Sprite(double posX, double posY, int sprite) {
        this.posX = posX;
        this.posY = posY;
        this.sprite = sprite;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public int getSprite() {
        return sprite;
    }
}
