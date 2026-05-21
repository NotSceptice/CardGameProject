public class Main {
    public static void main(String[] args) {
        Player p1 = new Player("Knight");
        Player p2 = new Player("Prince");
        Player p3 = new Player("Guardian");

        Game game = new Game();
        game.registerPlayer(p1);
        game.registerPlayer(p2);
        game.registerPlayer(p3);

        game.run();
    }
}
