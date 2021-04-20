public class Radium {
    Engine e;

    public static void main(String[] args) throws Exception {
        new Radium();
    }

    public Radium() {
        // TODO: menus, settings

        e = new Engine();
        e.loop();
    }
}
