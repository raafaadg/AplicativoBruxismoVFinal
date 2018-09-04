package br.com.monitoramento.bruxismo;


import android.app.ProgressDialog;
import android.os.CountDownTimer;
import android.util.Log;


import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class TimeGrafico2Activity extends DemoBase implements
        OnChartValueSelectedListener {

    private LineChart mChart;
    private boolean calibrar = false;
    public boolean controlThread2 = true;
    private int ref = 0;
    private int count = 0;
    private int media = 0;
    String messageStr="send";
    private static final int UDP_SERVER_PORT = 4200;
    private static final int MAX_UDP_DATAGRAM_LEN = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_linechart_time);


        mChart = findViewById(R.id.chart1);

        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        // set an alternative background color
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setViewPortOffsets(0f, 0f, 0f, 0f);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add data
        mChart.setData(data);
        //mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
        l.setEnabled(false);
        l.setTypeface(mTfLight);
        l.setTextColor(Color.WHITE);

        XAxis xl = mChart.getXAxis();
        xl.setTypeface(mTfLight);
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setCenterAxisLabels(true);
        xl.setEnabled(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setTypeface(mTfLight);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMaximum(100f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        controlThread2 = true;
        runThread();
//        new JsonTask().execute("http://192.168.4.1/edit");
        mChart.invalidate();

        final ProgressDialog progressDialog = new ProgressDialog(TimeGrafico2Activity.this);
        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Calibrando valor de Referência");
        progressDialog.setMessage("Favor esperar calibração. NÃO REALIZAR nenhuma mordida!!");
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setMax(100);
        progressDialog.setProgress(0);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                int progress = 0;
                calibrar = true;
                while (progress<= 100){

                    try {
                        progressDialog.setProgress(progress);
                        progress++;
                        Thread.sleep(100);


                    }catch (Exception ex){
                        ex.getMessage();
                    }

                }
                progressDialog.dismiss();
                media = ref/count;
                Log.v("media",String.valueOf(media));
                TimeGrafico2Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(
                                TimeGrafico2Activity.this,
                                "Calibração Concluida ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        t.start();
        progressDialog.show();
    }


    private void addEntry(float valor) {

        LineData data = mChart.getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

//            data.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 40) + 30f), 0);
            data.addEntry(new Entry(set.getEntryCount(), valor), 0);
            data.notifyDataChanged();

            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();

            // limit the number of visible entries
            mChart.setVisibleXRangeMaximum(1000);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            mChart.moveViewToX(data.getEntryCount());

            // this automatically refreshes the chart (calls invalidate())
            // mChart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
            mChart.setData(data);

        }
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(AxisDependency.RIGHT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.BLUE);
        set.setLineWidth(2f);
        set.setCircleRadius(1f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    private Thread thread;

    private void runThread() {

        new Thread() {
            public void run() {
                while (controlThread2) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getUDP();
                                if(calibrar){
                                    ref+=ref;
                                    count++;
                                }
                                //new ClientSendAndListen().run();
                            }
                        });
                        Thread.sleep(1000/4);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    void getUDP(){
        byte[] lMsg = new byte[MAX_UDP_DATAGRAM_LEN];
        byte[] message = messageStr.getBytes();
        DatagramPacket dp = new DatagramPacket(lMsg, lMsg.length);
        DatagramSocket ds = null;

        try {
            ds = new DatagramSocket(UDP_SERVER_PORT);
            ds.receive(dp);
            Log.i("UDP packet received", new String(dp.getData()));
            addEntry(Float.parseFloat(new String(dp.getData())));
        }catch (SocketException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            ds.close();
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (thread != null) {
            thread.interrupt();
        }
    }

}
