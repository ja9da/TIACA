package dk.newtec.tiaca;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.IOException;
import java.net.Socket;

public class TrackPuckActivity extends AppCompatActivity {

    private static final int THREAD_DELAY = 1000;

    private long screenWidth, screenHeight;
    private ImageView ringImageView;
    private int currentX = -1;
    private int currentY = -1;
    private long lastMoveEvent = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_track_puck);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        ringImageView = (ImageView) findViewById(R.id.rinkImageView);


        ringImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    long now = System.currentTimeMillis();
                    if ((now - lastMoveEvent) > THREAD_DELAY / 2) {
                        lastMoveEvent = now;
                        currentX = (int) ((event.getX() * 100) / screenWidth);
                        currentY = (int) ((event.getY() * 100) / screenHeight);
                        String data = "{\"request\":\"moveLogo\", \"position\": {\"x\": "+currentX+", \"y\": "+currentY+"}\n";
                        MessageSender.getInstance().sendData(data);
                    }
                } else if (event.getAction() > 100) {
                    System.out.println("OPTIONS");
                } else {
                    System.out.println(event.getAction());
                }


                return false;
            }
        });





        Drawable mImage = getResources().getDrawable(R.drawable.rink, null);

      
        ringImageView.setBackground(mImage);



    }





}
