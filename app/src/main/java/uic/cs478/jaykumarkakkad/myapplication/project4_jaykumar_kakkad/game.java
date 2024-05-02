package uic.cs478.jaykumarkakkad.myapplication.project4_jaykumar_kakkad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

// All three threads are implemented here.
// logic for thread level working in Agent1 class
public class game extends AppCompatActivity {

    ArrayList<String> gridItems1 = new ArrayList<>();
    ArrayList<String> gridItems2 = new ArrayList<>();

    private static final int GRID_SIZE = 100;
    private static final int GRID_DIMENSION = 10;

    private int gopherLocation;
    private int nextSequenceT1 = 1;
    private int nextSequenceT2 = 1;

    private Agent1 t1;
    private Agent1 t2;

    private ArrayAdapter<String> adapter1;
    private ArrayAdapter<String> adapter2;


    // if next guess received, UI updated and feedback is generated
    // feedback is send to the respective threads
    // if success, both threads are send a runnable to quit looper
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.NEXT_GUESS:
                    int nextGuess = msg.arg1;
                    String agentName = (String) msg.obj;
                    updateUI(nextGuess, agentName);
                    String feedback = findFeedback(nextGuess);

                    // get the correct handler
                    Handler thandler;
                    if (agentName.equals("t1")) {
                         thandler = t1.getHandler();
                    }
                    else {
                        thandler = t2.getHandler();
                    }

                    // create correct message object - using runnable here
                    if (feedback.equals("Success")){

                        Runnable quitRunnable = new Runnable() {
                            @Override
                            public void run() {
                                Looper.myLooper().quit();
                            }
                        };
                        t1.getHandler().post(quitRunnable);
                        t2.getHandler().post(quitRunnable);

//                        Message feedbackMsg = t1.getHandler().obtainMessage(Constants.GAME_FINISHED, feedback);
//                        t1.getHandler().sendMessage(feedbackMsg);
//                        Message feedbackMsg1 = t2.getHandler().obtainMessage(Constants.GAME_FINISHED, feedback);
//                        t2.getHandler().sendMessage(feedbackMsg1);

                        gameends(agentName);
                    }
                    else {
                    Message feedbackMsg = thandler.obtainMessage(Constants.FIND_NEXT, feedback);
                    //feedbackMsg.obj = agentName;
                    thandler.sendMessage(feedbackMsg);}
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // ============== SET grid 1 ================================
        // Create an array or list to hold 100 empty strings
        for (int i = 0; i < 100; i++) {
            gridItems1.add(""); // Add empty string
        }

        // Create an adapter with the empty dataset
        adapter1 = new ArrayAdapter<>(this, R.layout.item_layout, gridItems1);


        // Set the adapter to GridView1
        GridView gridView1 = findViewById(R.id.gridView1);
        gridView1.setAdapter(adapter1);

        /// ==================== set GRID2 =========================
        // Create an array or list to hold 100 empty strings
        for (int i = 0; i < 100; i++) {
            gridItems2.add(""); // Add empty string
        }

        // Create an adapter with the empty dataset
        adapter2 = new ArrayAdapter<>(this, R.layout.item_layout, gridItems2);

        // Adapter for GridView2
        GridView gridView2 = findViewById(R.id.gridView2);
        gridView2.setAdapter(adapter2);

        // ================= set  listener for stop button ====
        // sends message to worker threads to stop
        // also displays a toast message
        Button abutton = findViewById(R.id.stop_button);
        abutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // stop the threads
//                if (t1 != null) {
//                    t1.myExit(); // Calls Looper.quit()
//                }
//
//                // Stop thread t2
//                if (t2 != null) {
//                    t2.myExit(); // Calls Looper.quit()
//                }
                String feedback = "SUCCESS";
                Message feedbackMsg = t1.getHandler().obtainMessage(Constants.GAME_FINISHED, feedback);
                t1.getHandler().sendMessage(feedbackMsg);
                Message feedbackMsg1 = t2.getHandler().obtainMessage(Constants.GAME_FINISHED, feedback);
                t2.getHandler().sendMessage(feedbackMsg1);

                String mssg = " YOU STOPPED the GAME";
                Toast.makeText(getApplicationContext(), mssg, Toast.LENGTH_SHORT).show();

            }
        });

        Log.i("GAME_log", "onCreate: Gridview is created");

        // ============ Randomly fix a gopher location between 1 to 100
        Random rand = new Random();
        gopherLocation = rand.nextInt(GRID_SIZE) + 1;
        String mssg1 = " GOpher location is " + gopherLocation;
        Toast.makeText(getApplicationContext(), mssg1, Toast.LENGTH_SHORT).show();

        // ============ Initialize and start thread t1
        t1 = new Agent1("t1", mHandler);
        t1.start();
        Log.i("GAME_log", "onCreate: THREAD 1 STARTED");
        // === asks to sleep till the thread has a looper ready
        // if not put to sleep, null pointer exception is generated
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Message msg1 = t1.handler.obtainMessage(Constants.FIND_NEXT);
        msg1.obj = "Complete Miss";
        t1.handler.sendMessage(msg1);
        Log.i("GAME_log", "onCreate: MESSAGE 1 SENT");


        // Initialize and start thread t2
        t2 = new Agent1("t2", mHandler);
        t2.start();
        Log.i("GAME_log", "onCreate: THREAD 2 STARTED");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Message msg2 = t2.getHandler().obtainMessage(Constants.FIND_NEXT);
        msg2.obj = "Complete Miss";
        t2.getHandler().sendMessage(msg2);
        Log.i("GAME_log", "onCreate: MESSAGE 2 SENT");

    }

    // UI thread updates the UI element with next guess for the respective bot
    // uses exiting array adapter of grid view to make changes to UI
    private void updateUI(int nextGuess, String agentName) {
        Log.i("GAME_log", "updateUI: " + agentName);
        String nextSequenceString1 = Integer.toString(nextSequenceT1);
        String nextSequenceString2 = Integer.toString(nextSequenceT2);
        int row = (nextGuess - 1) / GRID_DIMENSION;
        int col = (nextGuess - 1) % GRID_DIMENSION;
        if (agentName.equals("t1")) {
            gridItems1.set(nextGuess - 1, nextSequenceString1);
            adapter1.notifyDataSetChanged();
            nextSequenceT1++;
        } else if (agentName.equals("t2")) {
            gridItems2.set(nextGuess - 1, nextSequenceString2);
            adapter2.notifyDataSetChanged();
            nextSequenceT2++;
        }
        Log.i("GAME_log", "updateUI: ADAPTER SUCESSFUL");

    }


    // feedback based on closeness of guess to the gopher location
    private String findFeedback(int nextGuess) {
        int rowGuess = (nextGuess - 1) / GRID_DIMENSION;
        int colGuess = (nextGuess - 1) % GRID_DIMENSION;
        int rowGopher = (gopherLocation - 1) / GRID_DIMENSION;
        int colGopher = (gopherLocation - 1) % GRID_DIMENSION;

        if (nextGuess == gopherLocation) {
            return "Success";
        } else if (Math.abs(rowGuess - rowGopher) <= 1 && Math.abs(colGuess - colGopher) <= 1) {
            return "Near Miss";
        } else if (Math.abs(rowGuess - rowGopher) <= 2 && Math.abs(colGuess - colGopher) <= 2) {
            return "Close Guess";
        } else {
            return "Complete Miss";
        }
    }

    private void gameends(String name){
        String mssg = name + " wins the game";
        Toast.makeText(getApplicationContext(), mssg, Toast.LENGTH_SHORT).show();

    }

}