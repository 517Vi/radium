import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Engine {
    Renderer r;
    Settings s;
    Player p;
    InputHandler ih;
    boolean running;
    byte[][] map;
    List<Sprite> sprites;

    public Engine(Settings s) {
        this.s = s;
        running = true;
        loadMap();
        r = new Renderer(s, sprites);
        ih = new InputHandler(s);
    }

    void loadMap() { // >0 not walkable (walls etc), <= 0 walkable (air, etc)
        try {
            Scanner s = new Scanner(new File("resources/map.txt"));
            map = new byte[s.nextInt()][s.nextInt()];
            p = new Player(s.nextDouble(), s.nextDouble(), -1, 0, 0, 0.66);

            for (int x = 0; x < map.length; x++)
                for (int y = 0; y < map[x].length; y++)
                    map[x][y] = (byte) s.nextInt();

            sprites = new ArrayList<>();
            while (s.hasNext()) {
                sprites.add(new Sprite(s.nextDouble(), s.nextDouble(), s.nextInt()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
        // System.out.println(deltaT);
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
