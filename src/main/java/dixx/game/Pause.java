package dixx.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class Pause extends AppCompatActivity {

    private Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        start = (Button) findViewById(R.id.resumebut);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameIntent = new Intent(Pause.this, MainActivity.class);
                startActivity(gameIntent);
            }
        });

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        finish();
    }

}
