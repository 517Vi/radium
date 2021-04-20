public class Engine {
    Renderer r;
    boolean running;

    public Engine() {
        r = new Renderer();
        running = true;

    }

    public void loop() {
        long lastTime = System.currentTimeMillis();
        while (running) {
            long currentTime = System.currentTimeMillis();
            long deltaT = currentTime - lastTime;
            update(deltaT);
            lastTime = currentTime;
        }
    }

    void update(long deltaT) {
        // TODO: Get user input
        // TODO: Update user with input
        // TODO: Update enemies
        // TODO: Render
        System.out.println(deltaT);
    }
}
