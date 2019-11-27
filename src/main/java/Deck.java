import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        Card.Rank[] ranks = Card.Rank.values();
        Card.Suit[] suits = Card.Suit.values();
        for (Card.Rank rank : ranks) {
            for (Card.Suit suit : suits) {
                cards.add(new Card(rank, suit)); // create a new instance of the Card class
            }
        }

    }

    public void shuffle() {
        Collections.shuffle(this.cards);
    }

    public List<Card> getCards(int amount) {
        List<Card> hand = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            hand.add(this.cards.get(0));
            this.cards.remove(0);
        }
        return hand;
    }
}
