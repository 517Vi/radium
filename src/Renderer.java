import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.*;

import javax.swing.JFrame;

public class Renderer extends JFrame {
    Settings s;
    private final int TEX_SIZE = 64;
    int[][] texture;

    public Renderer(Settings s) {
        super("Radium");
        this.s = s;
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(s.getScreenWidth(), s.getScreenHeight());
        setLocationRelativeTo(null);
        setVisible(true);

        // Generate textures
        texture = new int[8][TEX_SIZE * TEX_SIZE];
        for (int x = 0; x < TEX_SIZE; x++)
            for (int y = 0; y < TEX_SIZE; y++) {
                int xorcolor = (int) Math.pow(x * 256 / TEX_SIZE, y * 256 / TEX_SIZE);
                int xcolor = x * 256 / TEX_SIZE;
                int ycolor = y * 256 / TEX_SIZE;
                int xycolor = y * 128 / TEX_SIZE + x * 128 / TEX_SIZE;
                texture[0][TEX_SIZE * y + x] = 65536 * 254 * ((x != y && x != TEX_SIZE - y) ? 1 : 0);
                texture[1][TEX_SIZE * y + x] = xycolor + 256 * xycolor + 65536 * xycolor;
                texture[2][TEX_SIZE * y + x] = 256 * xycolor + 65536 * xycolor;
                texture[3][TEX_SIZE * y + x] = xorcolor + 256 * xorcolor + 65536 * xorcolor;
                texture[4][TEX_SIZE * y + x] = 256 * xorcolor;
                texture[5][TEX_SIZE * y + x] = 65536 * 192 * ((x % 16 == 0 || y % 16 == 0) ? 1 : 0);
                texture[6][TEX_SIZE * y + x] = 65536 * ycolor;
                texture[7][TEX_SIZE * y + x] = 0;
            }
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

            int texNum = map[mapX][mapY] - 1;

            double wallX;
            if (side == 0)
                wallX = p.getPosY() + perpWallDist * rayDirY;
            else
                wallX = p.getPosX() + perpWallDist * rayDirX;
            wallX -= Math.floor(wallX);

            int texX = (int) (wallX * (double) TEX_SIZE);
            if ((side == 0 && rayDirX > 0) || (side == 1 && rayDirY < 0))
                texX = TEX_SIZE - texX - 1;

            double step = 1.0 * TEX_SIZE / lineHeight;
            double texPos = (drawStart - s.getScreenHeight() / 2 + lineHeight / 2) * step;

            for (int y = drawStart; y < drawEnd; y++) {
                int texY = (int) texPos & (TEX_SIZE - 1);
                texPos += step;
                Color color = new Color(texture[texNum][TEX_SIZE * texY + texX]);
                if (side == 1)
                    color = color.darker();
                bi.setRGB(x, y, color.getRGB());
            }
        }

        g.drawImage(bi, 0, 0, null);
        g.dispose();
        bs.show();

    }
}
