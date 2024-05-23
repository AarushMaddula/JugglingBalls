import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class JugglingBalls {

    private HashMap<Integer, HashMap<Integer, Integer>> hm;

    private int currState;

    private final int MAX_HEIGHT, NUM_BALLS;

    public JugglingBalls(int numBalls, int maxHeight) {

        MAX_HEIGHT = maxHeight;
        NUM_BALLS = numBalls;

        hm = new HashMap<>();

        currState = (1 << numBalls) - 1;
        int endState = ((1 << numBalls) - 1) << (MAX_HEIGHT - NUM_BALLS);

        for (int i = currState; i <= endState; i++) {
            if (Integer.bitCount(i) == numBalls) {
                HashMap<Integer, Integer> subHM = new HashMap<>();

                if ((i & 1) == 0) { //checks if last element is empty
                    subHM.put(0, i >> 1);
                    hm.put(i, subHM);
                    continue;
                }

                for (int j = 0; j < maxHeight; j++) {
                    if (((i >> 1) & (1 << j)) == 0) {
                        subHM.put(j + 1, i >> 1 | (1 << j));
                    }
                }

                if (!subHM.isEmpty()) {
                    hm.put(i, subHM);
                }
            }
        }
        System.out.println("e");
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

    public void findAllCombinations() {
        ArrayList<Integer> statesVisited = new ArrayList<>();

        statesVisited.add(currState);

        for (Map.Entry<Integer, Integer> entry : hm.get(currState).entrySet()) {
            int finalState = entry.getValue();
            ArrayList<Integer> newStateVisited = new ArrayList<>(statesVisited);

            findAllCombinations(finalState, newStateVisited);
        }

        System.out.println("Finished!");
    }

    public void findAllCombinations(int state, ArrayList<Integer> statesVisited) {

        if (state == currState) {
            statesVisited.add(state);

            states.add(statesVisited);
            return;
        }

        if (statesVisited.contains(state)) {
            return;
        }

        statesVisited.add(state);

        for (Map.Entry<Integer, Integer> entry : hm.get(state).entrySet()) {
            int finalState = entry.getValue();
            ArrayList<Integer> newStateVisited = new ArrayList<>(statesVisited);

            findAllCombinations(finalState, newStateVisited);
        }
    }

}
