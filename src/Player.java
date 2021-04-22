public class Player {
    private double posX, posY;
    private double dirX, dirY;
    private double planeX, planeY;

    public Player(double posX, double posY, double dirX, double dirY, double planeX, double planeY) {
        this.posX = posX;
        this.posY = posY;
        this.dirX = dirX;
        this.dirY = dirY;
        this.planeX = planeX;
        this.planeY = planeY;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public double getDirX() {
        return dirX;
    }

    public double getDirY() {
        return dirY;
    }

    public double getPlaneX() {
        return planeX;
    }

    public double getPlaneY() {
        return planeY;
    }
}
