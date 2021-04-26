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

        g.setColor(Color.black);
        g.fillRect(0, 0, s.getScreenWidth() / 2, s.getScreenHeight() / 2);

        g.dispose();
        bs.show();

    }
}
