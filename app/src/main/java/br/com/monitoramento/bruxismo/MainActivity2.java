package br.com.monitoramento.bruxismo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

import br.com.monitoramento.bruxismo.client.GetLeitura.GetLeitura;
import br.com.monitoramento.bruxismo.client.GetLeitura.GetLeituraResponse;

public class MainActivity2 extends AppCompatActivity {

    TextView btnHit;
    TextView btnOff;
    TextView txtJson;
    ProgressDialog pd;
    String lText;
    String messageStr="send";
    int msg_length=messageStr.length();
    private static final int UDP_SERVER_PORT = 4210;
    private static final int MAX_UDP_DATAGRAM_LEN = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btnHit = (TextView) findViewById(R.id.btnHit);
        btnOff = (TextView) findViewById(R.id.btnOff);
        txtJson = (TextView) findViewById(R.id.tvJsonItem);

        btnHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new JsonTask().execute("http://192.168.4.1/edit");
//                new JsonTask().execute("http://192.168.4.1/mestrado/6");
//                new JsonTask().execute("http://192.168.4.1/mestrado/tele");
                new JsonTask().execute("http://192.168.4.1/mestrado/on");
//                new GetLeitura(MainActivity2.this);
//                runUdpServer()
            pd = new ProgressDialog(MainActivity2.this);
            pd.setMessage("AGUARDE! Realizando médias da Bateria");
            pd.setCancelable(false);
            pd.show();

            }
        });
        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JsonTask().execute("http://192.168.4.1/mestrado/off");
                pd = new ProgressDialog(MainActivity2.this);
                pd.setMessage("AGUARDE! Desligando GPIO4");
                pd.setCancelable(false);
                pd.show();
            }
        });
    }
    void runUdpServer(){
        byte[] lMsg = new byte[MAX_UDP_DATAGRAM_LEN];
        byte[] message = messageStr.getBytes();
        DatagramPacket dp = new DatagramPacket(lMsg, lMsg.length);
        DatagramSocket ds = null;
//                try{
//                    DatagramSocket s = new DatagramSocket(UDP_SERVER_PORT);
//                    InetAddress local = InetAddress.getByName("192.168.4.1");
//                    DatagramPacket p = new DatagramPacket(message, msg_length,local,UDP_SERVER_PORT);
//                    s.send(p);
//
//                    s.close();
//                }catch (SocketException e){
//                    e.printStackTrace();
//                }catch (IOException e){
//                    e.printStackTrace();
//                }

        try {
            //ds = new DatagramSocket(UDP_SERVER_PORT);
            ds = new DatagramSocket(UDP_SERVER_PORT);
//                    InetAddress local = InetAddress.getByName("192.168.4.1");
//                    DatagramPacket p = new DatagramPacket(message, msg_length,local,UDP_SERVER_PORT);
//                    ds.send(p);

            ds.receive(dp);
            lText = new String(dp.getData());
            Log.i("UDP packet received", lText);
            txtJson.setText(lText);

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

            /*pd = new ProgressDialog(MainActivity2.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();*/
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

//                line = reader.readLine();
//                txtJson.setText(line);
                //String total = "";
                while ((line = reader.readLine())  != null){
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
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

           // int index1 = result.indexOf(":");
           // int index2 = result.indexOf("}");
           // result = result.substring(index1+1,index2);
//            addEntry(Float.parseFloat(result));
            if (pd.isShowing()){
                pd.dismiss();
            }
            txtJson.setText(result);
        }
    }

    public void setInfo(GetLeituraResponse info) {
//        Context contexto = getApplicationContext();
//        for(String s : info.valor)
//            txtJson.setText(s);
//        txtJson.setText(info);

    }
    private void runThread() {

        new Thread() {
            public void run() {
                while (true) {
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                new GetLeitura(MainActivity2.this);
                            }
                        });
                        Thread.sleep(1000*10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}