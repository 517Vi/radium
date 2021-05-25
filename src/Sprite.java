public class Sprite {
    private double posX;
    private double posY;
    private int sprite;

    public Sprite(double posX, double posY, int sprite) {
        this.setPosX(posX);
        this.setPosY(posY);
        this.sprite = sprite;
    }

    public int getSprite() {
        return sprite;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

}
