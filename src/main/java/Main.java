import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String args[]) throws InterruptedException {
        //Initialisation
//        System.out.println("How many people?");
        int numPlayers = 6;
        List<Player> players = new ArrayList();
        Game game = new Game();
        int currentBigBlind = 0;

        //Create players
        for (int i = 0; i < numPlayers; i++) {
//            System.out.println("Hi, Player " + i + ", what's your name?");
            String name = Integer.toString(i);
            Player player = new Player(name);
            players.add(player);
        }

        //new round start
        while (players.size() > 1) {
            numPlayers = players.size();
            currentBigBlind = currentBigBlind % players.size();
            game.freshDeck();
            System.out.println("Player " + currentBigBlind + " has the big blind!");
            int bigBlind = 10;
            players.get(currentBigBlind).setBet(bigBlind);



            //Deal player's hands
            for (int i = 0; i < numPlayers; i++) {
                List<Card> dealtCard = game.dealCard();
                players.get(i).setFirstCard(dealtCard);
            }
            for (int i = 0; i < numPlayers; i++) {
                List<Card> dealtCard = game.dealCard();
                players.get(i).setSecondCard(dealtCard);
            }


            //preflop betting
            int liveBet = game.betStage(players, bigBlind);
            //play flop
            List<Card> communityCards = game.dealFlop();
            game.showCards(communityCards);
            Thread.sleep(1000);
            //flop betting
            liveBet = game.betStage(players, liveBet);
            //play turn
            communityCards.addAll(game.dealTurn());
            game.showCards(communityCards);
            Thread.sleep(1000);
            //turn betting
            liveBet = game.betStage(players, liveBet);
            //play river
            communityCards.addAll(game.dealRiver());
            game.showCards(communityCards);
            Thread.sleep(1000);
            //river betting
            liveBet = game.betStage(players, liveBet);
            //Show community cards
            game.showCards(communityCards);
            Thread.sleep(1000);
            //start showdown!
            List<List> results = new ArrayList<>();
            Showdown showdown = new Showdown();

            for (int i = 0; i < numPlayers; i++) {
                players.get(i).showCards();
                Thread.sleep(1000);
                List<Object> result = players.get(i).getResult(communityCards, showdown);
                System.out.println(result);
                results.add(result);
            }
            //calculates winners from hands
            List<Integer> winners = showdown.determineWin(results);
            List<Integer> noFoldWinners = new ArrayList<>();
            for (int i : winners) {
                if (!players.get(i).getFold()) {
                    noFoldWinners.add(i);
                }
            }

            for (int i : noFoldWinners) {
                System.out.println("Player " + players.get(i).getName() + " Won!");
            }
            for (int i = 0; i < players.size(); i++) {
                System.out.println(i);
                int bet = players.get(i).getBet();
                System.out.println(bet);
            }
            System.out.println("Settling bets...");
            game.betSettle(noFoldWinners, players);
            for (int i = 0; i < players.size(); i++) {
                System.out.println(i);
                int balance = players.get(i).getBalance();
                System.out.println(balance);
            }
            players = game.checkBust(players);
        }
        System.out.println("Player " + players.get(0).getName() + " Won Poker!");
    }
}
