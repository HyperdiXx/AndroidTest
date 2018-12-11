package dixx.game;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import io.realm.DynamicRealmObject;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

import static dixx.game.R.*;


public class Catcher extends View {

    private DB data;
    private Realm realm;
    private Bitmap character;
    private Bitmap back;
    private Paint scoreP;
    private Paint hpP;
    private Bitmap pb;
    private Bitmap size;

    private boolean startNewGame;



    private String checkPoint;


    private Paint apple, pillow, undead;
    private int applevel = 15, undeadvel = 40;
    private int appleX, appleY, pillowX, pillowY, undeadX, undeadY;
    private double velX, velY, gravity, velYObj, velXObj;
    private double speed = 20;
    private int cX = 0, cY;
    private int canvasW, canvasH;
    private int score, health = 100;
    private boolean IsUn = false;
    private double angle = -30;
    private double angleInRadians = Math.toRadians(angle);
    private boolean IsTouch = false;

    public String getCheckPoint() {
        return checkPoint;
    }

    public void setCheckPoint(String checkPoint) {
        this.checkPoint = checkPoint;
    }

    public double getPosY()
    {
        return velY;
    }


    public int getScore() {
        return score;
    }

    public Catcher(Context context, String setRes)
    {
        super(context);

        cY = 550;
        score = 0;

        realm = Realm.getDefaultInstance();

        Log.d("error", setRes);

        if(setRes.equals("1"))
        {
            RealmResults<DB> realdata = realm.where(DB.class).findAllAsync();

            realdata.load();
            Log.d("insetres",  "into setres");
            for(DB data:realdata) {
                cY = data.getPosY();
                score = data.getScore();
            }
            Log.d("score",  String.valueOf(score));

        }

        character = BitmapFactory.decodeResource(getResources(), drawable.playermain);
        back = BitmapFactory.decodeResource(getResources(), drawable.full);
        pb = BitmapFactory.decodeResource(getResources(), drawable.pause);

        apple = new Paint();
        pillow = new Paint();
        undead = new Paint();
        scoreP = new Paint();
        hpP = new Paint();

        scoreP.setColor(Color.WHITE);
        scoreP.setTextSize(100);
        scoreP.setAntiAlias(true);
        scoreP.setTypeface(Typeface.DEFAULT);

        hpP.setColor(Color.WHITE);
        hpP.setTextSize(100);
        hpP.setAntiAlias(true);
        hpP.setTypeface(Typeface.DEFAULT);



        gravity = 0.2;


        velXObj = Math.cos(angleInRadians) * speed;
        velYObj = Math.sin(angleInRadians) * speed;

    }

    private void readData()
    {
        RealmResults<DB> realdata = realm.where(DB.class).findAllAsync();

        realdata.load();

        for(DB datas:realdata)
        {
            cY = datas.getPosY();
            score = datas.getScore();
        }
    }



    public int getcX() {
        return cX;
    }

    public void setcX(int cX) {
        this.cX = cX;
    }

    public int getcY() {
        return cY;
    }

    public void setcY(int cY) {
        this.cY = cY;
    }

    public void playSound(View v)
    {

       /* mp = MediaPlayer.create(this, R.raw.sound);

        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mp.start();
            }
        });
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mp.release();
            }

        });

        // pl = (Button)findViewById(R.id.pausebut);
       // pl.setOnClickListener(new View.OnClickListener() {
         //   @Override
        //    public void onClick(View view) {

//
         //       Intent gameIntent = new Intent(MainActivity.this, MainActivity.class);
        //        startActivity(gameIntent);
            }
        });
        setContentView(R.layout.activity_start);


        */
    }


    public void update(Canvas canvas)
    {
        canvasH = canvas.getHeight();
        canvasW = canvas.getWidth();


        int minPosY = 20;
        int maxPosY = canvasH - character.getHeight();

        int minPosYApple = 50;
        int maxPosYApple = canvasH - 50;

        //velYObj += gravity;
        velY += 2;

        cY += velY;

        if(cY < minPosY)
            cY = minPosY;
        if(cY > maxPosY)
            cY = maxPosY;

        if(IsTouch)
        {
            canvas.drawBitmap(character, cX, cY, null);
            IsTouch = false;
        }
        else
            canvas.drawBitmap(character, cX, cY, null);

        appleX -= velXObj;
        appleY -= velYObj;

        if(appleY < minPosYApple)
            appleY = minPosYApple;
        if(appleY > maxPosYApple)
            appleY = maxPosYApple;

        if(IsHit(appleX, appleY))
        {
            score += 10;
            appleX = -100;
        }

        if(appleX < 0) {
            appleX = canvasW + 21;
            appleY = (int)Math.floor(Math.random() * (maxPosY - 20)) + 20;
        }


        pillowX -= applevel;

        if(IsHit(pillowX, pillowY) && !IsUn)
        {
            health -= 20;
            pillowX = -100;
            if(health == 0)
            {
                //Toast.makeText(getContext(), "Game Over!", Toast.LENGTH_SHORT).show();
                writeTODB(getPosY(), getScore());
                Intent gameOver = new Intent(getContext(), GameOver.class);
                gameOver.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                gameOver.putExtra("scoreR", score);
                getContext().startActivity(gameOver);
            }
        }

        if(pillowX < 0) {
            pillowX = canvasW + 21;
            pillowY = (int)Math.floor(Math.random() * (maxPosY - 20)) + 20;
        }

        undeadX -= undeadvel;

        if(IsHit(undeadX, undeadY))
        {
            undeadX = -100;
            IsUn = true;


        }

        if(undeadX < 0)
        {
            int counter = 0;
            counter++;
            if(counter == 10)
            {
                undeadX = canvasW + 21;
                undeadY = (int)Math.floor(Math.random() * (maxPosY - 20)) + 20;
            }

        }


        canvas.drawCircle(appleX, appleY, 25, apple);
        canvas.drawCircle(pillowX, pillowY, 40, pillow);
        canvas.drawCircle(undeadX, undeadY, 30, undead);

        velYObj += gravity;
    }

    public boolean IsHit(int x, int y)
    {
        if(cX < x && x < (cX + character.getWidth()) && cY < y && y < (cY + character.getHeight()))
        {
            return true;
        }
        return false;
    }



    @Override
    protected void onDraw(Canvas canvas)
    {
       super.onDraw(canvas);

       size = Bitmap.createScaledBitmap(pb, 300, 300, true);

       canvas.drawBitmap(back, 0, 0, null);
       canvas.drawBitmap(size, 40, 120, null);



       apple.setColor(Color.GREEN);
       apple.setAntiAlias(false);
       pillow.setColor(Color.RED);
       pillow.setAntiAlias(false);
       undead.setColor(Color.BLACK);
       undead.setAntiAlias(false);

       update(canvas);
        //canvas.drawBitmap(character, 20, 300, null);
       canvas.drawText("Score: " + score, 20, 100, scoreP);
       canvas.drawText("Health: " + health, 550, 100, hpP);
    }


    public void writeTODB(double posY, int score)
    {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                data = realm.createObject(DB.class);
                data.setScore(getScore());
                data.setPosY(getcY());
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {

            }
        });

    }


    public void In()
    {
        Intent pause = new Intent(getContext(), Pause.class);
        getContext().startActivity(pause);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if( x > 40 && x < 40 + size.getWidth() && y > 120 && y < 120 + size.getHeight() )
                {

                }
                IsTouch = true;
                velY = -22;

                break;
        }

        return super.onTouchEvent(event);
    }




}
