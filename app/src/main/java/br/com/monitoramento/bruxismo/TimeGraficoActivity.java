package br.com.monitoramento.bruxismo;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MenuItem;

        import android.graphics.Color;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.SeekBar;
        import android.widget.SeekBar.OnSeekBarChangeListener;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.github.mikephil.charting.charts.LineChart;
        import com.github.mikephil.charting.components.AxisBase;
        import com.github.mikephil.charting.components.Legend;
        import com.github.mikephil.charting.components.XAxis;
        import com.github.mikephil.charting.components.YAxis;
        import com.github.mikephil.charting.components.YAxis.AxisDependency;
        import com.github.mikephil.charting.data.Entry;
        import com.github.mikephil.charting.data.LineData;
        import com.github.mikephil.charting.data.LineDataSet;
        import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.sql.Time;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.List;
        import java.util.concurrent.TimeUnit;

        import br.com.monitoramento.bruxismo.client.GetLeitura.GetLeitura;
        import br.com.monitoramento.bruxismo.client.GetLeitura.GetLeituraResponse;

public class TimeGraficoActivity extends DemoBase implements
        OnChartValueSelectedListener {

    private LineChart mChart;
    private long startTime;
    private long stopTime ;
    private long elapsedTime ;
    ProgressDialog pd;

    String lText;
    String messageStr="send";
    int msg_length=messageStr.length();
    private static final int UDP_SERVER_PORT = 4210;
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


        runThread();
//        new JsonTask().execute("http://192.168.4.1/edit");


    }


    public void setInfo(GetLeituraResponse info) {
//        Context contexto = getApplicationContext();
//        for(String s : info.valor)
//            addEntry(Float.parseFloat(s));

        //mChart.invalidate();
//        stopTime = System.currentTimeMillis();
//        elapsedTime = stopTime - startTime;
//        //Toast.makeText(contexto, info.valor, Toast.LENGTH_SHORT).show();
//        Log.e("Tempo Exeução", String.valueOf(elapsedTime));
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
            data.addEntry(new Entry(set.getEntryCount(), valor/12), 0);
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
                while (true) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                new JsonTask().execute("http://192.168.4.1/mestrado/json3");
//                                new GetLeitura(TimeGraficoActivity.this);
                                startTime = System.currentTimeMillis();
                                getUDP();
                            }
                        });
                        Thread.sleep(1000*1/5);
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
//            InetAddress local = InetAddress.getByName("192.168.4.1");
//            DatagramPacket p = new DatagramPacket(message, msg_length,local,UDP_SERVER_PORT);
//            ds.send(p);

            ds.receive(dp);
            Log.i("UDP packet received", new String(dp.getData()));
            addEntry(Float.parseFloat(new String(dp.getData())));


//            lText = new String(dp.getData());
//            int index1 = lText.indexOf(":[");
//            int index2 = lText.indexOf("]}");
//            lText = lText.substring(index1+2,index2);
//            String buffer = "";
//            for(char res : lText.toCharArray()){
//                if(res != ',')
//                    buffer += res;
//                else{
//                    addEntry(Float.parseFloat(buffer));
//                    buffer = "";
//                }
//            }
            stopTime = System.currentTimeMillis();
            elapsedTime = stopTime - startTime;
            Log.e("Tempo Exeução", String.valueOf(elapsedTime));
        }catch (SocketException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            ds.close();
        }
    }

private class JsonTask extends AsyncTask<String, String, String> {

    protected void onPreExecute() {
        super.onPreExecute();

//            pd = new ProgressDialog(TimeGraficoActivity.this);
//            pd.setMessage("Please wait");
//            pd.setCancelable(false);
//            pd.show();
    }

    protected String doInBackground(String... params) {


        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();


            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
                Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
            }
            //Log.d("Ver se funfa: ", buffer.cep.toString());   //here u ll get whole response...... :-)

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

//        txtJson.setText(result);
        int index1 = result.indexOf(":[");
        int index2 = result.indexOf("]}");
        result = result.substring(index1+2,index2);
        String buffer = "";
        for(char res : result.toCharArray()){
            if(res != ',')
                buffer += res;
            else{
                //Log.d("Valores Partidos: ", buffer);
                //Log.d("Valor Gráfico", buffer);
                addEntry(Float.parseFloat(buffer));
                buffer = "";
            }
        }
        //stopTime = System.currentTimeMillis();
        //elapsedTime = stopTime - startTime;
        //Log.d("Tempo de Exibição", String.valueOf(elapsedTime));
//        if (pd.isShowing()){
//            pd.dismiss();
//        }
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
