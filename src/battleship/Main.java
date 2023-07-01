package battleship;

public class Main {

    public static void main(String[] args) {
        boolean debug = false;
        for (String arg: args) {
            if ("debug".equals(arg)) {
                debug = true;
                break;
            }
        }
        Game game = new Game(debug);
        game.loop();
    }
}
