package dixx.game;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.MotionEvent;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class MainActivity extends AppCompatActivity {

    MediaPlayer mp;
    public static int COL = Color.BLUE;
    private Catcher catherPlayer;
    private Handler h;
    private final static int inter = 30;
    private String stringRes = "0";
    private boolean created = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent().getExtras() != null)
        {
            stringRes = getIntent().getExtras().get("NEWGAME").toString();
        }

        Log.d("errormainactivity", String.valueOf(stringRes));

        mp = MediaPlayer.create(MainActivity.this, R.raw.zelda);
        mp.setLooping(true);
        mp.start();

        catherPlayer = new Catcher(this, stringRes);

        h = new Handler();
        setContentView(catherPlayer);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        catherPlayer.invalidate();
                    }
                });
            }
        }, 0, inter);

    }



    @Override
    protected void onResume()
    {
        super.onResume();
        //stringRes = getIntent().getExtras().get("NEWGAME").toString();

    }


    @Override
    protected void onPause()
    {
        super.onPause();
        mp.release();

        finish();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        finish();
    }


    class DrawV extends View
    {

        Paint p;
        Rect rect;
        boolean IsFly;
        boolean IsDown;

        public DrawV(Context context)
        {
            super(context);
            p = new Paint();
            rect = new Rect();
            IsFly = true;
            IsDown = false;
        }



        boolean isInside(double x, double y) {

            if(COL == Color.rgb(255, 0, 255))
                COL = Color.BLUE;
            else
                COL = Color.rgb(255, 0, 255);
            double pointX = 400;
            double pointY = 700;
            double radius = 150;
            double centerX = pointX + radius;
            double centerY = pointY + radius;
            double distanceX = x - centerX;
            double distanceY = y - centerY;
            return (distanceX * distanceX) + (distanceY * distanceY) <= radius * radius;
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            canvas.drawARGB(255,255,255,255);
            p.setColor(COL);
            p.setStrokeWidth(10);
            //rect.offset(125, 125);
            canvas.drawCircle(getWidth()/2, getHeight()/2, 150, p);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
            // put your logic here
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    p.setColor(COL);

                    invalidate();
                    if(isInside(x, y))
                    {

                    }

                    break;
                case MotionEvent.ACTION_MOVE:
                    System.out.println(x + ' ' + y);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    System.out.println(x + ' ' + y);
                    break;
            }
            return super.dispatchTouchEvent(event);
        }



    }




}
