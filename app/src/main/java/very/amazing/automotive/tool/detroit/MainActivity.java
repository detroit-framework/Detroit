package very.amazing.automotive.tool.detroit;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.openxc.VehicleManager;
import com.openxc.messages.DiagnosticResponse;
import com.openxc.messages.VehicleMessage;
import com.openxc.messages.CanMessage;

public class MainActivity extends Activity {

    String OPENIVN = "SERVER-URL:1609";
    String API_KEY = "XYZ";

    ArrayList<VehicleMessage> msgs;
    Button loadAppDis, loadColData;
    String make, model, year;
    boolean dbc_present;
    private VehicleManager mVehicleManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        RequestQueue queue = Volley.newRequestQueue(this);
        //String VIN = getVIN();
        String VIN = "ABCDEFGHIJKLMNOPQ"; // VIN can also be hardcoded.
        String url_vin = OPENIVN + "/api/v1/vin/" + VIN;

        JsonObjectRequest getRequest_vin = new JsonObjectRequest(Request.Method.GET, url_vin, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        try {
                            make = response.getString("make");
                            model = response.getString("model");
                            year = response.getString("year");
                            dbc_present = response.getBoolean("dbc_available");
                            TextView textView = findViewById(R.id.textView);
                            String detectedVehicle = "Vehicle Model: " + make + " " + model + " " + year;
                            textView.setText(detectedVehicle);
                            TextView textView8 = findViewById(R.id.textView8);
                            if (dbc_present == true) {
                                textView8.setText("Vehicle Model in Database!");
                                loadAppDis.setVisibility(View.VISIBLE);
                            } else {
                                textView8.setText("Vehicle Model not in Database, please Reverse Engineer!");
                                loadColData.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            Log.e("error", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("x-api-key", API_KEY);
                return headers;
            }
        };

        queue.add(getRequest_vin);
        wireUI();
    }


    View.OnClickListener loadApps = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(MainActivity.this, AppDisplay.class);
            i.putExtra("MMY", make + " " + model + " " + year);
            startActivity(i);

        }
    };

    View.OnClickListener loadCol = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(MainActivity.this, FreeDriving.class);
            i.putExtra("MMY", make + " " + model + " " + year);
            startActivityForResult(i, 2);

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                int re_success = data.getIntExtra("RE_success",0);
                if (re_success == 1) {
                    TextView textView8 = findViewById(R.id.textView8);
                    textView8.setText("Vehicle Model in Database!");
                    loadAppDis.setVisibility(View.VISIBLE);
                    loadColData.setVisibility(View.INVISIBLE);
                }
            }
        }
    }


    void wireUI(){
        loadAppDis = findViewById(R.id.loadAppDis);
        loadAppDis.setOnClickListener(loadApps);

        loadColData = findViewById(R.id.loadColData);
        loadColData.setOnClickListener(loadCol);
    }

    // Support and success depends on OpenXC dongle FW and vehicle
    public String getVIN() {
        byte[] data = {0x02, 0x09, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00};
        CanMessage message = new CanMessage(1, 0x7DF, data);

        VehicleMessage response = mVehicleManager.request(message);

        if (response != null) {
            DiagnosticResponse diagnosticResponse = response.asDiagnosticResponse();

            byte[] responseData = response.asCanMessage().getData();

            byte[] data2 = {0x30, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
            int newID = response.asCanMessage().getId() - 8;

            CanMessage message2 = new CanMessage(0, newID, data2);

            VehicleMessage response2 = mVehicleManager.request(message2);
            byte[] responseData2 = response2.asCanMessage().getData();

            int d1, d2, d3, d4, d5, d6, d7;
            d1 = responseData2[1];
            d2 = responseData2[2];
            d3 = responseData2[3];
            d4 = responseData2[4];
            d5 = responseData2[5];
            d6 = responseData2[6];
            d7 = responseData2[7];

            return Integer.toString(d1) + Integer.toString(d2) + Integer.toString(d3) + Integer.toString(d4) + Integer.toString(d5) + Integer.toString(d6) + Integer.toString(d7);
        }
        else{
            Log.v("VIN", "null response!");
            return null;
        }
    }

    public void addData(VehicleMessage msg){
        msgs.add(msg);
    }

    private VehicleMessage.Listener mListener = new VehicleMessage.Listener() {
        @Override
        public void receive(final VehicleMessage message) {
            addData(message);
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Log.i("VM", "Bound to VehicleManager");

            mVehicleManager = ((VehicleManager.VehicleBinder) service).getService();

            mVehicleManager.addListener(CanMessage.class, mListener);
        }
        public void onServiceDisconnected(ComponentName className) {
            Log.w("VM", "VehicleManager Service  disconnected unexpectedly");
            mVehicleManager = null;
        }
    };

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}

