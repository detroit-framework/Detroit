package very.amazing.automotive.tool.detroit;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.content.Intent;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.openxc.VehicleManager;
import com.openxc.messages.VehicleMessage;
import com.openxc.messages.CanMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static java.lang.StrictMath.abs;


public class Recorder extends Activity {

    String OPENIVN = "SERVER-URL:1609";
    String OPENIVN_IP = "STATIC-IP";
    int OPENIVN_TCP_PORT = 1612;
    String API_KEY = "XYZ";

    ArrayList<Integer> busIDs;
    ArrayList<Integer> canIDs;
    ArrayList<byte[]> data;
    ArrayList<Long> timestamps;
    String appId;
    boolean recording = false;
    boolean onlineMode = false;
    private VehicleManager mVehicleManager;
    private Context context;
    String make, model, year;

    // For benchmarking online mode
    String url = OPENIVN + "/api/v1/messages/";
    RequestQueue queue;
    JsonObjectRequest getRequest;
    int numberCANMessages = 0;
    String outputTCP;
    Socket socket;
    String receivedMessage = "";
    long online_duration = 0;
    int frame_length = 0;

    File file;
    String TAG = "Recorder";
    String folder = "event-traces";
    String folder_RE = "reverse-engineering-traces";
    String filename = "";
    FileOutputStream out;
    PrintWriter writer;

    String filenameOn = "";
    FileOutputStream foutOn;
    PrintWriter writerOn;

