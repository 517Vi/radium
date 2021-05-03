public class Radium {
    Engine e;
    Settings s;

    public static void main(String[] args) throws Exception {
        new Radium();
    }

    public Radium() {
        // TODO: menus

        s = new Settings();

        s.setScreenWidth(640);
        s.setScreenHeight(480);

        s.putKey("forward", 87);
        s.putKey("left", 65);
        s.putKey("backward", 83);
        s.putKey("right", 68);
        s.putKey("exit", 27);

        e = new Engine(s);
        e.loop();
    }
}
