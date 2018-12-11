package dixx.game;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {

    private MediaPlayer mp;
    private String score;
    private Button gameOver;
    private Button pauseBut;
    private TextView scoreText;
    private String res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        res = "0";

        mp = MediaPlayer.create(GameOver.this, R.raw.last);
        mp.setLooping(true);
        mp.start();

        score = getIntent().getExtras().get("scoreR").toString();

        gameOver = (Button) findViewById(R.id.butagain);
        pauseBut = (Button) findViewById(R.id.con);
        scoreText = (TextView)findViewById(R.id.textView);

        scoreText.setText("Score: " + score);

        gameOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                res = "0";
                Intent gameIntent = new Intent(GameOver.this, MainActivity.class);
                gameIntent.putExtra("NEWGAME", res);
                startActivity(gameIntent);;
            }
        });

        pauseBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                res = "1";
                Intent gameIntent = new Intent(GameOver.this, MainActivity.class);
                gameIntent.putExtra("NEWGAME", res);
                startActivity(gameIntent);
            }
        });
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mp.release();
        finish();
    }

}
