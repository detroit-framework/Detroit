package very.amazing.automotive.tool.detroit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.StrictMode;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class AppDisplay extends Activity {

    String OPENIVN = "SERVER-URL:1609";
    String API_KEY = "XYZ";

    public static List<Long> timeMetric;
    ArrayList<Integer> sizeMetric;
    private JSONArray arr;
    String name, app_id, streaming;
    String make, model, year;
    private static Recorder recorder;
    public static File timeFile, sizeFile;
    public static String folder = "event-traces";

    static boolean pressed1 = false;
    static boolean pressed2 = false;
    static boolean pressed3 = false;
    static boolean pressed4 = false;
    static boolean pressed5 = false;
    static boolean pressed6 = false;
    static boolean pressed7 = false;
    static boolean pressed8 = false;
    static boolean pressed9 = false;
    static boolean pressed10 = false;
    static boolean pressed11 = false;
    static boolean pressed12 = false;
    static boolean pressed13 = false;
    static boolean pressed14 = false;
    static boolean pressed15 = false;
    static boolean pressed16 = false;
    static boolean pressed17 = false;
    static boolean pressed18 = false;
    static boolean pressed19 = false;
    static boolean pressed20 = false;
    static boolean pressed21 = false;
    static boolean pressed22 = false;
    static boolean pressed23 = false;
    static boolean pressed24 = false;
    static boolean pressed25 = false;
    static boolean pressed26 = false;
    static boolean pressed27 = false;
    static boolean pressed28 = false;
    static boolean pressed29 = false;
    static boolean pressed30 = false;
    static boolean pressed31 = false;
    static boolean pressed32 = false;
    static boolean pressed33 = false;
    static boolean pressed34 = false;
    static boolean pressed35 = false;
    static boolean pressed36 = false;
    static boolean pressed37 = false;
    static boolean pressed38 = false;
    static boolean pressed39 = false;
    static boolean pressed40 = false;
    static boolean pressed41 = false;
    static boolean pressed42 = false;
    static boolean pressed43 = false;
    static boolean pressed44 = false;
    static boolean pressed45 = false;
    static boolean pressed46 = false;
    static boolean pressed47 = false;
    static boolean pressed48 = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        make = getIntent().getStringExtra("MMY").split(" ")[0];
        model = getIntent().getStringExtra("MMY").split(" ")[1];
        year = getIntent().getStringExtra("MMY").split(" ")[2];

        timeMetric = new ArrayList();
        sizeMetric = new ArrayList<>();

        timeFile = getExternalFilesDir(folder);
        timeFile.mkdirs();

        sizeFile = getExternalFilesDir(folder);
        sizeFile.mkdirs();

        String url = OPENIVN + "/api/v1/apps";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        displayApps(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("x-api-key", API_KEY);
                return headers;
            }
        };

        queue.add(getRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            String appNum = data.getExtras().getString("buttonNum");
            app_id = appNum;

            Button mButton = (Button) findViewById(getResources().getIdentifier("app" + appNum, "id",
                    this.getPackageName()));
            mButton.setBackgroundColor(Color.YELLOW);

            recorder = new Recorder(app_id, make, model, year, this);
            recorder.startRecording(AppDisplay.this, 0);

            if(appNum.equals("1")){
                pressed1 = true;
            }
            else if(appNum.equals("2")){
                pressed2 = true;
            }
            else if(appNum.equals("3")){
                pressed3 = true;
            }
            else if(appNum.equals("4")){
                pressed4 = true;
            }
            else if(appNum.equals("5")){
                pressed5 = true;
            }
            else if(appNum.equals("6")){
                pressed6 = true;
            }
            else if(appNum.equals("7")){
                pressed7 = true;
            }
            else if(appNum.equals("8")){
                pressed8 = true;
            }
            else if(appNum.equals("9")){
                pressed9 = true;
            }
            else if(appNum.equals("10")){
                pressed10 = true;
            }
            else if(appNum.equals("11")){
                pressed11 = true;
            }
            else if(appNum.equals("12")){
                pressed12 = true;
            }
            else if(appNum.equals("13")){
                pressed13 = true;
            }
            else if(appNum.equals("14")){
                pressed14 = true;
            }
            else if(appNum.equals("15")){
                pressed15 = true;
            }
            else if(appNum.equals("16")){
                pressed16 = true;
            }
            else if(appNum.equals("17")){
                pressed17 = true;
            }
            else if(appNum.equals("18")){
                pressed18 = true;
            }
            else if(appNum.equals("19")){
                pressed19 = true;
            }
            else if(appNum.equals("20")){
                pressed20 = true;
            }
            else if(appNum.equals("21")){
                pressed21 = true;
            }
            else if(appNum.equals("22")){
                pressed22 = true;
            }
            else if(appNum.equals("23")){
                pressed23 = true;
            }
            else if(appNum.equals("24")){
                pressed24 = true;
            }
            else if(appNum.equals("25")){
                pressed25 = true;
            }
            else if(appNum.equals("26")){
                pressed26 = true;
            }
            else if(appNum.equals("27")){
                pressed27 = true;
            }
            else if(appNum.equals("28")){
                pressed28 = true;
            }
            else if(appNum.equals("29")){
                pressed29 = true;
            }
            else if(appNum.equals("30")){
                pressed30 = true;
            }
            else if(appNum.equals("31")){
                pressed31 = true;
            }
            else if(appNum.equals("32")){
                pressed32 = true;
            }
            else if(appNum.equals("33")){
                pressed33 = true;
            }
            else if(appNum.equals("34")){
                pressed34 = true;
            }
            else if(appNum.equals("35")){
                pressed35 = true;
            }
            else if(appNum.equals("36")){
                pressed36 = true;
            }
            else if(appNum.equals("37")){
                pressed37 = true;
            }
            else if(appNum.equals("38")){
                pressed38 = true;
            }
            else if(appNum.equals("39")){
                pressed39 = true;
            }
            else if(appNum.equals("40")){
                pressed40 = true;
            }
            else if(appNum.equals("41")){
                pressed41 = true;
            }
            else if(appNum.equals("42")){
                pressed42 = true;
            }
            else if(appNum.equals("43")){
                pressed43 = true;
            }
            else if(appNum.equals("44")){
                pressed44 = true;
            }
            else if(appNum.equals("45")){
                pressed45 = true;
            }
            else if(appNum.equals("46")){
                pressed46 = true;
            }
            else if(appNum.equals("47")){
                pressed47 = true;
            }
            else if(appNum.equals("48")){
                pressed48 = true;
            }
        }
        else if (requestCode == 200) {
            String appNum = data.getExtras().getString("buttonNum");
            app_id = appNum;

            Button mButton = (Button) findViewById(getResources().getIdentifier("app" + appNum, "id",
                    this.getPackageName()));
            mButton.setBackgroundColor(Color.YELLOW);

            recorder = new Recorder(app_id, make, model, year, this);
            recorder.startRecording(AppDisplay.this, 1);

            if(appNum.equals("1")){
                pressed1 = true;
            }
            else if(appNum.equals("2")){
                pressed2 = true;
            }
            else if(appNum.equals("3")){
                pressed3 = true;
            }
            else if(appNum.equals("4")){
                pressed4 = true;
            }
            else if(appNum.equals("5")){
                pressed5 = true;
            }
            else if(appNum.equals("6")){
                pressed6 = true;
            }
            else if(appNum.equals("7")){
                pressed7 = true;
            }
            else if(appNum.equals("8")){
                pressed8 = true;
            }
            else if(appNum.equals("9")){
                pressed9 = true;
            }
            else if(appNum.equals("10")){
                pressed10 = true;
            }
            else if(appNum.equals("11")){
                pressed11 = true;
            }
            else if(appNum.equals("12")){
                pressed12 = true;
            }
            else if(appNum.equals("13")){
                pressed13 = true;
            }
            else if(appNum.equals("14")){
                pressed14 = true;
            }
            else if(appNum.equals("15")){
                pressed15 = true;
            }
            else if(appNum.equals("16")){
                pressed16 = true;
            }
            else if(appNum.equals("17")){
                pressed17 = true;
            }
            else if(appNum.equals("18")){
                pressed18 = true;
            }
            else if(appNum.equals("19")){
                pressed19 = true;
            }
            else if(appNum.equals("20")){
                pressed20 = true;
            }
            else if(appNum.equals("21")){
                pressed21 = true;
            }
            else if(appNum.equals("22")){
                pressed22 = true;
            }
            else if(appNum.equals("23")){
                pressed23 = true;
            }
            else if(appNum.equals("24")){
                pressed24 = true;
            }
            else if(appNum.equals("25")){
                pressed25 = true;
            }
            else if(appNum.equals("26")){
                pressed26 = true;
            }
            else if(appNum.equals("27")){
                pressed27 = true;
            }
            else if(appNum.equals("28")){
                pressed28 = true;
            }
            else if(appNum.equals("29")){
                pressed29 = true;
            }
            else if(appNum.equals("30")){
                pressed30 = true;
            }
            else if(appNum.equals("31")){
                pressed31 = true;
            }
            else if(appNum.equals("32")){
                pressed32 = true;
            }
            else if(appNum.equals("33")){
                pressed33 = true;
            }
            else if(appNum.equals("34")){
                pressed34 = true;
            }
            else if(appNum.equals("35")){
                pressed35 = true;
            }
            else if(appNum.equals("36")){
                pressed36 = true;
            }
            else if(appNum.equals("37")){
                pressed37 = true;
            }
            else if(appNum.equals("38")){
                pressed38 = true;
            }
            else if(appNum.equals("39")){
                pressed39 = true;
            }
            else if(appNum.equals("40")){
                pressed40 = true;
            }
            else if(appNum.equals("41")){
                pressed41 = true;
            }
            else if(appNum.equals("42")){
                pressed42 = true;
            }
            else if(appNum.equals("43")){
                pressed43 = true;
            }
            else if(appNum.equals("44")){
                pressed44 = true;
            }
            else if(appNum.equals("45")){
                pressed45 = true;
            }
            else if(appNum.equals("46")){
                pressed46 = true;
            }
            else if(appNum.equals("47")){
                pressed47 = true;
            }
            else if(appNum.equals("48")){
                pressed48 = true;
            }
        }
    }

    public void displayApps(JSONObject r) {
        try {
            arr = r.getJSONArray("apps");

            for (int i = 0; i < arr.length(); ++i) {
                JSONObject obj = (JSONObject) arr.get(i);
                name = obj.getString("name");

                Button mButton = (Button) findViewById(getResources().getIdentifier("app" + (i + 1), "id",
                        this.getPackageName()));
                mButton.setText(name);
            }

            final Button app1 = findViewById(R.id.app1);
            final Button app2 = findViewById(R.id.app2);
            final Button app3 = findViewById(R.id.app3);
            final Button app4 = findViewById(R.id.app4);
            final Button app5 = findViewById(R.id.app5);
            final Button app6 = findViewById(R.id.app6);
            final Button app7 = findViewById(R.id.app7);
            final Button app8 = findViewById(R.id.app8);
            final Button app9 = findViewById(R.id.app9);
            final Button app10 = findViewById(R.id.app10);
            final Button app11 = findViewById(R.id.app11);
            final Button app12 = findViewById(R.id.app12);
            final Button app13 = findViewById(R.id.app13);
            final Button app14 = findViewById(R.id.app14);
            final Button app15 = findViewById(R.id.app15);
            final Button app16 = findViewById(R.id.app16);
            final Button app17 = findViewById(R.id.app17);
            final Button app18 = findViewById(R.id.app18);
            final Button app19 = findViewById(R.id.app19);
            final Button app20 = findViewById(R.id.app20);
            final Button app21 = findViewById(R.id.app21);
            final Button app22 = findViewById(R.id.app22);
            final Button app23 = findViewById(R.id.app23);
            final Button app24 = findViewById(R.id.app24);
            final Button app25 = findViewById(R.id.app25);
            final Button app26 = findViewById(R.id.app26);
            final Button app27 = findViewById(R.id.app27);
            final Button app28 = findViewById(R.id.app28);
            final Button app29 = findViewById(R.id.app29);
            final Button app30 = findViewById(R.id.app30);
            final Button app31 = findViewById(R.id.app31);
            final Button app32 = findViewById(R.id.app32);
            final Button app33 = findViewById(R.id.app33);
            final Button app34 = findViewById(R.id.app34);
            final Button app35 = findViewById(R.id.app35);
            final Button app36 = findViewById(R.id.app36);
            final Button app37 = findViewById(R.id.app37);
            final Button app38 = findViewById(R.id.app38);
            final Button app39 = findViewById(R.id.app39);
            final Button app40 = findViewById(R.id.app40);
            final Button app41 = findViewById(R.id.app41);
            final Button app42 = findViewById(R.id.app42);
            final Button app43 = findViewById(R.id.app43);
            final Button app44 = findViewById(R.id.app44);
            final Button app45 = findViewById(R.id.app45);
            final Button app46 = findViewById(R.id.app46);
            final Button app47 = findViewById(R.id.app47);
            final Button app48 = findViewById(R.id.app48);

            app1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed1 == false){
                        genPerms(1);
                    }
                    else{
                        terminate();
                        app1.setBackgroundColor(Color.GREEN);
                        pressed1 = false;
                    }
                }
            });
            app2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed2 == false){
                        genPerms(2);
                    }
                    else{
                        terminate();
                        app2.setBackgroundColor(Color.GREEN);
                        pressed2 = false;
                    }

                }
            });
            app3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed3 == false){
                        genPerms(3);
                    }
                    else{
                        terminate();
                        app3.setBackgroundColor(Color.GREEN);
                        pressed3 = false;
                    }
                }
            });
            app4.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed4 == false){
                        genPerms(4);
                    }
                    else{
                        terminate();
                        app4.setBackgroundColor(Color.GREEN);
                        pressed4 = false;
                    }
                }
            });
            app5.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed5 == false){
                        genPerms(5);
                    }
                    else{
                        terminate();
                        app5.setBackgroundColor(Color.GREEN);
                        pressed5 = false;
                    }
                }
            });
            app6.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed6 == false){
                        genPerms(6);
                    }
                    else{
                        terminate();
                        app6.setBackgroundColor(Color.GREEN);
                        pressed6 = false;
                    }
                }
            });
            app7.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed7 == false){
                        genPerms(7);
                    }
                    else{
                        terminate();
                        app7.setBackgroundColor(Color.GREEN);
                        pressed7 = false;
                    }
                }
            });
            app8.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed8 == false){
                        genPerms(8);
                    }
                    else{
                        terminate();
                        app8.setBackgroundColor(Color.GREEN);
                        pressed8 = false;
                    }
                }
            });
            app9.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed9 == false){
                        genPerms(9);
                    }
                    else{
                        terminate();
                        app9.setBackgroundColor(Color.GREEN);
                        pressed9 = false;
                    }
                }
            });
            app10.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed9 == false){
                        genPerms(10);
                    }
                    else{
                        terminate();
                        app10.setBackgroundColor(Color.GREEN);
                        pressed10 = false;
                    }
                }
            });
            app11.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed11 == false){
                        genPerms(11);
                    }
                    else{
                        terminate();
                        app11.setBackgroundColor(Color.GREEN);
                        pressed11 = false;
                    }
                }
            });
            app12.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed12 == false){
                        genPerms(12);
                    }
                    else{
                        terminate();
                        app12.setBackgroundColor(Color.GREEN);
                        pressed12 = false;
                    }
                }
            });
            app13.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed13 == false){
                        genPerms(13);
                    }
                    else{
                        terminate();
                        app13.setBackgroundColor(Color.GREEN);
                        pressed13 = false;
                    }
                }
            });
            app14.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed14 == false){
                        genPerms(14);
                    }
                    else{
                        terminate();
                        app14.setBackgroundColor(Color.GREEN);
                        pressed14 = false;
                    }
                }
            });
            app15.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed15 == false){
                        genPerms(15);
                    }
                    else{
                        terminate();
                        app15.setBackgroundColor(Color.GREEN);
                        pressed15 = false;
                    }
                }
            });
            app16.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed16 == false){
                        genPerms(16);
                    }
                    else{
                        terminate();
                        app16.setBackgroundColor(Color.GREEN);
                        pressed16 = false;
                    }
                }
            });
            app17.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed17 == false){
                        genPerms(17);
                    }
                    else{
                        terminate();
                        app17.setBackgroundColor(Color.GREEN);
                        pressed17 = false;
                    }
                }
            });
            app18.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed18 == false){
                        genPerms(18);
                    }
                    else{
                        terminate();
                        app18.setBackgroundColor(Color.GREEN);
                        pressed18 = false;
                    }
                }
            });
            app19.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed19 == false){
                        genPerms(19);
                    }
                    else{
                        terminate();
                        app19.setBackgroundColor(Color.GREEN);
                        pressed19 = false;
                    }
                }
            });
            app20.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed20 == false){
                        genPerms(20);
                    }
                    else{
                        terminate();
                        app20.setBackgroundColor(Color.GREEN);
                        pressed20 = false;
                    }
                }
            });
            app21.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed21 == false){
                        genPerms(21);
                    }
                    else{
                        terminate();
                        app21.setBackgroundColor(Color.GREEN);
                        pressed21 = false;
                    }
                }
            });
            app22.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed22 == false){
                        genPerms(22);
                    }
                    else{
                        terminate();
                        app22.setBackgroundColor(Color.GREEN);
                        pressed22 = false;
                    }
                }
            });
            app23.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed23 == false){
                        genPerms(23);
                    }
                    else{
                        terminate();
                        app23.setBackgroundColor(Color.GREEN);
                        pressed23 = false;
                    }
                }
            });
            app24.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed24 == false){
                        genPerms(24);
                    }
                    else{
                        terminate();
                        app24.setBackgroundColor(Color.GREEN);
                        pressed24 = false;
                    }
                }
            });
            app25.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed25 == false){
                        genPerms(25);
                    }
                    else{
                        terminate();
                        app25.setBackgroundColor(Color.GREEN);
                        pressed25 = false;
                    }
                }
            });
            app26.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed26 == false){
                        genPerms(26);
                    }
                    else{
                        terminate();
                        app26.setBackgroundColor(Color.GREEN);
                        pressed26 = false;
                    }
                }
            });
            app27.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed27 == false){
                        genPerms(27);
                    }
                    else{
                        terminate();
                        app27.setBackgroundColor(Color.GREEN);
                        pressed27 = false;
                    }
                }
            });
            app28.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed28 == false){
                        genPerms(28);
                    }
                    else{
                        terminate();
                        app28.setBackgroundColor(Color.GREEN);
                        pressed28 = false;
                    }
                }
            });
            app29.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed29 == false){
                        genPerms(29);
                    }
                    else{
                        terminate();
                        app29.setBackgroundColor(Color.GREEN);
                        pressed29 = false;
                    }
                }
            });
            app30.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed30 == false){
                        genPerms(30);
                    }
                    else{
                        terminate();
                        app30.setBackgroundColor(Color.GREEN);
                        pressed30 = false;
                    }
                }
            });
            app31.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed31 == false){
                        genPerms(31);
                    }
                    else{
                        terminate();
                        app32.setBackgroundColor(Color.GREEN);
                        pressed32 = false;
                    }
                }
            });
            app33.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed33 == false){
                        genPerms(33);
                    }
                    else{
                        terminate();
                        app33.setBackgroundColor(Color.GREEN);
                        pressed33 = false;
                    }
                }
            });
            app34.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed34 == false){
                        genPerms(34);
                    }
                    else{
                        terminate();
                        app34.setBackgroundColor(Color.GREEN);
                        pressed34 = false;
                    }
                }
            });
            app35.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed35 == false){
                        genPerms(35);
                    }
                    else{
                        terminate();
                        app35.setBackgroundColor(Color.GREEN);
                        pressed35 = false;
                    }
                }
            });
            app36.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed36 == false){
                        genPerms(36);
                    }
                    else{
                        terminate();
                        app36.setBackgroundColor(Color.GREEN);
                        pressed36 = false;
                    }
                }
            });
            app37.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed37 == false){
                        genPerms(37);
                    }
                    else{
                        terminate();
                        app37.setBackgroundColor(Color.GREEN);
                        pressed37 = false;
                    }
                }
            });
            app38.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed38 == false){
                        genPerms(38);
                    }
                    else{
                        terminate();
                        app38.setBackgroundColor(Color.GREEN);
                        pressed38 = false;
                    }
                }
            });
            app39.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed39 == false){
                        genPerms(39);
                    }
                    else{
                        terminate();
                        app39.setBackgroundColor(Color.GREEN);
                        pressed39 = false;
                    }
                }
            });
            app40.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed40 == false){
                        genPerms(40);
                    }
                    else{
                        terminate();
                        app40.setBackgroundColor(Color.GREEN);
                        pressed40 = false;
                    }
                }
            });
            app41.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed41 == false){
                        genPerms(41);
                    }
                    else{
                        terminate();
                        app41.setBackgroundColor(Color.GREEN);
                        pressed41 = false;
                    }
                }
            });
            app42.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed42 == false){
                        genPerms(42);
                    }
                    else{
                        terminate();
                        app42.setBackgroundColor(Color.GREEN);
                        pressed42 = false;
                    }
                }
            });
            app43.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed43 == false){
                        genPerms(43);
                    }
                    else{
                        terminate();
                        app43.setBackgroundColor(Color.GREEN);
                        pressed43 = false;
                    }
                }
            });
            app44.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed44 == false){
                        genPerms(44);
                    }
                    else{
                        terminate();
                        app44.setBackgroundColor(Color.GREEN);
                        pressed44 = false;
                    }
                }
            });
            app45.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed45 == false){
                        genPerms(45);
                    }
                    else{
                        terminate();
                        app45.setBackgroundColor(Color.GREEN);
                        pressed45 = false;
                    }
                }
            });
            app46.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed46 == false){
                        genPerms(46);
                    }
                    else{
                        terminate();
                        app47.setBackgroundColor(Color.GREEN);
                        pressed47 = false;
                    }
                }
            });
            app48.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pressed48 == false){
                        genPerms(48);
                    }
                    else{
                        terminate();
                        app48.setBackgroundColor(Color.GREEN);
                        pressed48 = false;
                    }
                }
            });
        }
        catch(JSONException e) {
            Log.e("Error", "JSONException occured!");
        }
    }

    public void genPerms(int appNum){
        Intent i = new Intent(AppDisplay.this, Permissions.class);
        try{
            JSONObject obj = (JSONObject) arr.get(appNum - 1);

            streaming = obj.getString("streaming");

            i.putExtra("name", obj.getString("name"));
            i.putExtra("desc", obj.getString("description"));
            i.putExtra("author", obj.getString("author_name"));
            i.putExtra("appNum", Integer.toString(appNum));
            i.putExtra("streaming", streaming);
            i.putExtra("author_id", obj.getString("author_id"));
            i.putExtra("make", make);
            i.putExtra("model", model);
            i.putExtra("year", year);
        }
        catch(JSONException e){
            Log.e("error", e.toString());
        }

        if(streaming.equals("0"))
        {
            Log.d("debug", "offline mode starting through AppDisplay");
            startActivityForResult(i, 100);
        }
        else if (streaming.equals("1"))
        {
            Log.d("debug", "online mode starting through AppDisplay");
            startActivityForResult(i,200);
        }
    }

    public void terminate(){
        if(streaming.equals("0")){
            Toast.makeText(AppDisplay.this, "Recording stopped", Toast.LENGTH_LONG).show();
            Thread zipandupload = new Thread(new Runnable() {
                @Override
                public void run() {
                    String myFile = recorder.next("data");
                    recorder.stopRecording(AppDisplay.this);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                    String currentDateandTime = sdf.format(new Date());
                    String zipFile = "/storage/emulated/0/Android/data/very.amazing.automotive.tool.detroit/files/event-traces/dataZip_" + currentDateandTime + ".zip";
                    zip(myFile,zipFile);
                }
            });
            zipandupload.start();
        }
        else{
            Toast.makeText(AppDisplay.this, "Recording stopped", Toast.LENGTH_LONG).show();
            recorder.stopRecording(AppDisplay.this);
        }
    }

    public void zip(String file, String zipFileName) {
        try {
            int BUFFER = 8192;
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipFileName);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            byte data[] = new byte[BUFFER];

            Log.v("Compress", "Adding: " +  file);
            FileInputStream fi = new FileInputStream(file);
            origin = new BufferedInputStream(fi, BUFFER);

            ZipEntry entry = new ZipEntry(file.substring(file.lastIndexOf("/") + 1));
            out.putNextEntry(entry);
            int count;

            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }

            origin.close();
            out.close();

            upload(zipFileName);

            // Time Metric
            timeMetric.add(System.currentTimeMillis());

            // Size Metric
            File uncompressed = new File(file);
            int uncompressed_file_size = Integer.parseInt(String.valueOf(uncompressed.length()/1024));
            File compressed = new File(zipFileName);
            int compressed_file_size = Integer.parseInt(String.valueOf(compressed.length()/1024));
            sizeMetric.add(uncompressed_file_size);
            System.out.println("Uncompressed: " + uncompressed_file_size);
            sizeMetric.add(compressed_file_size);
            System.out.println("Compressed: " + compressed_file_size);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            String filename = timeFile.getAbsolutePath() + "/" + "times_" + currentDateandTime + ".txt";
            FileOutputStream fout = new FileOutputStream(filename);
            PrintWriter writer = new PrintWriter(fout);

            for(int i = 0; i < timeMetric.size(); ++i){
                writer.printf(timeMetric.get(i) + "\n");
            }

            writer.close();
            fout.close();

            currentDateandTime = sdf.format(new Date());

            String filename1 = sizeFile.getAbsolutePath() + "/" + "sizes_" + currentDateandTime + ".txt";
            FileOutputStream fout1 = new FileOutputStream(filename1);
            PrintWriter writer1 = new PrintWriter(fout1);

            for(int i = 0; i < sizeMetric.size(); ++i){
                writer1.printf(sizeMetric.get(i) + "\n");
            }

            writer1.close();
            fout1.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void upload(String zipName){
        JSONObject obj = null;
        try {
            obj = (JSONObject) arr.get(Integer.parseInt(app_id) - 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String author_id = null;
        try {
            author_id = obj.getString("author_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = OPENIVN + "/api/v1/translate/?make=" + make + "&model=" + model + "&year=" + year + "&app_id=" + app_id + "&user_id=" + author_id;
        Log.d("debug", url);
        HttpResponse<String> jsonResponse = null;
        Unirest.setTimeouts(0, 0);
        try{
            jsonResponse  = Unirest.post(url)
                    // .header("Content-Type", "application/zip")
                    .header("x-api-key", API_KEY)
                    .field("zip_file", new File(zipName)).asString();
            System.out.println(jsonResponse.getStatus());
        }
        catch (UnirestException e) {
            Log.e("post(): %s", e.getMessage());
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(AppDisplay.this, MainActivity.class));
        finish();
    }
}
