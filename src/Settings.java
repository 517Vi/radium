import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Settings {
    private int screenWidth;
    private int screenHeight;
    private Map<String, Integer> keyMap;

    public Settings() {
        keyMap = new HashMap<String, Integer>();
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public void putKey(String key, int value) {
        keyMap.put(key, value);
    }

    public void rebindKey(String key, int value) {
        keyMap.replace(key, value);
    }

    public int getKeyCode(String key) {
        return keyMap.get(key);
    }

    public Collection<Integer> getKeyCodes() {
        return keyMap.values();
    }
}
