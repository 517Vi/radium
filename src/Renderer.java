import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.swing.JFrame;
import javax.imageio.ImageIO;

public class Renderer extends JFrame {
    Settings s;
    private final int TEX_SIZE = 64;
    BufferedImage[] textures;
    List<Sprite> sprites;
    double[] zBuffer;
    Object[] spriteOrder;
    double[] spriteDistance;

    public Renderer(Settings s, List<Sprite> sprites) {
        super("Radium");
        this.s = s;
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(s.getScreenWidth(), s.getScreenHeight());
        setLocationRelativeTo(null);
        setVisible(true);

        try {
            textures = new BufferedImage[10];
            textures[0] = ImageIO.read(new File("resources/textures/eagle.png"));
            textures[1] = ImageIO.read(new File("resources/textures/redbrick.png"));
            textures[2] = ImageIO.read(new File("resources/textures/greystone.png"));
            textures[3] = ImageIO.read(new File("resources/textures/mossy.png"));
            textures[4] = ImageIO.read(new File("resources/textures/colorstone.png"));
            textures[5] = ImageIO.read(new File("resources/textures/wood.png"));
            textures[6] = ImageIO.read(new File("resources/sprites/barrel.png"));
            textures[7] = ImageIO.read(new File("resources/sprites/pillar.png"));
            textures[8] = ImageIO.read(new File("resources/sprites/greenlight.png"));
            textures[9] = ImageIO.read(new File("resources/sprites/enemy.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.sprites = sprites;

        spriteOrder = new Integer[sprites.size()];
        spriteDistance = new double[sprites.size()];

        zBuffer = new double[s.getScreenWidth()];
    }

    public void drawFrame(Player p, byte[][] map, InputHandler ih) {
        // Graphics Boilerplate
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(2);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        BufferedImage bi = new BufferedImage(s.getScreenWidth(), s.getScreenHeight(), BufferedImage.TYPE_INT_ARGB);

        // Draw floor / ceiling
        for (int y = 0; y < s.getScreenHeight(); y++) {
            double rayDirX0 = p.getDirX() - p.getPlaneX();
            double rayDirY0 = p.getDirY() - p.getPlaneY();
            double rayDirX1 = p.getDirX() + p.getPlaneX();
            double rayDirY1 = p.getDirY() + p.getPlaneY();

            int yPos = y - s.getScreenHeight() / 2;
            double zPos = 0.5 * s.getScreenHeight();
            double rowDistance = zPos / yPos;

            double floorStepX = rowDistance * (rayDirX1 - rayDirX0) / s.getScreenWidth();
            double floorStepY = rowDistance * (rayDirY1 - rayDirY0) / s.getScreenWidth();
            double floorX = p.getPosX() + rowDistance * rayDirX0;
            double floorY = p.getPosY() + rowDistance * rayDirY0;

            for (int x = 0; x < s.getScreenWidth(); ++x) {
                int cellX = (int) (floorX);
                int cellY = (int) (floorY);

                int tx = (int) (TEX_SIZE * (floorX - cellX)) & (TEX_SIZE - 1);
                int ty = (int) (TEX_SIZE * (floorY - cellY)) & (TEX_SIZE - 1);

                floorX += floorStepX;
                floorY += floorStepY;

                int floorTexture = 5;
                Color color;

                color = new Color(textures[floorTexture].getRGB(tx, ty));
                color = color.darker();
                color = color.darker();
                bi.setRGB(x, y, color.getRGB());
            }
        }

        // Draw walls
        for (int x = 0; x < bi.getWidth(); x++) {
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
                Color color = new Color(textures[texNum].getRGB(texX, texY));
                if (side == 1)
                    color = color.darker();
                bi.setRGB(x, y, color.getRGB());
            }
            zBuffer[x] = perpWallDist;
        }

        for (int i = 0; i < sprites.size(); i++) {
            spriteOrder[i] = i;
            spriteDistance[i] = (Math.pow(p.getPosX() - sprites.get(i).getPosX(), 2)
                    + Math.pow(p.getPosY() - sprites.get(i).getPosY(), 2));
        }

        {
            final List<Object> spriteOrderCopy = Arrays.asList(spriteOrder);
            List<Object> sortedList = new ArrayList<Object>(spriteOrderCopy);
            Collections.sort(sortedList, Comparator.comparing(s -> spriteDistance[(int) spriteOrderCopy.indexOf(s)]));
            Collections.reverse(sortedList);
            spriteOrder = sortedList.toArray();

        }

        try {

            for (int i = 0; i < sprites.size(); i++) {
                double spriteX = sprites.get((int) spriteOrder[i]).getPosX() - p.getPosX();
                double spriteY = sprites.get((int) spriteOrder[i]).getPosY() - p.getPosY();

                double invDet = 1.0 / (p.getPlaneX() * p.getDirY() - p.getDirX() * p.getPlaneY());
                double transformX = invDet * (p.getDirY() * spriteX - p.getDirX() * spriteY);
                double transformY = invDet * (-p.getPlaneY() * spriteX + p.getPlaneX() * spriteY);
                int spriteScreenX = (int) ((s.getScreenWidth() / 2) * (1 + transformX / transformY));

                int spriteHeight = Math.abs((int) (s.getScreenHeight() / transformY));
                int drawStartY = -spriteHeight / 2 + s.getScreenHeight() / 2;
                if (drawStartY < 0)
                    drawStartY = 0;
                int drawEndY = spriteHeight / 2 + s.getScreenHeight() / 2;
                if (drawEndY >= s.getScreenHeight())
                    drawEndY = s.getScreenHeight() - 1;

                int spriteWidth = Math.abs((int) (s.getScreenHeight() / (transformY)));
                int drawStartX = -spriteWidth / 2 + spriteScreenX;
                if (drawStartX < 0)
                    drawStartX = 0;
                int drawEndX = spriteWidth / 2 + spriteScreenX;
                if (drawEndX >= s.getScreenWidth())
                    drawEndX = s.getScreenWidth() - 1;

                for (int stripe = drawStartX; stripe < drawEndX; stripe++) {
                    int texX = (int) (256 * (stripe - (-spriteWidth / 2 + spriteScreenX)) * TEX_SIZE / spriteWidth)
                            / 256;
                    if (transformY > 0 && stripe > 0 && stripe < s.getScreenWidth() && transformY < zBuffer[stripe])
                        for (int y = drawStartY; y < drawEndY; y++) {
                            int d = (y) * 256 - s.getScreenHeight() * 128 + spriteHeight * 128;
                            int texY = ((d / spriteHeight) * TEX_SIZE) / 256;
                            int color = textures[sprites.get((int) spriteOrder[i]).getSprite()].getRGB(texX, texY);
                            if ((color & 0x00FFFFFF) != 0)
                                bi.setRGB(stripe, y, color);
                            if (sprites.get((int) spriteOrder[i]).getSprite() > 8) { // if enemy is drawn
                                if (stripe == s.getScreenWidth() / 2 && y == s.getScreenHeight() / 2)
                                    if (ih.isPressed("shoot")) {
                                        sprites.remove((int) spriteOrder[i]);
                                    }
                                Sprite sprite = sprites.get((int) spriteOrder[i]);
                                sprite.setPosX(sprite.getPosX() + (p.getPosX() - sprite.getPosX()) / 450000.0);
                                sprite.setPosY(sprite.getPosY() + (p.getPosY() - sprite.getPosY()) / 450000.0);
                            }
                        }
                }
            }
        } catch (IndexOutOfBoundsException e) {
        }
        g.drawImage(bi, 0, 0, null);
        g.dispose();
        bs.show();

    }
}
