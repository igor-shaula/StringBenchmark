package com.igor.shaula.omni_logger;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private boolean isJobRunning;

    private EditText etIterationsNumber;
    private TextView tvExplanationForTheFAB;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etIterationsNumber = findViewById(R.id.tiedNumberOfIterations);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvExplanationForTheFAB = findViewById(R.id.tvExplanationForTheFAB);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isJobRunning) {
                    stopCurrentJob();
                } else {
                    startNewJob();
                }
            }
        });
    }

    private void stopCurrentJob() {
        interruptPerformanceTest();
        isJobRunning = false;
        etIterationsNumber.setEnabled(true);
        tvExplanationForTheFAB.setText(R.string.explanationForReadyFAB);
        fab.setImageResource(android.R.drawable.ic_media_play);
    }

    private void startNewJob() {
        runPerformanceAppraisal();
        isJobRunning = true;
        etIterationsNumber.setEnabled(false);
        tvExplanationForTheFAB.setText(R.string.explanationForBusyFAB);
        fab.setImageResource(android.R.drawable.ic_media_pause);
    }

    private void interruptPerformanceTest() {

    }

    private void runPerformanceAppraisal() {
                /*
                    String[] sourceArray = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
                    String longForTest = "";
                    {
                        long nanoTime = System.nanoTime();
                        System.out.println("before preparing the string for logger @ " + nanoTime);
                        for (int i = 0; i < 10_000; i++) {
                            for (String string : sourceArray) {
                                longForTest += string;
                            }
                        }
                        System.out.println("after preparing the string for logger @ " + (System.nanoTime() - nanoTime));
                        // 918760833 - for 1_000 iterations - almost 1 second
                        // 67333459506 - for 10_000 iterations - 67 seconds
                        // 67461031537 - for 10_000 iterations - 67 seconds
                    }
                    {
                        long nanoTime = System.nanoTime();
                        System.out.println("starting standard logger @ " + nanoTime);
                        Log.v("LOG", longForTest);
                        System.out.println("ending standard logger @ " + (System.nanoTime() - nanoTime));
                        // 186875 ns - for 1000 iterations
                        // 602605 ns - for 10_000 iterations
                        // 618177 ns - for 10_000 iterations
                    }
                    {
                        long nanoTime = System.nanoTime();
                        System.out.println("starting custom logger @ " + nanoTime);
                        VAL.v("VAL", longForTest);
                        System.out.println("ending custom logger @ " + (System.nanoTime() - nanoTime));
                        // 347604 ns - for 1000 iterations
                        // 1262656 ns - for 10_000 iterations
                        // 4720677 ns - for 10_000 iterations
                    }
                    VAL.v("" + getString(R.string.vero_test).length());
                    VAL.v("", "");
                    VAL.v("", "", "");
                    VAL.v();
                    VAL.v((String[]) null);
                    VAL.v(null, null);
                    VAL.v(null, null, null);
                    VAL.v(this.toString(), null, null);
                    VAL.v("1");
                    VAL.v("1", "2");
                    VAL.v("1", "2", "3");
                    VAL.v("", "", "", "");
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}