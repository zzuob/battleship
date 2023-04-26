package battleship;

public class Main {

    public static void main(String[] args) {
        Game game = new Game();
        boolean debug = false;
        for (String arg: args) {
            if ("debug".equals(arg)) {
                debug = true;
                break;
            }
        }
        game.loop(debug);
    }
}
