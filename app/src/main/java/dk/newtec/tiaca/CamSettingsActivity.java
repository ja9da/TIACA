package dk.newtec.tiaca;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.TextView;

public class CamSettingsActivity extends AppCompatActivity {


    String camTag = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam_settings);
        camTag = this.getIntent().getExtras().getString("camTag");
    }

    public void onBackPressed() {
        super.onBackPressed();

        TextView nameView = (TextView)  findViewById(R.id.camNameText);
        String name = nameView.getText().toString();

        TextView ipView = (TextView)  findViewById(R.id.camIpText);
        String ip = ipView.getText().toString();

        TextView portView = (TextView)  findViewById(R.id.camPort);
        String port = portView.getText().toString();

        CameraLabel  camLabel =  MainActivity.freeCamsMap.get(camTag);

        camLabel.update(name,ip,port);

    }


}
