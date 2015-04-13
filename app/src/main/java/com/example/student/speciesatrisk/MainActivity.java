package com.example.student.speciesatrisk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    //Debugging
    private static String TAG = "MainActivity";

    //DIALOG MESSAGES
    public static final String ABOUT_MESSAGE = "Species at Risk is an open application used to observe and analyze data from wildlife species that are at risk of becoming extinct.";
    public static final String HELP_MESSAGE = "The data is observed by studying the Canadian Environmental Sustainability Indicators (CESI) status of risk levels.";
    public static final String CONTACT_MESSAGE = "You can contact the developer of this application at imohamed7@myseneca.ca";



    //Data
    private String selection;
    private String currentURL;
    private List<String> lines = new ArrayList<String>();
    private List<String> specieNames = new ArrayList<String>();

    //UI
    private Spinner sp;
    private TextView t1, t2, t3;
    private Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readFromFile();
        setupView();

    }

    private void setupView() {
        sp = (Spinner)findViewById(R.id.spinner);
        t1 = (TextView)findViewById(R.id.num1);
        t2 = (TextView)findViewById(R.id.num2);
        t3 = (TextView)findViewById(R.id.num3);
        b = (Button)findViewById(R.id.button);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, specieNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);

        // TODO add event listener methods to spinner
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                findSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, WebViewActivity.class);
                i.putExtra("key", currentURL);
                startActivity(i);
            }
        });

    }

    private void findSelection(int position) {
        String selection = specieNames.get(position);
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains(selection)) {
                String[] columns = line.split(",");
                t1.setText(columns[1]);
                t2.setText(columns[2]);
                t3.setText(columns[3]);

                currentURL = columns[4];

            }

        }
    }

    private void readFromFile() {
        try {
            //InputStreamReader is = new InputStreamReader(getAssets()
              //      .open("cosewic_en.csv"));
            InputStreamReader isr = new InputStreamReader((getResources().openRawResource(R.raw.cosewic_en)));

            BufferedReader reader = new BufferedReader(isr);
            reader.readLine(); //ignore title
            reader.readLine();//ignore empty line
            reader.readLine();//ignore column headers

            String line;
            while ((line = reader.readLine()) != null) {
                if (BuildConfig.DEBUG) {
                    Log.v(TAG, "line = " + line);
                }

                lines.add(line);
                String[] linesArr = line.split(",");

                if (BuildConfig.DEBUG) {
                    Log.v(TAG, "linesArr.length = " + linesArr.length);
                }

                specieNames.add(linesArr[0]);

            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            showAboutDialog();

            return true;
        }
        else if(id == R.id.action_help){
            showHelpDialog();
            return true;
        }
        else if(id == R.id.action_contact){
            showContactDialog();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    // or instead stackoverflow 17462836
    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(ABOUT_MESSAGE)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                           dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(HELP_MESSAGE)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void showContactDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(CONTACT_MESSAGE)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
