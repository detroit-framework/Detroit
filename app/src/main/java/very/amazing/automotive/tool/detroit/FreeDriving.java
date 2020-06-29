package very.amazing.automotive.tool.detroit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.os.SystemClock;
import android.widget.Chronometer;


public class FreeDriving extends Activity {

    private Recorder recorder;
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;
    String make, model, year;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.freedriving);
        chronometer = findViewById(R.id.chronometer2);
        Intent intent = getIntent();
        make = intent.getStringExtra("MMY").split(" ")[0];
        model = intent.getStringExtra("MMY").split(" ")[1];
        year = intent.getStringExtra("MMY").split(" ")[2];
        recorder = new Recorder("0", make, model, year, this);
    }

    View.OnClickListener startPhase1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            start(view);
        }
    };

    View.OnClickListener stopPhase1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            stop(view);
        }
    };

    public void start(View v) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();

            running = true;
            recorder.startRecordingPhase1(this);
        }
        else{
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
            recorder.stopRecordingPhase1(this);
        }
    }

    public void stop(View v) {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }

        if(recorder.isRecording()) {
            recorder.stopRecordingPhase1(this);
        }

        Intent i = new Intent(FreeDriving.this, RawView.class);
        i.putExtra("MMY", make + " " + model + " " + year);
        startActivityForResult(i, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                int re_success = data.getIntExtra("RE_success",0);

                Intent i = new Intent();
                i.putExtra("RE_success", re_success);
                setResult(RESULT_OK, i);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(FreeDriving.this, MainActivity.class));
        finish();
    }
}
