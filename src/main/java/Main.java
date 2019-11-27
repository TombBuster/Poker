import java.util.List;

public class Main {

    public static void main(String args[]) {
        for (int k = 0; k < 6; k++) {


            Deck newDeck = new Deck();
            newDeck.shuffle();
            List<Card> player1Hand = newDeck.getCards(7);
            Game game = new Game();
            List<Object> results = game.determineHand(player1Hand);
            for (Card i : player1Hand) {
                System.out.print(i.getRank() + " OF ");
                System.out.println(i.getSuit());
            }

            System.out.println(results);
        }
    }
}
