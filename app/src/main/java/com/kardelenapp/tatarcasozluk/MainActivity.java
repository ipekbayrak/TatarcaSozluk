package com.kardelenapp.tatarcasozluk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private EditText editText;
    private Button button;
    private AdView mAdView;
    private DBHelper mydb ;
    private Map<String, Object> hash;
    private ListView listview;
    private TextView textView1;
    boolean isEngDen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout layout = (LinearLayout) findViewById(R.id.adsContainer);
        AdsController adsController = new AdsController(this);
        adsController.loadBanner(layout);

        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);
        listview = (ListView) findViewById(R.id.listView);
        textView1 = (TextView) findViewById(R.id.textView1);

        mydb = new DBHelper(this);


        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() > 0)
                {
                    bringResults();
                }
                else
                {
                    listview.setAdapter(null);
                    textView1.setText("");
                }
            }

        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEngDen){
                    isEngDen=true;
                    button.setText("Tatarca\n->Türkçe");

                    if(editText.length() > 0)
                    {
                        bringResults();
                    }
                    else
                    {
                        listview.setAdapter(null);
                        textView1.setText("");
                    }

                }
                else {
                    isEngDen=false;
                    button.setText("Türkçe ->\nTatarca");

                    if(editText.length() > 0)
                    {
                        bringResults();
                    }
                    else
                    {
                        listview.setAdapter(null);
                        textView1.setText("");
                    }
                }

            }
        });


        listview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hash = (Map<String,Object>) listview.getItemAtPosition(position);

                String s1 = (String)hash.get("id");
                int i = Integer.parseInt(s1);


                if(isEngDen)
                {
                    textView1.setText(mydb.getMeaningEng(i));
                }
                else
                {
                    textView1.setText(mydb.getMeaningToki(i));
                }

            }
        });


    }

    public void bringResults()
    {
        if (!isEngDen)
        {
            SimpleAdapter adapter2 = new SimpleAdapter(MainActivity.this, mydb.getSimilarToki(editText.getText().toString()),
                    android.R.layout.simple_list_item_1,
                    new String[] { "isim"  },
                    new int[] { android.R.id.text1 });


            listview.setAdapter(adapter2);
        }
        else
        {
            SimpleAdapter adapter2 = new SimpleAdapter(MainActivity.this, mydb.getSimilar(editText.getText().toString()),
                    android.R.layout.simple_list_item_1,
                    new String[] { "isim" },
                    new int[] { android.R.id.text1 });

            //setContentView(listView);

            listview.setAdapter(adapter2);
        }



    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {

            case R.id.about:
                Intent myIntent = new Intent(this, Hakkinda.class);
                this.startActivity(myIntent);

                break;
        }
        return true;
    }

}
