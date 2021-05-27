public class Radium {
    Engine e;
    Settings s;

    public static void main(String[] args) throws Exception {
        new Radium();
    }

    public Radium() {

        s = new Settings();

        s.setScreenWidth(960);
        s.setScreenHeight(720);

        s.putKey("forward", 87);
        s.putKey("left", 65);
        s.putKey("backward", 83);
        s.putKey("right", 68);
        s.putKey("exit", 27);
        s.putKey("shoot", 32);

        e = new Engine(s);
        e.loop();
    }
}
