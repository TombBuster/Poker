import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class Game {
    List<Object> determineHand(List<Card> cards) {
        List<Object> results = new ArrayList<>();
        List<Object> isFlush = checkFlush(cards);
        List<Object> isStraight = checkStraight(cards);
        List<Object> isOfAKind = ofAKind(cards);
        if (!isFlush.isEmpty() && !isStraight.isEmpty()) {
            results = isStraight;
            results.add(0, "Straight Flush");
        } else if (!isFlush.isEmpty()) {
            results = isFlush;
        } else if (!isStraight.isEmpty()) {
            results = isStraight;
        } else if (!isOfAKind.isEmpty()) {
            results = isOfAKind;
        } else {
            results.add("High Card");
        }

        return results;
    }

    List<Object> checkFlush(List<Card> cards) {
        List<Object> flushResult = new ArrayList<>();
        ArrayList<String> suits = new ArrayList<String>();
        for (Card i : cards) {
            suits.add(i.getSuit().toString());
        }
        int[] flushNum = {Collections.frequency(suits, "SPADES"),
                Collections.frequency(suits, "HEARTS"),
                Collections.frequency(suits, "DIAMONDS"),
                Collections.frequency(suits, "CLUBS")};
        for (int i=0; i<flushNum.length; i++) {
            if (flushNum[i] >= 5) {
                String flushSuit;
                switch(i) {
                    case 0:
                        flushSuit = "SPADES";
                        break;
                    case 1:
                        flushSuit = "HEARTS";
                        break;
                    case 2:
                        flushSuit = "DIAMONDS";
                        break;
                    case 3:
                        flushSuit = "CLUBS";
                        break;
                    default:
                        flushSuit = "null";
                        break;
                }
                List<Integer> flushInts = new ArrayList<>();
                for (int j=0; j<7; j++) {
                    if (suits.get(j).equals(flushSuit)) {
                        flushInts.add(cards.get(j).getRank().getValue());
                   }
               }
                flushInts.sort(Collections.reverseOrder());
                flushInts = flushInts.subList(0,5);
                flushResult.add("Flush");
                flushResult.addAll(flushInts);
                return flushResult;
            }
        }
        //TODO: filter out to just that suit and find high card.
        return flushResult;
    }

    List<Object> checkStraight(List<Card> cards) {
        //TODO: Check low ace straight
        //TODO: Use high card from straight (i[4])
        List<Object> straightResult = new ArrayList<>();
        int[] cardValues = new int[7];
        for (int i = 0; i < cards.size(); i++) {
            cardValues[i] = cards.get(i).getRank().getValue();
        }

        Arrays.sort(cardValues);

        int[][] cardArrays = {Arrays.copyOfRange(cardValues, 2, 7),
                Arrays.copyOfRange(cardValues, 1, 6),
                Arrays.copyOfRange(cardValues, 0, 5)};

        for (int[] i : cardArrays) {
            //check for repeat values
            if (IntStream.of(i).distinct().toArray().length == 5) {
                //check the last and first element are 4 apart
                if (i[4] - i[0] == 4) {
                    straightResult.add("Straight");
                    straightResult.add(i[4]);
                    return straightResult;
                }
            }
        }
        return straightResult;
    }

    List<Object> ofAKind(List<Card> cards) {

        List<Object> ofAKindResult = new ArrayList<>();
        List<Integer> pairCheck = new ArrayList<>();
        List<Integer> threeCheck = new ArrayList<>();
        List<Integer> fourCheck = new ArrayList<>();
        //Grabbing card values
        List<Integer> cardValues = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            cardValues.add(cards.get(i).getRank().getValue());
        }

        //Creating an array of unique values
        int[] uniqueValues = cardValues.stream().mapToInt(Integer::intValue).distinct().toArray();
        //Creating an array of frequencies > 1 of unique values
        for (int uniqueValue : uniqueValues) {
            int freq = Collections.frequency(cardValues, uniqueValue);
            switch (freq) {
                case 2:
                    pairCheck.add(uniqueValue);
                    break;
                case 3:
                    threeCheck.add(uniqueValue);
                    break;
                case 4:
                    fourCheck.add(uniqueValue);
                    break;
                default:
                    break;
            }
        }

        if (fourCheck.size() >= 1) {
            ofAKindResult.add("Four of a kind");
            return ofAKindResult;
        } else if (threeCheck.size() >= 1 && pairCheck.size() >= 1) {
            ofAKindResult.add("Full House");
            return ofAKindResult;
        } else if (threeCheck.size() >= 1) {
            ofAKindResult.add("Three of a kind");
            return ofAKindResult;
        } else if (pairCheck.size() >= 2) {
            ofAKindResult.add("Two Pair");
            return ofAKindResult;
        } else if (pairCheck.size() == 1) {
            ofAKindResult.add("Pair");
            return ofAKindResult;
        }
        return ofAKindResult;
    }
}
