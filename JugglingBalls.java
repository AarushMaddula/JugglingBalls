import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class JugglingBalls {

    private HashMap<Integer, HashMap<Integer, Integer>> hm;

    private int currState;

    private final int MAX_HEIGHT, NUM_BALLS;

    public JugglingBalls(int numBalls, int maxHeight) {

        currState = (int) Math.pow(2, numBalls) - 1;
        MAX_HEIGHT = maxHeight;
        NUM_BALLS = numBalls;

        hm = new HashMap<>();

        int numCombinations = (int) Math.pow(2, maxHeight);

        for (int i = 0; i < numCombinations; i++) {
            if (Integer.bitCount(i) == numBalls) {
                String sub = String.format("%" + maxHeight + "s", Integer.toBinaryString(i >> 1)).replace(' ', '0');

                HashMap<Integer, Integer> subHM = new HashMap<>();

                if (Integer.numberOfTrailingZeros(i) > 0) { //checks if last element is empty
                    subHM.put(0, Integer.parseInt(sub, 2));
                    hm.put(i, subHM);
                    continue;
                }

                for (int j = 0; j < maxHeight; j++) {
                    if (sub.charAt(j) == '0') {
                        String subsub = sub.substring(0, j) + "1" + sub.substring(j + 1);

                        subHM.put(maxHeight - j, Integer.parseInt(subsub, 2));
                    }
                }

                if (!subHM.isEmpty()) {
                    hm.put(i, subHM);
                }
            }
        }
        System.out.println();

    }

    public static void main(String[] args) {

        Scanner nb = new Scanner(System.in);
        System.out.print("Enter the number of Balls: ");
        int numBalls = Integer.parseInt(nb.nextLine());

        Scanner mh = new Scanner(System.in);
        System.out.print("Enter the maximum height: ");
        int maxHeight = Integer.parseInt(mh.nextLine());

        if (numBalls > maxHeight) {
            System.out.println("dawg u like type in stupud numbers. you broke me!");
            return;
        }

        JugglingBalls jb = new JugglingBalls(numBalls, maxHeight);
        jb.findAllCombinations();
        System.out.println(jb.isPossibleCombination(4, 2));

        while (true) {
            jb.printCurrState();

            Scanner s = new Scanner(System.in);
            System.out.print("Enter toss: ");


            int toss = Integer.parseInt(s.nextLine());

            if (jb.isValidToss(toss)) {
                jb.toss(toss);
            } else {
                System.out.println("Invalid Toss!");
            }
        }
    }

    public void printCurrState() {
        String currStateStr = String.format("%" + MAX_HEIGHT + "s", Integer.toBinaryString(currState)).replace(' ', '0');

        for (int i = 0; i < currStateStr.length(); i++) {
            char c = currStateStr.charAt(i);

            if (c == '1') {
                System.out.println("⬤");
            } else {
                System.out.println("__");
            }
        }
    }

    public boolean isValidToss(int height) {
        return hm.get(currState).containsKey(height);
    }
    public void toss(int height) {
        currState = hm.get(currState).get(height);
    }

    public boolean isPossibleCombination(int ...tosses) {
        int sum = 0;

        for (int toss : tosses) {
            sum += toss;
        }

        int length = tosses.length;

        return (double) NUM_BALLS == ((double) sum/length);
    }

    ArrayList<ArrayList<Integer>> states = new ArrayList<>();
    ArrayList<ArrayList<Integer>> tosses = new ArrayList<>();

    public void findAllCombinations() {
        ArrayList<Integer> statesVisited = new ArrayList<>();
        ArrayList<Integer> tossesVisited = new ArrayList<>();


        statesVisited.add(currState);
        tossesVisited.add(-1);


        for (Map.Entry<Integer, Integer> entry : hm.get(currState).entrySet()) {
            int finalState = entry.getValue();
            ArrayList<Integer> newStateVisited = new ArrayList<>(statesVisited);
            ArrayList<Integer> newTossVisited = new ArrayList<>(tossesVisited);


            findAllCombinations(finalState, newStateVisited, newTossVisited);
        }

        System.out.println("Finished!");
    }

    public void findAllCombinations(int state, ArrayList<Integer> statesVisited, ArrayList<Integer> tossesVisited) {

        int toss = Math.getExponent(state - (statesVisited.getLast() >> 1)) + 1;

        tossesVisited.add(toss);

        if (state == currState) {
            states.add(statesVisited);
            tosses.add(tossesVisited);
            return;
        }

        if (statesVisited.contains(state)) {
            return;
        }

        statesVisited.add(state);

        for (Map.Entry<Integer, Integer> entry : hm.get(state).entrySet()) {
            int finalState = entry.getValue();
            ArrayList<Integer> newStateVisited = new ArrayList<>(statesVisited);
            ArrayList<Integer> newTossVisited = new ArrayList<>(tossesVisited);

            findAllCombinations(finalState, newStateVisited, newTossVisited);
        }
    }

}
