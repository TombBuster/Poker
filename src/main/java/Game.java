import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

import static oracle.jrockit.jfr.events.Bits.intValue;

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

    int betStage(List<Player> players, int liveBet) throws InterruptedException {
        int allPlayed = 0;
        int numPlayers = players.size();
        int i = 0;

        while (allPlayed < numPlayers) {
            i = i % numPlayers;
            Player player = players.get(i);
            // check if folded or all in
            if (!player.getFold() && !player.getAllIn()) {
                //display current liveBet
                System.out.println("Hi, Player " + players.get(i).getName());
                System.out.println("The live bet is: " + liveBet);
                System.out.println("Your cards are: ");
                players.get(i).showCards();
                //Scan for options [Check, call, fold, raise]
                Scanner scanner = new Scanner(System.in);
                Thread.sleep(500);
                System.out.println("\nCheck, Raise, Fold?");
                boolean isValidChoice = false;
                String playerChoice = "C";
                String[] choices = {"Check", "Call", "C", "Fold", "F", "Raise", "R"};
                while (!isValidChoice) {
                    System.out.println("Please choose an option: ");
                    playerChoice = scanner.nextLine();
                    // Case insensitive
                    if (!playerChoice.equals("")) {
                        playerChoice = playerChoice.substring(0, 1).toUpperCase() + playerChoice.substring(1).toLowerCase();
                    }
                    for (String choice : choices) {
                        if (playerChoice.equals(choice)) {
                            isValidChoice = true;
                            break;
                        }
                    }
                    switch (playerChoice) {
                        case "C":
                            playerChoice = "Call";
                            break;
                        case "Check":
                            playerChoice = "Call";
                            break;
                        case "F":
                            playerChoice = "Fold";
                            break;
                        case "R":
                            playerChoice = "Raise";
                            break;
                        default:
                            break;
                    }
                }

                //fold logic: take away bet from balance, set boolean to true
                if (playerChoice.equals("Fold")) {
                    player.setFold(true);
                }
                //raise logic: scan for raise, check if bet is greater than liveBet, and greater than balance. TODO: Add option for cancel.
                //Change bet and liveBet to new value. if bet==balance, set allIn boolean to true.
                //set allPlayed = 1
                else if (playerChoice.equals("Raise")) {
                    int inputBet = player.getBet();
                    int balance = player.getBalance();
                    boolean isValidBet = false;
                        while (!isValidBet) {
                            try {
                                System.out.println("Please enter an amount to bet: ");
                                inputBet = Integer.parseInt(scanner.nextLine());
                            } catch (Exception e) {
                                System.out.println("Invalid type. Please enter a number.");
                                continue;
                            }
                        if (inputBet > balance) {
                            System.out.println("You don't have enough money for that bet. Please enter a lower amount.");
                        } else if (inputBet <= 0) {
                            System.out.println("Please enter a positive amount.");
                        } else if (balance == inputBet) {
                            System.out.println("Going all in!");
                            isValidBet = true;
                            player.setAllIn(true);
                        } else if (inputBet <= liveBet) {
                            System.out.println("You need to enter a higher number than the current bet.");
                        } else {
                            isValidBet = true;
                        }
                    }
                    player.setBet(inputBet);
                    liveBet = inputBet;
                    allPlayed = 0;
                }
                //check/call logic: if bet != liveBet, set equal as long as <= balance. if >, set all in boolean to true, and bet to balance.
                else if (playerChoice.equals("Call")) {
                    int bet = player.getBet();
                    int balance = player.getBalance();
                    if (bet != liveBet) {
                        if (liveBet < balance) {
                            player.setBet(liveBet);
                        } else {
                            player.setAllIn(true);
                            player.setBet(balance);
                        }
                    }
                }
            }
            allPlayed++;
            i++;
        }
        return liveBet;
    }

    void betSettle(List<Integer> winners, List<Player> players) {
        List<Integer> winningBets = new ArrayList<>();
        int pot = 0;
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            int bet = player.getBet();
            pot += bet;

            //Round reset
            player.setAllIn(false);
            player.setFold(false);
            player.setBet(0);
            //Settling bets of non-winners
            if (!winners.contains(i)) {
                player.setBalance(player.getBalance() - bet);
                if (player.getBalance() == 0) {
                    System.out.println("Player " + players.get(i).getName() + " is bust!");
                    //TODO: sort out removing players when bust
                }
            } else {
                //Settle bets of winning players
                winningBets.add(bet);
            }
        }

        int sum = winningBets.stream().mapToInt(Integer::intValue).sum();
        for (int i = 0; i < winners.size(); i++) {
            Player player = players.get(winners.get(i));
            double ratio = ((double) winningBets.get(i)) / sum;
            double newBalance = player.getBalance() + ratio * pot - winningBets.get(i);
            player.setBalance(intValue(newBalance));
        }
    }

    List<Player> checkBust(List<Player> players) {
        List<Player> newPlayers = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            if ( players.get(i).getBalance() != 0) {
                newPlayers.add(players.get(i));
            }
        }
        return newPlayers;
    }
}
