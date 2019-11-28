import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String args[]) {



            Deck newDeck = new Deck();
            newDeck.shuffle();
            List<List> results = new ArrayList<>();
            List<Card> playerHand = newDeck.getCards(5);
            Game game = new Game();
            List<Card>[] players = new List[6];
        for (int k = 0; k < 6; k++) {
            players[k] = new ArrayList(playerHand);
            List<Card> newCards = newDeck.getCards(2);
            players[k].addAll(newCards);
            List<Object> result = game.determineHand(players[k]);
            System.out.println("\nPlayer " + k + "'s Hand is: \n");
            for (Card i : players[k]) {
                System.out.print(i.getRank() + " OF ");
                System.out.println(i.getSuit());
            }
            System.out.println(result);
            results.add(result);
        }
        List<Integer> winners = game.determineWin(results);
        System.out.println(winners);
    }
}
