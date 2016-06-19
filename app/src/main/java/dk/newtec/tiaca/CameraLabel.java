package dk.newtec.tiaca;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;



/**
 * Created by jl on 6/19/16.
 */
public class CameraLabel  extends TextView{

    private boolean isFree = true;
    private boolean isSlot = false;
    private String name = null;
    private long lastClik = 0;
    int margin = 5;
    Context context;
    String ip;
    String port;
    String tag;

    public CameraLabel(Context context, int id, boolean isSlot, int width, int height) {

        super(context);
        this.context = context;
        this.isSlot = isSlot;
        tag = "cam"+id;
        this.setTag(tag);


        this.setGravity(Gravity.CENTER);

        updateName();
        LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(width, height
               , 1f);
        params.setMargins(margin,margin,margin,margin);
        this.setBackgroundColor(Color.BLUE);
        this.setLayoutParams(params);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                long now = System.currentTimeMillis();
                if ((now-lastClik)<500){
                    System.out.println("show dialog");
                    Intent intent = new Intent(CameraLabel.this.context, CamSettingsActivity.class);
                    intent.putExtra("camTag",(String) CameraLabel.this.getTag());
                    CameraLabel.this.context.startActivity(intent);
                }
                lastClik = now;
            }
        });


       setOnLongClickListener(new View.OnLongClickListener() {

            // Defines the one method for the interface, which is called when the View is long-clicked
            public boolean onLongClick(View v) {

                ClipData.Item item = new ClipData.Item((CharSequence)v.getTag());
                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

                ClipData dragData = new ClipData(v.getTag().toString(),mimeTypes, item);
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(CameraLabel.this);

                v.startDrag(dragData,myShadow,null,0);
                return true;




            }
        });


    }

    public void update(String name, String ip, String port) {
        this.name = name;
        this.ip= ip;
        this.port = port;
        isFree = (name==null);
        updateName();
    }

    private void updateName(){
        if (isSlot){ // tableView
            if (isFree){
                this.setText("<<Free slot>>");
            }
            else {
                if (name == null){
                    this.setText(tag);
                }
                else {
                    this.setText(name);
                }

            }

        }
        else { // notUsedCams
            if (isFree){
                this.setText("Not Set");
            }
            else {
                if (name == null){
                    this.setText(tag);
                }
                else {
                    this.setText(name);
                }

            }

        }

    }

    public void setData(CameraLabel src) {
        if (!src.isFree) {
            name = src.getName();
            ip = src.getIp();
            port = src.getPort();
            src.clear();
            this.isFree = false;
            updateName();
        }
    }

    private void clear() {
        name = null;
        ip = null;
        port = null;
        updateName();

    }


    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }
}
