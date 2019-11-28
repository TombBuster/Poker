import java.util.List;

public class Game {
    private Deck deck;
    void freshDeck() {
        Deck newDeck = new Deck();
        newDeck.shuffle();
        this.deck = newDeck;
    }

    List<Card> dealCard() {
        return deck.getCards(1);
    }

    List<Card> dealFlop() {
        //burn for tradition
        deck.getCards(1);
        return deck.getCards(3);
    }

    List<Card> dealTurn() {
        //burn for tradition
        deck.getCards(1);
        return deck.getCards(1);
    }

    List<Card> dealRiver() {
        //burn for tradition
        deck.getCards(1);
        return deck.getCards(1);
    }

    void showCards(List<Card> communityCards) {
        System.out.println("\nThe community cards are: \n");
        for (Card i : communityCards) {
            System.out.print(i.getRank() + " OF ");
            System.out.println(i.getSuit());
        }
    }
}
