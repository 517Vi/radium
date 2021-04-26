public class Radium {
    Engine e;
    Settings s;

    public static void main(String[] args) throws Exception {
        new Radium();
    }

    public Radium() {
        // TODO: menus, settings
        s = new Settings();
        s.setScreenWidth(640);
        s.setScreenHeight(480);

        e = new Engine(s);
        e.loop();
    }
}
