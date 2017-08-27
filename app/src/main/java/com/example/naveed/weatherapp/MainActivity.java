package com.example.naveed.weatherapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    private TextView temp,date,city,desc;
    private ImageView image;
    private ProgressDialog pg;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        temp = (TextView)findViewById(R.id.temp);
        city = (TextView)findViewById(R.id.city);
        desc = (TextView)findViewById(R.id.descc);
        date = (TextView)findViewById(R.id.date);
        image = (ImageView)findViewById(R.id.desc);
        pg=new ProgressDialog(this);
        //image = (ImageView) findViewById(R.id.weatherImageView);
        String ss = "Karachi";
        url = "http://api.openweathermap.org/data/2.5/weather?q="+ss+"&appid=542ffd081e67f4512b705f89d2a611b2";
        openWeatherMap(url);
    }
    private void openWeatherMap(String url)
    {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject("main");
                            String actualtemp = Integer.toString((int)Math.round(jsonObject.getDouble("temp")-273.15));

                            JSONArray jsonArray = response.getJSONArray("weather");
                            JSONObject weather = jsonArray.getJSONObject(0);
                            String description = weather.getString("description");
                            //Calendar c = Calendar.getInstance();
                            //System.out.println("Current time => " + c.getTime());
                            Calendar c = Calendar.getInstance();

                            String sDate = c.get(Calendar.YEAR) + "-"
                                    + c.get(Calendar.MONTH)
                                    + "-" + c.get(Calendar.DAY_OF_MONTH)
                                    + " at " + c.get(Calendar.HOUR_OF_DAY)
                                    + ":" + c.get(Calendar.MINUTE);

                            String name =response.getString("name");
                            temp.setText(actualtemp);
                            city.setText(name);
                            desc.setText(description);
                            date.setText(sDate);
                            int iconResourceId = getResources().getIdentifier("icon_" + description.replace(" ", ""), "drawable", getPackageName());
                            image.setImageResource(iconResourceId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(getBaseContext(),"Citi Not Found",Toast.LENGTH_LONG).show();

                    }
                });

// Access the RequestQueue through your singleton class.
        // MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsObjRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search,menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.search)
        {

            View customView = getLayoutInflater().inflate(R.layout.custom_dialoge, null);
            final EditText cityname = (EditText) customView.findViewById(R.id.citiname);
            Button btnShow = (Button) customView.findViewById(R.id.citysearch);
            final AlertDialog.Builder custom = new AlertDialog.Builder(this);
            custom.setTitle("PLEASE ENTER CITY NAME");
            final AlertDialog Dial = custom.create();
            custom.setView(customView);
            final AlertDialog show = custom.show();

            btnShow.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    //Toast.makeText(getBaseContext(),cityname.getText().toString(),Toast.LENGTH_LONG).show();
                    String searchstring = cityname.getText().toString();
                    if(!searchstring.equals("")){
                        String q = searchstring;
                        url = "http://api.openweathermap.org/data/2.5/weather?q="+searchstring+"&appid=542ffd081e67f4512b705f89d2a611b2";
                        pg.setMessage("Please wait...");
                        pg.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                openWeatherMap(url);
                                pg.dismiss();
                            }

                        },1300);

                    }

                    show.dismiss();

                }
            });


            custom.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                }
            });

          //  custom.create();
            //custom.show();

        }



        return super.onOptionsItemSelected(item);
    }
    private  final   void show() {
        pg.setMessage("Please waite");
    }
}
