public class Main {
    public static void main(String[] args) {
        Player p1 = new Player("Aang");
        Player p2 = new Player("SpongeBob");
        Player p3 = new Player("Michelangelo");

        Game game = new Game();
        game.registerPlayer(p1);
        game.registerPlayer(p2);
        game.registerPlayer(p3);

        game.run();
    }
}
