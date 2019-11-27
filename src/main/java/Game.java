import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class Game {
    List<String> determineWin(List<Card> cards) {
        List<String> results = new ArrayList<>();
        boolean isFlush = checkFlush(cards);
        if (isFlush) {
            results.add("Flush");
        }
        boolean isStraight = checkStraight(cards);
        if (isStraight) {
            results.add("Straight");
        }

        String isOfAKind = ofAKind(cards);
        if (!isOfAKind.equals("null")) {
            results.add(isOfAKind);
        }

        return results;
    }

    boolean checkFlush(List<Card> cards) {
        ArrayList<String> suits = new ArrayList<String>();
        for (Card i : cards) {
            suits.add(i.getSuit().toString());
        }
        int[] flushNum = {Collections.frequency(suits, "SPADES"),
                Collections.frequency(suits, "HEARTS"),
                Collections.frequency(suits, "DIAMONDS"),
                Collections.frequency(suits, "CLUBS")};
        for (int i : flushNum) {
            if (i >= 5) {
                return true;
            }
        }
        //TODO: filter out to just that suit and find high card.
        return false;
    }

    boolean checkStraight(List<Card> cards) {
        //TODO: Check low ace straight
        //TODO: Use high card from straight (i[4])
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
                    System.out.println(Arrays.toString(i));
                    return true;
                }
            }
        }
        return false;
    }

    String ofAKind(List<Card> cards) {

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
            return "Four of a kind";
        } else if(threeCheck.size() >= 1 && pairCheck.size() >= 1) {
            return "Full House";
        } else if(threeCheck.size() >= 1) {
            return "Three Of A Kind";
        } else if(pairCheck.size() >= 2) {
            return "Two Pair";
        } else if(pairCheck.size() == 1) {
            return "Pair";
        }


        return "null";
    }
}
