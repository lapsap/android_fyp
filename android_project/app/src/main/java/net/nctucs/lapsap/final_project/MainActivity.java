package net.nctucs.lapsap.final_project;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    TextView text_read, text_write;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTrace();

        handler.postDelayed(runnable, 1000);
    }

    @Override
    protected void onStop(){
        super.onStop();
        handler.removeCallbacks(runnable);
    }
    @Override
    protected void onPause(){
        super.onPause();
        handler.removeCallbacks(runnable);
    }
    @Override
    protected void onResume(){
        super.onResume();
        handler.postDelayed(runnable, 1000);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                Process p = Runtime.getRuntime().exec("su");
                DataOutputStream os = new DataOutputStream(p.getOutputStream());
                os.writeBytes("sh /data/lapsap/readtrace.sh \n");
                os.flush();
                os.writeBytes("exit\n");
                os.flush();
                BufferedReader stdInput = new BufferedReader(new
                        InputStreamReader(p.getInputStream()));

                // read the output from the command

                String la_write = null, la_read = null;
                la_write = stdInput.readLine();
                la_read = stdInput.readLine();
                System.out.println("Write " + la_write + " Read" + la_read);

                text_read = (TextView) findViewById(R.id.label_read);
                text_write = (TextView) findViewById(R.id.label_write);
                text_read.setText("Read : " + la_read);
                text_write.setText("Write : " + la_write);

            } catch (IOException e) {
                e.printStackTrace();
            }
            handler.postDelayed(this, 1000);
        }
    };

    public void initTrace(){
        try {
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            os.writeBytes("sh /data/lapsap/ontracer.sh \n");
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
