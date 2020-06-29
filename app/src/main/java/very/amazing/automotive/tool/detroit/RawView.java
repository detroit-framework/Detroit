package very.amazing.automotive.tool.detroit;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.os.SystemClock;
import android.widget.Chronometer;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class RawView extends Activity {

    String OPENIVN = "SERVER-URL:1609";
    String API_KEY = "XYZ";

    private Recorder recorder;
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;
    private List<String> events;
    private int index;
    String make, model, year;
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    private TextView textView9;
    Button button_done;
    String folderRE = "/storage/emulated/0/Android/data/very.amazing.automotive.tool.detroit/files/reverse-engineering-traces/";
    String obd_folder = "/storage/emulated/0/torqueLogs/"; // Replace if Torque is not used for OBD-II data

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.raw_view);
        chronometer = findViewById(R.id.chronometer);
        Intent intent = getIntent();
        make = intent.getStringExtra("MMY").split(" ")[0];
        model = intent.getStringExtra("MMY").split(" ")[1];
        year = intent.getStringExtra("MMY").split(" ")[2];

        recorder = new Recorder("0", make, model, year, this);
        events = Arrays.asList("Lock drivers side, LOCK_D",
                "Lock passengers side, LOCK_P",
                "Unlock drivers side, UNLOCK_D",
                "Unlock passengers side, UNLOCK_P",
                "Open trunk, T_OPEN",
                "Close trunk, T_CLOSE",
                "Drivers side door open, DOOR_OPEN_D",
                "Drivers side door close, DOOR_CLOSE_D",
                "Passengers side door open, DOOR_OPEN_P",
                "Passengers side door close, DOOR_CLOSE_P",
                "Left back door open, DOOR_OPEN_LB",
                "Left back door close, DOOR_CLOSE_LB",
                "Right back door open, DOOR_OPEN_RB",
                "Right back door close, DOOR_CLOSE_RB",
                "Drivers side window down, WINDOW_DOWN_D",
                "Drivers side window up, WINDOW_UP_D",
                "Passengers side window down, WINDOW_DOWN_P",
                "Passengers side window up, WINDOW_P_UP",
                "Left back window down, WINDOW_DOWN_LB",
                "Left back window up, WINDOW_UP_LB",
                "Right back window down, WINDOW_DOWN_RB",
                "Right back window up, WINDOW_UP_RB",
                "Turn on heating /ac fan, AC_FAN_ON",
                "Incremental fan speed increase, AC_FAN_SPEED_UP",
                "Increase temperature incrementally 65-75, TEMP_UP_65-75",
                "Decrease temperature incrementally 75-65, TEMP_DOWN_75-65",
                "Incremental fan speed decrease, AC_FAN_SPEED_DOWN",
                "Air circulation button on, AIR_CIRC_ON",
                "Air circulation button off, AIR_CIRC_OFF",
                "Honking horn, HORN_HONK",
                "Headlights off-on, HEADLIGHTS_OFF-ON",
                "Headlights on-off, HEADLIGHTS_ON-OFF",
                "Hazard lights on, HAZARDS_ON",
                "Hazard lights off, HAZARDS_OFF",
                "Windshield wipers once, WIPERS_SETTING_ONCE",
                "Windshield wipers setting 1, WIPERS_SETTING_0-1",
                "Windshield wipers setting 1-2, WIPERS_SETTING_1-2",
                "Windshield wipers setting 2-3, WIPERS_SETTING_2-3",
                "Interior lights all on, INT_LIGHTS_ON",
                "Interior lights all off, INT_LIGHTS_OFF",
                "Windshield wiper fluid, WIPERS_SETTING_FLUID",
                "Left turn signal on, TURN_ON_LEFT",
                "Left turn signal off, TURN_OFF_LEFT",
                "Right turn signal on, TURN_ON_RIGHT",
                "Right turn signal off, TURN_OFF_RIGHT",
                "Activate parking brake, PARKING_BRAKE_ACT",
                "Release parking brake, PARKING_BRAKE_REL",
                "Open hood, HOOD_OPEN",
                "Close hood, HOOD_CLOSE",
                "Drivers side mirror left right up down, MIRROR_D",
                "Passengers side mirror left right up down, MIRROR_P",
                "Buckle driver, BUCKLE_DRIVER",
                "Unbuckle driver, UNBUCKLE_DRIVER");
        index = 0;

        TextView textView = findViewById(R.id.textView6);
        textView.setText(Integer.toString(events.size()) + " events to be recorded");
    }

    public void start(View v) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();

            running = true;
            recorder.startRecordingPhase2(this, events.get(index));
            index++;

            TextView textView = findViewById(R.id.textView6);
            textView.setText(Integer.toString(events.size()-1) + " events left to be recorded");

            TextView textView5 = findViewById(R.id.textView5);
            textView5.setText(events.get(index));
        }
        else{
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
            recorder.stopRecordingPhase2(this);
        }
    }

    public void stop(View v) throws IOException {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }

        if(recorder.isRecording()) {
            recorder.stopRecordingPhase2(this);

            Log.d("mertdebug", "choosing file now");
            File obd_csv = getLastModified(obd_folder);
            File destination = new File(folderRE + obd_csv.getName());

            FileChannel in = new FileInputStream(obd_csv).getChannel();
            FileChannel out = new FileOutputStream(destination).getChannel();

            try {
                in.transferTo(0, in.size(), out);
            } catch(Exception e){
                e.printStackTrace();
            } finally {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            }

            Thread uploadRE = new Thread(new Runnable() {
                @Override
                public void run() {
                    String zipFile = folderRE + make + model + year + ".zip";
                    zipRE(folderRE, zipFile);
                }
            });
            uploadRE.start();
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textView9= (TextView) findViewById(R.id.textView9);
        progressBar.setVisibility(View.VISIBLE);
        textView9.setVisibility(View.VISIBLE);
        button_done = findViewById(R.id.button_done);
        button_done.setOnClickListener(done);

        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                            textView9.setText("Reverse Engineering. Wait until finished and then click DONE: " + (progressStatus*100/progressBar.getMax()) + "%");
                        }
                    });
                    try {
                        // We want to wait 5 minutes before we return to home screen. Sleep for 3 seconds on each iteration.
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    View.OnClickListener done = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d("debug", "now going back to main activity");
            Intent i = new Intent();
            i.putExtra("RE_success", 1);
            setResult(RESULT_OK, i);
            finish();
        }
    };

    public static File getLastModified(String directoryFilePath)
    {
        File directory = new File(directoryFilePath);
        File[] files = directory.listFiles();
        long lastModifiedTime = Long.MIN_VALUE;
        File chosenFile = null;

        if (files != null)
        {
            for (int j = 0; j < files.length; j++)
            {
                if (files[j].lastModified() > lastModifiedTime)
                {
                    chosenFile = files[j];
                    lastModifiedTime = chosenFile.lastModified();
                }
            }
        }

        return chosenFile;
    }

    public void zipRE(String folder, String zipFileName) {
        try {
            int BUFFER = 8192;
            BufferedInputStream origin = null;
            File folderRE = new File(folder);
            String[] fileList = folderRE.list();

            FileOutputStream dest = new FileOutputStream(zipFileName);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            byte data[] = new byte[BUFFER];

            for ( int i = 0 ; i < fileList.length ; i++  )
            {
                fileList[i] = folder + "/" + fileList[i];
            }

            Log.d("debug",  "now zipping");
            for(int i=0; i < fileList.length; i++) {
                Log.v("Compress", "Adding: " + fileList[i]);
                FileInputStream fi = new FileInputStream(fileList[i]);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(fileList[i].substring(fileList[i].lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
            out.close();

            Log.d("debug", "now uploading");
            uploadRE(zipFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadRE(String zipName){
        String url = OPENIVN + "/api/v1/generateDBC/?make=" + make + "&model=" + model + "&year=" + year + "&wheelbase=112.0";
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

    public void next(View v) {
        if(running) {
            TextView textView6 = findViewById(R.id.textView6);
            textView6.setText(Integer.toString(events.size()-index-1) + " events left to be recorded");

            TextView textView5 = findViewById(R.id.textView5);
            textView5.setText(events.get(index));

            recorder.startRecordingPhase2(this, events.get(index));
            recorder.clear();

            index++;
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(RawView.this, MainActivity.class));
        finish();
    }
}