    Runnable httplisten = new Runnable() {
        @Override
        public void run() {
            try {
                while (isRecording()) {
                    getRequest = new JsonObjectRequest(Request.Method.GET, url + appId, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    receivedMessage = response.toString();
                                    Log.d("debug", "Received message from server: " + receivedMessage);
                                    try{
                                        receivedMessage = response.getString("message");
                                        Log.d("debug", "Received message from server: " + receivedMessage);

                                        Intent i = new Intent(context, PopUp.class);
                                        i.putExtra("myMsg", receivedMessage);
                                        context.startActivity(i);
                                    }
                                    catch(JSONException e){
                                        System.out.println(e.toString());
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

                    // Poll every 5 seconds
                    Thread.sleep(5000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public Recorder(String appIdIn, String makeIn, String modelIn, String yearIn, Context context){
        this.context = context;
        mVehicleManager = null;
        appId = appIdIn;
        make = makeIn;
        model = modelIn;
        year = yearIn;
    }

    public ArrayList<byte[]> getData() {
        return data;
    }

    public ArrayList<Integer> getBusIDs() {
        return busIDs;
    }

    public ArrayList<Integer> getCanIDs(){ return canIDs;};

    public ArrayList<Long> getTimestamps() {
        return timestamps;
    }

    /**
     * If it is currently recording, it will add this data.
     * @param msg Received vehiclemessage
     */
    public void addData (VehicleMessage msg) {
        if(isRecording() && !isOnlineMode()){
            try {
                String stringData = "";
                byte [] myMsg = msg.asCanMessage().getData();
                for(int j = 0; j < myMsg.length; ++j){
                    Integer b = abs(myMsg[j]);
                    if(b < 16){
                        stringData = stringData + "0" + Integer.toHexString(b);
                    }
                    else{
                        stringData += Integer.toHexString(b);
                    }
                }

                String timeStamp = Long.toString(msg.asCanMessage().getTimestamp());
                timeStamp = timeStamp.substring(0, 10) + "." + timeStamp.substring(10);

                writer.printf("%s",  timeStamp + "," + msg.asCanMessage().getId() +
                        "," + msg.asCanMessage().getBusId() + "," + stringData + "\n");
            }
            catch(Exception e){
                System.out.println(e.toString());
            }
        }
        else if (isRecording() && isOnlineMode()) {
            byte[] myMsg = msg.asCanMessage().getData();
            String stringData = "";
            for (int j = 0; j < myMsg.length; ++j) {
                Integer b = abs(myMsg[j]);
                if (b < 16) {
                    stringData = stringData + "0" + Integer.toHexString(b);
                } else {
                    stringData += Integer.toHexString(b);
                }
            }

            String timeStamp = Long.toString(msg.asCanMessage().getTimestamp());
            timeStamp = timeStamp.substring(0, 10) + "." + timeStamp.substring(10);

            if (numberCANMessages == 0) {
                // Do nothing
            }
            else {
                outputTCP = timeStamp + "," + msg.asCanMessage().getId() + "," + msg.asCanMessage().getBusId() + "," + stringData + ",";
                Thread tcpthread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sendToServer(outputTCP, socket);
                        } catch (Exception e) {
                            StringWriter errors = new StringWriter();
                            e.printStackTrace(new PrintWriter(errors));
                            String hier2 = errors.toString();
                        } catch (Throwable th) {
                            StringWriter errors = new StringWriter();
                            th.printStackTrace(new PrintWriter(errors));
                            String hier3 = errors.toString();
                        }
                    }
                });
                tcpthread.start();
            }
            numberCANMessages++;
        }
        else{
            return;
        }
    }

    // Method to send data to TCP Server
    public void sendToServer(String nachricht, Socket socket) throws SocketException, IOException, Exception, Throwable
    {
        long before_tcp = System.currentTimeMillis();
        try
        {
            PrintWriter outToServer = new PrintWriter(
                    new OutputStreamWriter(
                            socket.getOutputStream()));
            outToServer.print(nachricht + "\n");
            outToServer.flush();
            online_duration += System.currentTimeMillis() - before_tcp;
            frame_length += nachricht.length() + 20 + 5 + 18;
        }
        catch (UnknownHostException e) {
            System.out.print(e.toString());
        } catch (IOException e) {
            System.out.print(e.toString());
        }catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public boolean isRecording(){
        return recording;
    }

    public boolean isOnlineMode() {
        return onlineMode;
    }

    public void startRecording(Activity a, int index){
        if(mVehicleManager == null){
            Log.v("Record", "new event");
            Intent intent = new Intent(a, VehicleManager.class);
            a.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }

        recording = true;

        if(index == 0) {
            onlineMode = false;
            file = a.getExternalFilesDir(folder);
            file.mkdirs();
            try{
                Log.i(TAG, "Opening file " + filename);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());

                filename = file.getAbsolutePath() + "/" + "data_" + currentDateandTime + ".txt";
                out = new FileOutputStream(filename);
                writer = new PrintWriter(out);
            }
            catch(Exception e){
                System.out.println(e.toString());
            }
            Log.v("Record", Environment.getExternalStorageDirectory().getPath());
            Log.i(TAG, "Creating folder " + folder);
        }
        else if (index == 1) {
            Log.d("debug", "yeah, we in online mode.");
            try{
                onlineMode = true;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());
                filenameOn = AppDisplay.timeFile.getAbsolutePath() + "/" + "times_" + currentDateandTime + ".txt";
                foutOn = new FileOutputStream(filenameOn);
                writerOn = new PrintWriter(foutOn);
            }
            catch(Exception e){
                System.out.println(e.toString());
            }
            try
            {
                socket = new Socket(OPENIVN_IP, OPENIVN_TCP_PORT);
            }
            catch(Exception e)
            {
                System.out.print("Could not create TCP socket.");
                System.out.print(e.getLocalizedMessage());
                System.out.print("\n");
            }
            outputTCP = appId + "," + make + "_" + model + "_" + year;
            Log.d("debug", "We are sending the first entry.");
            try {
                sendToServer(outputTCP, socket);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

            // Now let us start a listener thread for incoming messages from backend
            Log.d("debug", "Initializing HTTP GET request.");
            try {
                queue = Volley.newRequestQueue(this.context);
                Log.d("debug", "HTTP request queue created!");
                new Thread(httplisten).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void clear(){
        timestamps = new ArrayList<>();
        busIDs = new ArrayList<>();
        canIDs = new ArrayList<>();
        data = new ArrayList<>();
    }

    public void stopRecording(Activity a){
        if(mVehicleManager != null) {
            Log.i(TAG, "Unbinding from Vehicle Manager");
            mVehicleManager.removeListener(VehicleMessage.class, mListener);
            a.unbindService(mConnection);
            mVehicleManager = null;
        }

        AppDisplay.timeMetric.add(System.currentTimeMillis());
        AppDisplay.timeMetric.add(Long.valueOf(numberCANMessages));
        recording = false;
        if (onlineMode) {
            try {
                socket.close();
                Log.d("debug", "Closed socket successfully and transmitted " + numberCANMessages+ " for " + online_duration + "ms.");
                writerOn.printf(online_duration + "\n");
                writerOn.printf(frame_length + "\n");
                writerOn.printf(numberCANMessages + "\n");
                writerOn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void startRecordingPhase1(Activity a){
        if(mVehicleManager == null){
            Log.v("Record", "Phase 1 Reverse Engineering Data");
            Intent intent = new Intent(a, VehicleManager.class);
            a.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }

        recording = true;
        onlineMode = false;
        file = a.getExternalFilesDir(folder_RE);
        file.mkdirs();
        try{
            Log.i(TAG, "Opening file " + filename);

            filename = file.getAbsolutePath() + "/" + "Phase1.txt";
            out = new FileOutputStream(filename);
            writer = new PrintWriter(out);
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        Log.v("Record", Environment.getExternalStorageDirectory().getPath());
    }

    public void stopRecordingPhase1(Activity a){
        if(mVehicleManager != null) {
            Log.i(TAG, "Unbinding from Vehicle Manager");
            mVehicleManager.removeListener(VehicleMessage.class, mListener);
            a.unbindService(mConnection);
            mVehicleManager = null;
        }
        recording = false;
    }

    public void startRecordingPhase2(Activity a, String event){
        if(mVehicleManager == null){
            Log.v("Record", "Phase 2 Reverse Engineering Data");
            Intent intent = new Intent(a, VehicleManager.class);
            a.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }

        recording = true;
        onlineMode = false;
        file = a.getExternalFilesDir(folder_RE);
        file.mkdirs();
        try{
            Log.i(TAG, "Opening file " + filename);

            filename = file.getAbsolutePath() + "/" + event + ".txt";
            out = new FileOutputStream(filename);
            writer = new PrintWriter(out);
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        Log.v("Record", Environment.getExternalStorageDirectory().getPath());
    }

    public void stopRecordingPhase2(Activity a){
        if(mVehicleManager != null) {
            Log.i(TAG, "Unbinding from Vehicle Manager");
            mVehicleManager.removeListener(VehicleMessage.class, mListener);
            a.unbindService(mConnection);
            mVehicleManager = null;
        }
        recording = false;
    }

    public String next(String event){
        try {
            Log.i(TAG, "Opening file " + filename);

            if(!isOnlineMode()){
                writer.close();
                out.close();
            }
            else{
                writerOn.close();
                foutOn.close();
            }
            AppDisplay.timeMetric.add(System.currentTimeMillis());
        }
        catch (IOException e) {
            Log.e(TAG, "Error opening file!");
        }
        return filename;
    }


    private VehicleMessage.Listener mListener = new VehicleMessage.Listener() {
        @Override
        public void receive(final VehicleMessage message) {
            Thread receivethread = new Thread(new Runnable() {
                @Override
                public void run() {
                    addData(message);
                }
            });
            receivethread.start();
        }
    };


    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Log.i(TAG, "Bound to VehicleManager");

            mVehicleManager = ((VehicleManager.VehicleBinder) service).getService();

            mVehicleManager.addListener(CanMessage.class, mListener);
        }
        public void onServiceDisconnected(ComponentName className) {
            Log.w(TAG, "VehicleManager Service  disconnected unexpectedly");
            mVehicleManager = null;
        }
    };
}