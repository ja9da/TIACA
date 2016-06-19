package dk.newtec.tiaca;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DebugActivity extends AppCompatActivity implements ResponseListener{

    ListView  previoursCommandsList;
    EditText sendText;
    EditText responseText;


    final ArrayList<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        previoursCommandsList = (ListView) findViewById(R.id.previousCommandsList);
        sendText = (EditText)  findViewById(R.id.editText);

        responseText= (EditText)  findViewById(R.id.outputText);





        final ListAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);

        previoursCommandsList.setAdapter(adapter);

        previoursCommandsList.setClickable(true);
        previoursCommandsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Object o = DebugActivity.this. previoursCommandsList.getItemAtPosition(position);
                sendText.setText((String) o);
            }
        });


    }

    public void onSend(View view){
        System.out.println("Send");
        String textToSend = sendText.getText().toString();

        if (!list.contains(textToSend)) {
            if (list.size() > 9) {
                list.remove(9);

            }
            list.add(0,textToSend);
            previoursCommandsList.invalidateViews();
        }


    }

    @Override
    public void handleResponse(String response) {
        responseText.setText(response);
    }
}
