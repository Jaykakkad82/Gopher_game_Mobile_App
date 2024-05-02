package uic.cs478.jaykumarkakkad.myapplication.project4_jaykumar_kakkad;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

public class Agent1 extends HandlerThread {
    public Handler handler;
    private ArrayList<Integer> allGuesses;
    private Handler mainHandler;
    private String agentname;

    //private int i = 0;

    int startRow1 = 2;
    int startRow2 = 7;
    int startCol1 = 4;
    int startCol2 = 6;


    int prev_holes_found = 0;

    Queue<Integer> queue = new LinkedList<>();
    Set<Integer> visited = new HashSet<>();

    Set<Integer> allguesses = new HashSet<>();


    int next_guess = 0;

    LinkedList<Integer> mylinkedList = (LinkedList<Integer>) queue;

    public Agent1(String name, Handler mainHandler) {
        super(name);
        agentname = name;
        this.mainHandler = mainHandler;
        allGuesses = new ArrayList<>();
    }

    // Since handler thread is used, implemented onlooperprepared()
    // implements logic for game finished and find next
    // find next runs algorithm based on which thread is executing

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        handler = new Handler(getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Log.i(agentname, "HandleMEssage: MESSAGE Received");

                switch (msg.what) {
                    case Constants.GAME_FINISHED:
                        // Terminate the thread
                        Log.i(agentname, "GAME Finished; THREAD TErminated");

                        getLooper().quit();
                        break;
                    case Constants.FIND_NEXT:
                        // See the feedback from previous guess
                        String feedback = (String) msg.obj;
                        // Find the next guess using the BFS algorithm
                        int nextGuess;
                        if (agentname.equals("t1")){
                            nextGuess = bfsAlgorithm(feedback);
                        }
                        else {
                            nextGuess = sequenceAlgo(feedback);
                        }

                        // Pass the next guess to the main thread as a message
                        Message nextGuessMsg = mainHandler.obtainMessage(Constants.NEXT_GUESS);
                        nextGuessMsg.obj = agentname;
                        nextGuessMsg.arg1 = nextGuess;
                        mainHandler.sendMessage(nextGuessMsg);
                        Log.i(agentname, "HandleMEssage: MESSAGE SENT BACK to MAIN : ");
                        // Sleep for 5 seconds
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.i(agentname, "HandleMEssage: COMPLETE MESSAGE DONE!!!! ");
                        break;
                }
            }
        };
    }

    public void myExit() {
        // Quit the looper to exit the thread's message loop
        getLooper().quit();
    }

    private int bfsAlgorithm(String feedback) {

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {1,1}, {1,-1},{-1,1},{-1,-1}};

        boolean addOnTop = false;

        if(visited.size() == 0){
            next_guess= startRow1 * 10 + startCol1;
            visited.add(next_guess);
            queue.offer(next_guess);
        }



            next_guess = queue.poll();
            int row = next_guess / 10;
            int col = next_guess % 10;
            prev_holes_found = 0;

            for (int[] dir : directions) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                // Check if the new position is within bounds and not visited
                int newHole = newRow * 10 + newCol;
                if (newRow >= 0 && newRow <= 9 && newCol >= 1 && newCol <= 10 && !visited.contains(newHole)) {
                    // Mark the new hole as visited and add it to the queue
                    visited.add(newHole);
                    prev_holes_found++;
                    queue.offer(newHole);

                }

            }

        allguesses.add(next_guess);
        return next_guess;
    }

    private int sequenceAlgo(String feedback) {

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {1,1}, {1,-1},{-1,1},{-1,-1}};

        boolean addOnTop = false;

        if(visited.size() == 0){
            next_guess= startRow2 * 10 + startCol2;
            visited.add(next_guess);
            queue.offer(next_guess);
        }
            // if any of the feebacks, then change the queuu
        // bring ahead neighbors of last guess
            if (feedback.equals("Near Miss") || feedback.equals("Close Guess")){

                Log.i("AGENT","Feedback : "+ feedback + next_guess);
                for (int i = 0; i < prev_holes_found; i++) {
                    if (!queue.isEmpty()) {
                        int lastValue = mylinkedList.removeLast();
                        mylinkedList.addFirst(lastValue);
                    }
            }

            }

        next_guess = queue.poll();
        int row = next_guess / 10;
        int col = next_guess % 10;
        prev_holes_found = 0;

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            // Check if the new position is within bounds and not visited
            int newHole = newRow * 10 + newCol;
            if (newRow >= 0 && newRow <= 9 && newCol >= 1 && newCol <= 10 && !visited.contains(newHole)) {
                // Mark the new hole as visited and add it to the queue
                visited.add(newHole);
                prev_holes_found++;
                queue.offer(newHole);

            }

        }

        allguesses.add(next_guess);
        return next_guess;
    }


    public Handler getHandler() {
        return handler;
    }

}

