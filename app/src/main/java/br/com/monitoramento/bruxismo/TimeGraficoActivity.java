package br.com.monitoramento.bruxismo;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;
import static android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE;

public class TimeGraficoActivity extends DemoBase implements
        OnChartValueSelectedListener {

    private LineChart mChart;
    String messageStr="send";
    private static final int UDP_SERVER_PORT = 4200;
    private static final int MAX_UDP_DATAGRAM_LEN = 10;
    public boolean controlThread = true;
    ProgressDialog pd;
    public TextView tv_ongra;
    public String urll = "http://192.168.4.1/mestrado/offline";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_linechart_time);

        tv_ongra = findViewById(R.id.tv_ongra);
        tv_ongra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(TimeGraficoActivity.this);
                pd.setMessage("Aguarde reconexão com o dispositivo.");
                pd.setCancelable(false);
                pd.show();
                controlThread = false;
                Thread.interrupted();
                Log.v("run","ESSA PORRA TEM QUE PARAR AGORAAAA!!!");
                Log.v("run",String.valueOf(controlThread));
                //new SendRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                tryHTTP("http://192.168.4.1/mestrado/offline");
            }
        });
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

        controlThread = true;
        runThread();
//        new JsonTask().execute("http://192.168.4.1/edit");
        mChart.invalidate();

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
    //@Override
    //public void onDestroy() {
    //    controlThread = false;
    //    Thread.interrupted();
    //    //tryHTTP("http://192.168.4.1/mestrado/offline");
    //    Log.v("run","ESSA PORRA TEM QUE PARAR AGORAAAA!!!");
    //    Log.v("run",String.valueOf(controlThread));
    //    //new SendRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    //    super.onDestroy();
    //}

    private class SendRequest extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(urll);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()) {
                pd.dismiss();
            }
            Toast.makeText(TimeGraficoActivity.this, result, Toast.LENGTH_LONG).show();
            startActivity(new Intent(TimeGraficoActivity.this, MainActivity.class));
            finish();
            Log.v("run","Fechou e foi pra main");
            //Toast.makeText(TimeGraficoActivity.this, result, Toast.LENGTH_LONG).show();

        }
    }


    public void tryHTTP(String url){
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest putRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.v("online","Modo online desativado");
                        if (pd.isShowing()) {
                            pd.dismiss();
                        }
                        Toast.makeText(TimeGraficoActivity.this, response, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(TimeGraficoActivity.this, MainActivity.class));
                        finish();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.v("online",error.toString());

                    }
                }
        );
        putRequest.setRetryPolicy(new DefaultRetryPolicy(
                40000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(putRequest);
    }


    @Override
    protected void onRestart() {
        Log.v("run","PODE VOLTAR A FUNFAR AGORAAAA!!!");
        super.onRestart();
    }

    private Thread thread;

    private void runThread() {

        new Thread() {
            public void run() {
                if (interrupted()){
                    //new SendRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    return;
                }
                while (controlThread) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getUDP();
                                Log.v("run","RODANDO!!!");
                                if (interrupted()){
                                    //new SendRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                    return;
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
