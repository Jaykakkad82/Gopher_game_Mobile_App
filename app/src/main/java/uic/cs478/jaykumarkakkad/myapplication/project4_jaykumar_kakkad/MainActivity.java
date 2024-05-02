package uic.cs478.jaykumarkakkad.myapplication.project4_jaykumar_kakkad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button abutton = findViewById(R.id.button_start);
        abutton.setOnClickListener(new View.OnClickListener() {

            // Checks first for permission to start the game, if not asks for permission
            @Override
            public void onClick(View v) {
                //startGame();

                // ==============
                if (ContextCompat.checkSelfPermission(MainActivity.this, "com.me.app.myapp.jkakkad.permission.GAME_PLAYER") == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted, start the game
                    // GO to game
                    startGame();

                } else {
                    // Permission is not granted, request it from the user
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{"com.me.app.myapp.jkakkad.permission.GAME_PLAYER"}, PERMISSION_REQUEST_CODE);
                }
                //=================

            }
        });

    }

    private void startGame() {
        // Start the game activity
        Intent intent = new Intent(MainActivity.this, game.class);
        startActivity(intent);

    }

    //  if permission not received displays message
    // if received start the game
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, start the game
                startGame();
            } else {
                // Permission is denied, handle accordingly (e.g., show a message)
                Toast.makeText(this, "Permission denied. Game cannot be started.", Toast.LENGTH_SHORT).show();
            }
        }
    }
//    =================
}