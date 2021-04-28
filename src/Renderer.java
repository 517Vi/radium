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

    public void drawFrame() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(2);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        BufferedImage bi = new BufferedImage(s.getScreenWidth(), s.getScreenHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                if (x > bi.getWidth() / 2 && y < bi.getHeight() / 2) {
                    bi.setRGB(x, y, new Color(255, 0, 255).getRGB());
                } else {
                    bi.setRGB(x, y, new Color(0, 0, 0).getRGB());
                }
            }
        }

        g.drawImage(bi, 0, 0, null);
        g.dispose();
        bs.show();

    }
}
