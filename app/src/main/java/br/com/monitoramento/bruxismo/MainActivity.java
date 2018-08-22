package br.com.monitoramento.bruxismo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    TextView tv_emg_cadastro;
    TextView tv_emg_grafico;
    TextView tv_emg_controle;
    TextView tv_emg_compartilar;
    TextView tv_emg_teste;
    TextView tv_emg_online;

    CadastroActivity cadastro = new CadastroActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Carrega o layout que você vai usar nessa activity
        setContentView(R.layout.activity_main);
        // Carregue todas as views do seu layout
        loadViews();


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
                new JsonTask().execute("http://192.168.4.1/mestrado/online");
                Log.v("online","Modo online ativado");
                Intent numbersIntent = new Intent(MainActivity.this, TimeGraficoActivity.class);
                startActivity(numbersIntent);
            }
        });

        new JsonTask().execute("http://192.168.4.1/mestrado/offline");
        Log.v("online","Modo online desativado");
    }

    private void loadViews() {
        tv_emg_cadastro=(TextView) findViewById(R.id.cadastro);
        tv_emg_grafico=(TextView) findViewById(R.id.grafico);
        tv_emg_controle=(TextView) findViewById(R.id.controle);
        tv_emg_compartilar=(TextView) findViewById(R.id.compartilhar);
        tv_emg_teste=(TextView) findViewById(R.id.teste);
        tv_emg_online=(TextView) findViewById(R.id.online);

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
           // if (pd.isShowing()){
           //     pd.dismiss();
           // }
           // txtJson.setText(result);
        }
    }

}
