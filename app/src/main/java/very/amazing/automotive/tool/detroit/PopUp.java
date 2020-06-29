package very.amazing.automotive.tool.detroit;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PopUp extends Activity {
    Button done;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up);

        String msg = getIntent().getStringExtra("myMsg");
        TextView msgBox = findViewById(R.id.textView3);
        msgBox.setText(msg);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.4));

        wireUI();
    }


    View.OnClickListener loadDone = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    void wireUI(){
        done = findViewById(R.id.done_button);
        done.setOnClickListener(loadDone);
    }
}
