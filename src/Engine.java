public class Engine {
    Renderer r;
    Settings s;
    Player p;
    InputHandler ih;
    boolean running;
    byte[][] map;

    public Engine(Settings s) {
        this.s = s;
        running = true;
        loadMap();
        p = new Player(22, 12, -1, 0, 0, 0.66);
        r = new Renderer(s);
        ih = new InputHandler(s);
    }

    void loadMap() { // >0 not walkable (walls etc), <= 0 walkable (air, etc)
        // TODO: Load from txt file
        map = new byte[][] { { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 4, 4, 6, 4, 4, 6, 4, 6, 4, 4, 4, 6, 4 },
                { 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4 },
                { 8, 0, 3, 3, 0, 0, 0, 0, 0, 8, 8, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6 },
                { 8, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6 },
                { 8, 0, 3, 3, 0, 0, 0, 0, 0, 8, 8, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4 },
                { 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 4, 0, 0, 0, 0, 0, 6, 6, 6, 0, 6, 4, 6 },
                { 8, 8, 8, 8, 0, 8, 8, 8, 8, 8, 8, 4, 4, 4, 4, 4, 4, 6, 0, 0, 0, 0, 0, 6 },
                { 7, 7, 7, 7, 0, 7, 7, 7, 7, 0, 8, 0, 8, 0, 8, 0, 8, 4, 0, 4, 0, 6, 0, 6 },
                { 7, 7, 0, 0, 0, 0, 0, 0, 7, 8, 0, 8, 0, 8, 0, 8, 8, 6, 0, 0, 0, 0, 0, 6 },
                { 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 6, 0, 0, 0, 0, 0, 4 },
                { 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 6, 0, 6, 0, 6, 0, 6 },
                { 7, 7, 0, 0, 0, 0, 0, 0, 7, 8, 0, 8, 0, 8, 0, 8, 8, 6, 4, 6, 0, 6, 6, 6 },
                { 7, 7, 7, 7, 0, 7, 7, 7, 7, 8, 8, 4, 0, 6, 8, 4, 8, 3, 3, 3, 0, 3, 3, 3 },
                { 2, 2, 2, 2, 0, 2, 2, 2, 2, 4, 6, 4, 0, 0, 6, 0, 6, 3, 0, 0, 0, 0, 0, 3 },
                { 2, 2, 0, 0, 0, 0, 0, 2, 2, 4, 0, 0, 0, 0, 0, 0, 4, 3, 0, 0, 0, 0, 0, 3 },
                { 2, 0, 0, 0, 0, 0, 0, 0, 2, 4, 0, 0, 0, 0, 0, 0, 4, 3, 0, 0, 0, 0, 0, 3 },
                { 1, 0, 0, 0, 0, 0, 0, 0, 1, 4, 4, 4, 4, 4, 6, 0, 6, 3, 3, 0, 0, 0, 3, 3 },
                { 2, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 1, 2, 2, 2, 6, 6, 0, 0, 5, 0, 5, 0, 5 },
                { 2, 2, 0, 0, 0, 0, 0, 2, 2, 2, 0, 0, 0, 2, 2, 0, 5, 0, 5, 0, 0, 0, 5, 5 },
                { 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 5, 0, 5, 0, 5, 0, 5, 0, 5 },
                { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5 },
                { 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 5, 0, 5, 0, 5, 0, 5, 0, 5 },
                { 2, 2, 0, 0, 0, 0, 0, 2, 2, 2, 0, 0, 0, 2, 2, 0, 5, 0, 5, 0, 0, 0, 5, 5 },
                { 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 5, 5, 5, 5, 5, 5, 5, 5, 5 } };
    }

    public void loop() {
        long lastTime = System.currentTimeMillis();
        while (running) {
            long currentTime = System.currentTimeMillis();
            long deltaT = currentTime - lastTime;
            update(deltaT);
            lastTime = currentTime;
        }
        r.dispose();
    }

    void update(long deltaT) {
        processInput((double) deltaT / 1000.0);
        // TODO: Update enemies
        r.drawFrame(p, map);
        System.out.println(deltaT);
    }

    void processInput(double frameTime) {
        double moveSpeed = frameTime * 5.0;
        double rotSpeed = frameTime * 3.0;

        if (ih.isPressed("exit"))
            running = false;

        if (ih.isPressed("forward")) {
            if (map[(int) (p.getPosX() + p.getDirX() * moveSpeed)][(int) p.getPosY()] <= 0)
                p.setPosX(p.getPosX() + p.getDirX() * moveSpeed);
            if (map[(int) p.getPosX()][(int) (p.getPosY() + p.getDirY() * moveSpeed)] <= 0)
                p.setPosY(p.getPosY() + p.getDirY() * moveSpeed);
        }
        if (ih.isPressed("backward")) {
            if (map[(int) (p.getPosX() - p.getDirX() * moveSpeed)][(int) p.getPosY()] <= 0)
                p.setPosX(p.getPosX() - p.getDirX() * moveSpeed);
            if (map[(int) p.getPosX()][(int) (p.getPosY() - p.getDirY() * moveSpeed)] <= 0)
                p.setPosY(p.getPosY() - p.getDirY() * moveSpeed);
        }
        if (ih.isPressed("right")) {
            double oldDirX = p.getDirX();
            p.setDirX(p.getDirX() * Math.cos(-rotSpeed) - p.getDirY() * Math.sin(-rotSpeed));
            p.setDirY(oldDirX * Math.sin(-rotSpeed) + p.getDirY() * Math.cos(-rotSpeed));
            double oldPlaneX = p.getPlaneX();
            p.setPlaneX(p.getPlaneX() * Math.cos(-rotSpeed) - p.getPlaneY() * Math.sin(-rotSpeed));
            p.setPlaneY(oldPlaneX * Math.sin(-rotSpeed) + p.getPlaneY() * Math.cos(-rotSpeed));
        }
        if (ih.isPressed("left")) {
            double oldDirX = p.getDirX();
            p.setDirX(p.getDirX() * Math.cos(rotSpeed) - p.getDirY() * Math.sin(rotSpeed));
            p.setDirY(oldDirX * Math.sin(rotSpeed) + p.getDirY() * Math.cos(rotSpeed));
            double oldPlaneX = p.getPlaneX();
            p.setPlaneX(p.getPlaneX() * Math.cos(rotSpeed) - p.getPlaneY() * Math.sin(rotSpeed));
            p.setPlaneY(oldPlaneX * Math.sin(rotSpeed) + p.getPlaneY() * Math.cos(rotSpeed));
        }
    }
}
