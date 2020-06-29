package very.amazing.automotive.tool.detroit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Permissions extends Activity {

    String OPENIVN = "SERVER-URL:1609";
    String API_KEY = "XYZ";

    Button val, inVal;
    TextView title;
    String streaming, make, model, year, wheelbase, author_id, app_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permissions);

        String url = OPENIVN + "/api/v1/apps/" + getIntent().getStringExtra("appNum");
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONObject permissions = response.getJSONObject("permissions");
                            String list = "";

                            for(int i = 0; i < permissions.names().length(); ++i){
                                if(permissions.getString(permissions.names().getString(i)) == "true"){
                                    list = list + permissions.names().getString(i) + '\n';
                                }
                            }

                            TextView tv = findViewById(R.id.permList);
                            tv.setText(list);
                            tv.setMovementMethod(new ScrollingMovementMethod());
                        }
                        catch(JSONException e){
                            Log.e("JSON", e.toString());
                        }
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

        streaming = getIntent().getStringExtra("streaming");
        make = getIntent().getStringExtra("make");
        model = getIntent().getStringExtra("model");
        year = getIntent().getStringExtra("year");
        wheelbase = getIntent().getStringExtra("wheelbase");
        app_id = getIntent().getStringExtra("appNum");
        author_id = getIntent().getStringExtra("author_id");

        // Displays validated permissions
        title = findViewById(R.id.permTitle);
        title.setText(getIntent().getStringExtra("name") + " Permissions\n"
                + getIntent().getStringExtra("author") + "\n"
                + getIntent().getStringExtra("desc")
                + getIntent().getStringExtra("streaming"));

        TextView authDesc = findViewById(R.id.author_desc);
        authDesc.setText("Author: "+ getIntent().getStringExtra("author") + "\n"
                + "Description: " + getIntent().getStringExtra("desc") + "\n"
                + "Online Mode:" + getIntent().getStringExtra("streaming"));

        getIntent().removeExtra("streaming");
        getIntent().removeExtra("make");
        getIntent().removeExtra("model");
        getIntent().removeExtra("year");
        getIntent().removeExtra("wheelbase");
        getIntent().removeExtra("author_id");
        getIntent().removeExtra("name");
        getIntent().removeExtra("author");
        getIntent().removeExtra("desc");

        wireUI();
    }

    View.OnClickListener loadVal = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (streaming.equals("0")) {
                Intent intent = new Intent();
                intent.putExtra("buttonNum", getIntent().getStringExtra("appNum"));
                setResult(100, intent);
                finish();

                getIntent().removeExtra("appNum");

                Toast.makeText(Permissions.this, "Recording offline data", Toast.LENGTH_LONG).show();
            }
            else{
                Intent intent = new Intent();
                intent.putExtra("buttonNum", getIntent().getStringExtra("appNum"));
                setResult(200, intent);
                finish();

                getIntent().removeExtra("appNum");

                Toast.makeText(Permissions.this, "Recording online data", Toast.LENGTH_LONG).show();
            }
        }
    };

    View.OnClickListener loadInVal = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(Permissions.this, AppDisplay.class);
            startActivity(i);
        }
    };

    void wireUI(){
        val = findViewById(R.id.valYes);
        val.setOnClickListener(loadVal);

        inVal = findViewById(R.id.valNo);
        inVal.setOnClickListener(loadInVal);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(Permissions.this, AppDisplay.class));
        finish();
    }
}
