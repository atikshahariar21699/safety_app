package com.example.robiul.familylocator;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Our_details extends AppCompatActivity {

    TextView myText;
    Button bton;
    String code,key,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_details);
        myText = (TextView) findViewById(R.id.textid);
        bton=(Button) findViewById(R.id.ourdetails_id);

        Intent myintent=getIntent();

        if(myintent!=null)
        {
            code=myintent.getStringExtra("Code");
            name=myintent.getStringExtra("Name");
        }

        new MyTask().execute("https://api.myjson.com/bins/19bjrc");
        bton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gotonevigation();

            }
        });

    }

    void  gotonevigation()
    {
        Intent myintent = new Intent(this, Mynavigation.class);
        myintent.putExtra("Code",code);
        myintent.putExtra("Name",name);
        startActivity(myintent);
        finish();
    }


    private class MyTask extends AsyncTask<String, String, String> {


        @Override
        protected void onPostExecute(String result) {


            // TODO Auto-generated method stub

            StringBuilder re = new StringBuilder();
            JSONArray contacts=null;
            try {
                // Getting JSON Array node
                contacts= new JSONArray(result);
                for (int i=0;i<contacts.length();i++)
                {
                    String str=null;
                    JSONObject creator=contacts.getJSONObject(i);
                    String Uri,name,Occupation,Age,Present_address,phone,parmanent_address,email,facebook;


                        name=creator.getString("namemain");

                       Occupation=creator.getString("Occupationmain");

                        Age=creator.getString("Agemain");

                        Present_address=creator.getString("Present_addressmain");

                        parmanent_address=creator.getString("parmanent_addressmain");

                       phone=creator.getString("phone");

                       email=creator.getString("email");


                    str="\nName:"+name+"\nOccupation:"+Occupation+"\nAge:"+Age+"\nPsesent Address:"+Present_address+"\nParmanent Address:"+
                            parmanent_address +"\nphone:"+phone+"\nemail:"+email;



                        str+="\n\n";



                        re.append(str);




                }


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Can't purse Json file",Toast.LENGTH_SHORT).show();
            }






            myText.setText(re.toString());

        }
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder result = new StringBuilder();

            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Something Wrong",Toast.LENGTH_SHORT).show();
            } finally {
                urlConnection.disconnect();
            }
            return result.toString();
        }

    }



}
