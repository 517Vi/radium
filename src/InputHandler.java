import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.*;

public class InputHandler {
    private Map<Integer, Boolean> keysPressed;
    private Settings s;

    public InputHandler(Settings s) {
        this.s = s;

        keysPressed = new HashMap<Integer, Boolean>();
        for (Object keyCode : s.getKeyCodes()) {
            keysPressed.put((Integer) keyCode, false);
        }

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent ke) {
                synchronized (InputHandler.class) {
                    switch (ke.getID()) {
                        case KeyEvent.KEY_PRESSED:
                            System.out.println(ke.getKeyCode());
                            keysPressed.replace(ke.getKeyCode(), false, true);
                            break;

                        case KeyEvent.KEY_RELEASED:
                            keysPressed.replace(ke.getKeyCode(), true, false);
                            break;
                    }
                    return false;
                }
            }
        });
    }

    public boolean isPressed(String key) {
        return keysPressed.get(s.getKeyCode(key));
    }
}
