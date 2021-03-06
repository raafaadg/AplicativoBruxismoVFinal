package br.com.monitoramento.bruxismo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;
import static android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE;


public class MainActivity extends AppCompatActivity {

    TextView tv_emg_cadastro;
    TextView tv_emg_grafico;
    TextView tv_emg_controle;
    TextView tv_emg_compartilar;
    TextView tv_emg_teste;
    TextView tv_emg_online;
    public ProgressDialog pd;
    public String urll;
    public boolean reco = false;
    private static final int REQUEST_CODE = 3132;

    CadastroActivity cadastro = new CadastroActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Carrega o layout que você vai usar nessa activity
        setContentView(R.layout.activity_main);
        // Carregue todas as views do seu layout
        loadViews();
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Asking for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }

        tv_emg_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent numbersIntent = new Intent(MainActivity.this, CadastroActivity.class);
                startActivity(numbersIntent);
            }
        });

        tv_emg_grafico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent numbersIntent = new Intent(MainActivity.this, GraficoActivity.class);
                startActivity(numbersIntent);
            }
        });

        tv_emg_controle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent numbersIntent = new Intent(MainActivity.this, ConfigActivity.class);
                startActivity(numbersIntent);
            }
        });

        tv_emg_compartilar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent numbersIntent = new Intent(MainActivity.this, CompartilharActivity.class);
                startActivity(numbersIntent);
            }
        });

        tv_emg_teste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent numbersIntent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(numbersIntent);
            }
        });
        tv_emg_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new ClientSend().run();
                urll = "http://192.168.4.1/mestrado/online";
                new SendRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                //tryHTTP("http://192.168.4.1/mestrado/online");

            }
        });

        urll = "http://192.168.4.1/mestrado/offline";
        new SendRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        //tryHTTP("http://192.168.4.1/mestrado/offline");

    }

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
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (!result.isEmpty()) {
                if (result.equals("Online Habilitado")) {
                    reco = true;
                    startActivity(new Intent(MainActivity.this, TimeGraficoActivity.class));
                }
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onRestart() {
        //if(reco) {
            //reco = false;
            //tryHTTP("http://192.168.4.1/mestrado/offline");
            //urll = "http://192.168.4.1/mestrado/offline";
            //new SendRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        //}
        super.onRestart();
    }

    private static String getIpAddress(WifiInfo wifiInfo) {
        String result;
        int ip = wifiInfo.getIpAddress();

        result = String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff),
                (ip >> 24 & 0xff));

        return result;
    }

    @SuppressLint("HardwareIds")
    private static String getMacAddressByWifiInfo(Context context) {
        String resp = "";
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifi != null) {
                WifiInfo info = wifi.getConnectionInfo();
                if (info != null) resp = getIpAddress(info);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp = "";
        }
        return resp;
    }
    private void loadViews() {
        tv_emg_cadastro=(TextView) findViewById(R.id.cadastro);
        tv_emg_grafico=(TextView) findViewById(R.id.grafico);
        tv_emg_controle=(TextView) findViewById(R.id.controle);
        tv_emg_compartilar=(TextView) findViewById(R.id.compartilhar);
        tv_emg_teste=(TextView) findViewById(R.id.teste);
        tv_emg_online=(TextView) findViewById(R.id.online);

    }

    public class ClientSend implements Runnable {
        @Override
        public void run() {
            try {
                DatagramSocket udpSocket = new DatagramSocket(4210);
                InetAddress serverAddr = InetAddress.getByName("192.168.4.1");
                byte[] buf = ("The String to Send").getBytes();
                DatagramPacket packet = new DatagramPacket(buf, buf.length,serverAddr, 4210);
                udpSocket.send(packet);
            } catch (SocketException e) {
                Log.e("Udp:", "Socket Error:", e);
            } catch (IOException e) {
                Log.e("Udp Send:", "IO Error:", e);
            }
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
                        //if(response.equals("Online Desabilitado")){
                        //    Log.v("online","Modo online desativado");
                        //    Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                        //}else {
                        //    Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                        //    Intent numbersIntent = new Intent(MainActivity.this, TimeGraficoActivity.class);
                        //    startActivity(numbersIntent);
                        //    Log.v("online", "Modo online Ativado");
                        //}
                        if (pd.isShowing()) {
                            pd.dismiss();
                        }
                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
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
        )/*{
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Timestamp", String.valueOf(System.currentTimeMillis()));
                params.put("IP",getMacAddressByWifiInfo(getApplicationContext()));

                return params;
            }

        }*/;
        putRequest.setRetryPolicy(new DefaultRetryPolicy(
                40000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(putRequest);
    }

}
