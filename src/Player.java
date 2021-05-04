public class Player {
    private double posX, posY;
    private double dirX, dirY;
    private double planeX, planeY;

    public Player(double posX, double posY, double dirX, double dirY, double planeX, double planeY) {
        this.setPosX(posX);
        this.setPosY(posY);
        this.setDirX(dirX);
        this.setDirY(dirY);
        this.setPlaneX(planeX);
        this.setPlaneY(planeY);
    }

    public double getPlaneY() {
        return planeY;
    }

    public void setPlaneY(double planeY) {
        this.planeY = planeY;
    }

    public double getPlaneX() {
        return planeX;
    }

    public void setPlaneX(double planeX) {
        this.planeX = planeX;
    }

    public double getDirY() {
        return dirY;
    }

    public void setDirY(double dirY) {
        this.dirY = dirY;
    }

    public double getDirX() {
        return dirX;
    }

    public void setDirX(double dirX) {
        this.dirX = dirX;
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
