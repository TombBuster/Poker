import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
    List<Object> determineHand(List<Card> cards) {
        List<Object> results = new ArrayList<>();
        List<Object> isFlush = checkFlush(cards);
        List<Object> isStraight = checkStraight(cards);
        List<Object> isOfAKind = ofAKind(cards);
        if (!isFlush.isEmpty() && !isStraight.isEmpty()) {
            results = isStraight;
            results.add(0, "Straight Flush");
        } else if (isOfAKind.get(0).equals("Four of a kind")) {
            results = isOfAKind;
        } else if (isOfAKind.get(0).equals("Full House")) {
            results = isOfAKind;
        }
        else if (!isFlush.isEmpty()) {
            results = isFlush;
        } else if (!isStraight.isEmpty()) {
            results = isStraight;
        } else {
            results = isOfAKind;
        }
        return results;
    }

    List<Integer> determineWin(List<List> results) {
        List <Integer> winners = new ArrayList<>();
        List <String> hands = new ArrayList<>();
        //Create list of hands of players
        for (List<Object> result: results) {
            hands.add(result.get(0).toString());
        }
        //Find frequencies of each of the hands
        String[] allHands = {"Straight Flush","Four of a kind","Full House",
                "Flush","Straight","Three of a kind","Two Pair","Pair","High Card"};
        int[] flushNum = new int[allHands.length];
        for (int i = 0; i < allHands.length; i++) {
            flushNum[i] = Collections.frequency(hands, allHands[i]);
        }

//        int[] flushNum = {Collections.frequency(hands, "Straight Flush"),
//                Collections.frequency(hands, "Four of a kind"),
//                Collections.frequency(hands, "Full House"),
//                Collections.frequency(hands, "Flush"),
//                Collections.frequency(hands, "Straight"),
//                Collections.frequency(hands, "Three of a kind"),
//                Collections.frequency(hands, "Two Pair"),
//                Collections.frequency(hands, "Pair"),
//                Collections.frequency(hands, "High Card")};
        String winningHand = "";
        //The winning hand is whichever has a positive value in order of hand ranking
        for (int i = 0; i < flushNum.length; i++) {
            if (flushNum[i] > 0) {
                winningHand = allHands[i];
                break;
            }
        }

        //Generates list of players with winning hand
        for (int i = 0; i < hands.size(); i++) {
            if (hands.get(i).equals(winningHand)) {
                winners.add(i);
            }
        }

        if (winners.size() == 1) {
            return winners;
        } else {
            List <Integer> winnersMulti = new ArrayList<>();
            String finalWinningHand = winningHand;
            results = results.stream().filter(result -> result.get(0).toString().equals(finalWinningHand)).collect(Collectors.toList());
            for (int i = 1; i < 6; i++) {
                Object[] multiArr = winMulti(i, results, winners);
                results = (List<List>) multiArr[0];
                winners = (List<Integer>) multiArr[1];
            }
        }
        return winners;
    }

    Object[] winMulti(int i, List<List> results, List<Integer> winners) {
        List<List> newResults = new ArrayList<>();
        List<Integer> newWinners = new ArrayList<>();
        int[] currentIndex = new int[results.size()];
        for (int j = 0; j < results.size(); j++) {
            currentIndex[j] = (int) results.get(j).get(i);
        }
        int maxNum = getMax(currentIndex);
        for (int j = 0; j < currentIndex.length; j++) {
            if (currentIndex[j] == maxNum) {
                newResults.add(results.get(j)); //instead of removing (shifts indices backwards), add to new list and return that.
                newWinners.add(winners.get(j));
            }
        }
        Object[] multiArr = {newResults, newWinners};
        return multiArr;
    }

    int getMax(int[] arr) {
        int maxNum = 0;
        for (int value : arr) {
            if (value > maxNum) {
                maxNum = value;
            }
        }
        return maxNum;
    }

    List<Object> checkFlush(List<Card> cards) {
        List<Object> flushResult = new ArrayList<>();
        ArrayList<String> suits = new ArrayList<String>();
        //Create list of suits
        for (Card i : cards) {
            suits.add(i.getSuit().toString());
        }

        //Frequency list for suits
        int[] flushNum = {Collections.frequency(suits, "SPADES"),
                Collections.frequency(suits, "HEARTS"),
                Collections.frequency(suits, "DIAMONDS"),
                Collections.frequency(suits, "CLUBS")};

        //Finds the flush suit
        for (int i = 0; i < flushNum.length; i++) {
            if (flushNum[i] >= 5) {
                String flushSuit;
                switch (i) {
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

                //Filters cards so only the flush suit cards remain
                List<Integer> flushInts = new ArrayList<>();
                for (int j = 0; j < 7; j++) {
                    if (suits.get(j).equals(flushSuit)) {
                        flushInts.add(cards.get(j).getRank().getValue());
                    }
                }
                //Sorts and picks out the highest 5 cards of flush
                flushInts.sort(Collections.reverseOrder());
                flushInts = flushInts.subList(0, 5);
                flushResult.add("Flush");
                flushResult.addAll(flushInts);
                return flushResult;
            }
        }
        return flushResult;
    }

    List<Object> checkStraight(List<Card> cards) {
        List<Object> straightResult = new ArrayList<>();
        List<Integer> cardValues = new ArrayList<>();
        for (Card card : cards) {
            cardValues.add(card.getRank().getValue());
        }
        cardValues.sort(Collections.reverseOrder());
        List<List> cardArrays = new ArrayList<>();
        //Splits the cards into groups of 5 for checking straight
        cardArrays.add(cardValues.subList(0, 5));
        cardArrays.add(cardValues.subList(1, 6));
        cardArrays.add(cardValues.subList(2, 7));
        //Low ace straight check
        if (cardValues.get(0) == 14) {
            List<Integer> potLowAce = new ArrayList<>();
            potLowAce.addAll(cardValues.subList(3, 7));
            potLowAce.add(1);
            cardArrays.add(potLowAce);
        }

        //This loop checks if an array of five numbers are all consecutive
        for (List<Integer> i : cardArrays) {
            //check for repeat values
            if (i.stream().distinct().toArray().length == 5) {
                //check the last and first element are 4 apart
                if (i.get(0) - i.get(4) == 4) {
                    straightResult.add("Straight");
                    straightResult.addAll(i);
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
        for (Card card : cards) {
            cardValues.add(card.getRank().getValue());
        }
        cardValues.sort(Collections.reverseOrder());
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
        List<Integer> kickers = new ArrayList<>();
        //Logic for hand determining, flows through in order of hand value
        if (!fourCheck.isEmpty()) {
            ofAKindResult.add("Four of a kind");
            for (int i = 0; i < 4 ; i++) {
                ofAKindResult.add(fourCheck.get(0));
            }
            return ofAKindResult;
        } else if (!threeCheck.isEmpty() && !pairCheck.isEmpty()) {
            ofAKindResult.add("Full House");
            for (int i = 0; i < 3; i++) {
                ofAKindResult.add(threeCheck.get(0));
            }
            if (pairCheck.get(0).equals(threeCheck.get(0))) {
                ofAKindResult.add(pairCheck.get(1));
                ofAKindResult.add(pairCheck.get(1));
            } else {
                ofAKindResult.add(pairCheck.get(0));
                ofAKindResult.add(pairCheck.get(0));
            }
            return ofAKindResult;
        } else if (!threeCheck.isEmpty()) {
            ofAKindResult.add("Three of a kind");
            for (int i = 0; i < 3; i++) {
                ofAKindResult.add(threeCheck.get(0));
            }
            for (int uniqueValue : uniqueValues) {
                if (uniqueValue != threeCheck.get(0)) {
                    kickers.add(uniqueValue);
                }
            }
            ofAKindResult.add(kickers.get(0));
            ofAKindResult.add(kickers.get(1));
            return ofAKindResult;
        } else if (pairCheck.size() >= 2) {
            ofAKindResult.add("Two Pair");
            ofAKindResult.add(pairCheck.get(0));
            ofAKindResult.add(pairCheck.get(0));
            ofAKindResult.add(pairCheck.get(1));
            ofAKindResult.add(pairCheck.get(1));
            for (int uniqueValue : uniqueValues) {
                if (uniqueValue != pairCheck.get(0) || uniqueValue != pairCheck.get(1)) {
                    kickers.add(uniqueValue);
                }
            }
            ofAKindResult.add(kickers.get(0));
            return ofAKindResult;
        } else if (!pairCheck.isEmpty()) {
            ofAKindResult.add("Pair");
            ofAKindResult.add(pairCheck.get(0));
            ofAKindResult.add(pairCheck.get(0));
            for (int uniqueValue : uniqueValues) {
                if (uniqueValue != pairCheck.get(0)) {
                    kickers.add(uniqueValue);
                }
            }
            ofAKindResult.add(kickers.get(0));
            ofAKindResult.add(kickers.get(1));
            ofAKindResult.add(kickers.get(2));
            return ofAKindResult;
        }
        ofAKindResult.add("High Card");
        for (int i = 0; i < 5; i++) {
            ofAKindResult.add(cardValues.get(i));
        }
        return ofAKindResult;
    }
}
