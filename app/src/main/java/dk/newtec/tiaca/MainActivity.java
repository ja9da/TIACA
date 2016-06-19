package dk.newtec.tiaca;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.camera2.CameraAccessException;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    SharedPreferences.OnSharedPreferenceChangeListener listener;
    public static Context context;

    private GridLayout camTableView;
    private LinearLayout freeCamsView;
    View.OnDragListener onDragListener = null;


    int cols ;
    int rows;
    static Map<String,CameraLabel> freeCamsMap = new HashMap<String,CameraLabel>();

   // private TextView cam1View;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.context);


        rows = Integer.parseInt(prefs.getString("view_row","1"));
        cols = Integer.parseInt(prefs.getString("view_column","1"));
/*
        SharedPreferences preferences = getSharedPreferences("server_port", 0);
        preferences.edit().remove("server_port").commit();
*/

        camTableView = (GridLayout)  findViewById(R.id.camTableView);

        freeCamsView = (LinearLayout) findViewById(R.id.freeCamsView);


        onDragListener = new  View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {


                switch (event.getAction()){

                    case DragEvent.ACTION_DRAG_ENTERED:
                        //System.out.println(v + ""+ event);
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                       // System.out.println(event);
                        break;

                    case DragEvent.ACTION_DROP:

                        // Gets the item containing the dragged data
                        ClipData.Item item = event.getClipData().getItemAt(0);

                        // Gets the text data from the item.

                        if (v.getTag() != null) {
                            String srcTag = item.getText().toString();
                            String destTag = v.getTag().toString();
                            System.out.println(srcTag + "->" + destTag);


                            CameraLabel src = MainActivity.freeCamsMap.get(srcTag);
                            CameraLabel dest = MainActivity.freeCamsMap.get(destTag);

                            dest.setData(src);
                        }
                        return true;

                    case DragEvent.ACTION_DRAG_ENDED:
                        System.out.println(event);
                        break;

                    default:

                        break;

                }
                return true;
            }

        };


    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (freeCamsMap.isEmpty()){
            int camCount = 0;
            for(int i=0; i<rows*cols; i++){
                CameraLabel cameraLabel = new CameraLabel(this, camCount++, false, LinearLayout.LayoutParams.MATCH_PARENT, 60);
                freeCamsMap.put((String) cameraLabel.getTag(),cameraLabel);
                freeCamsView.addView(cameraLabel);
                cameraLabel.setOnDragListener(onDragListener);
            }

            camTableView.setColumnCount(cols);
            camTableView.setColumnCount(rows);

            int tableWidth = camTableView.getWidth() ;
            int tableHeigth = camTableView.getWidth() ;

            for(int i=0; i<rows*cols; i++){
                CameraLabel cameraLabel = new CameraLabel(this, camCount++, true, tableWidth / cols, tableHeigth / rows);
                freeCamsMap.put((String) cameraLabel.getTag(),cameraLabel);
                camTableView.addView(cameraLabel);
                cameraLabel.setOnDragListener(onDragListener);
            }
        }

    }



    public void onShowSettings(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onShowDebug(View view){
        Intent intent = new Intent(this, DebugActivity.class);
        startActivity(intent);
    }



    public void onShowTrackPuck(View view){
        System.out.println("TrackPuckActivity");
        Intent intent = new Intent(this, dk.newtec.tiaca.TrackPuckActivity.class);
        startActivity(intent);
    }
}
