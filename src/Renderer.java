import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.*;

import javax.swing.JFrame;

public class Renderer extends JFrame {
    Settings s;

    public Renderer(Settings s) {
        super("Radium");
        this.s = s;
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(s.getScreenWidth(), s.getScreenHeight());
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void drawFrame(Player p, byte[][] map) {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(2);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        BufferedImage bi = new BufferedImage(s.getScreenWidth(), s.getScreenHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++)
                bi.setRGB(x, y, Color.BLACK.getRGB());
            double cameraX = 2 * x / (double) bi.getWidth() - 1;
            double rayDirX = p.getDirX() + p.getPlaneX() * cameraX;
            double rayDirY = p.getDirY() + p.getPlaneY() * cameraX;

            int mapX = (int) p.getPosX();
            int mapY = (int) p.getPosY();

            double sideDistX, sideDistY;

            double deltaDistX = Math.abs(1 / rayDirX);
            double deltaDistY = Math.abs(1 / rayDirY);
            double perpWallDist;

            int stepX, stepY;

            boolean hit = false;
            int side = -1;

            if (rayDirX < 0) {
                stepX = -1;
                sideDistX = (p.getPosX() - mapX) * deltaDistX;
            } else {
                stepX = 1;
                sideDistX = (mapX + 1.0 - p.getPosX()) * deltaDistX;
            }
            if (rayDirY < 0) {
                stepY = -1;
                sideDistY = (p.getPosY() - mapY) * deltaDistY;
            } else {
                stepY = 1;
                sideDistY = (mapY + 1.0 - p.getPosY()) * deltaDistY;
            }

            while (!hit) {
                if (sideDistX < sideDistY) {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                } else {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }
                if (map[mapX][mapY] > 0)
                    hit = true;
            }

            if (side == 0) {
                perpWallDist = (mapX - p.getPosX() + (1 - stepX) / 2) / rayDirX;
            } else {
                perpWallDist = (mapY - p.getPosY() + (1 - stepY) / 2) / rayDirY;
            }

            int lineHeight = (int) (s.getScreenHeight() / perpWallDist);

            int drawStart = -lineHeight / 2 + s.getScreenHeight() / 2;
            if (drawStart < 0)
                drawStart = 0;
            int drawEnd = lineHeight / 2 + s.getScreenHeight() / 2;
            if (drawEnd >= s.getScreenHeight())
                drawEnd = s.getScreenHeight() - 1;

            Color color;
            switch (map[mapX][mapY]) {
                case 1:
                    color = Color.RED;
                    break;
                case 2:
                    color = Color.GREEN;
                    break;
                case 3:
                    color = Color.BLUE;
                    break;
                case 4:
                    color = Color.WHITE;
                    break;
                default:
                    color = Color.YELLOW;
                    break;
            }

            if (side == 1) {
                color = color.darker();
            }

            for (int y = drawStart; y <= drawEnd; y++) {
                bi.setRGB(x, y, color.getRGB());
            }
        }

        g.drawImage(bi, 0, 0, null);
        g.dispose();
        bs.show();

    }
}
