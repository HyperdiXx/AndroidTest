package dixx.game;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class StartActivity extends AppCompatActivity {

    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle SavedInstance)
    {
        super.onCreate(SavedInstance);
        setContentView(R.layout.activity_main);

        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder().name("realmdb.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        mp = MediaPlayer.create(StartActivity.this, R.raw.sound);
        mp.start();

        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {

                    sleep(5000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    Intent mainIntent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                }

            }
        };
        thread.start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mp.release();

        finish();
    }
}
