package br.com.monitoramento.bruxismo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

/**
 * Created by rafae on 08/11/2017.
 */

public class ConfigActivity extends AppCompatActivity {

    Button bt_salvar;
    Button bt_abrir;
    Button bt_iniciar_moni;
    Button bt_telemetria;
    ProgressDialog pd;


    private static final int REQUEST_CODE = 3132;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Carrega o layout que você vai usar nessa activity
        setContentView(R.layout.activity_controle);
        // Carregue todas as views do seu layout
        loadViews();
        // Seta o que o botão vai fazer
        bt_iniciar_moni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfigActivity.this, SetupStartActivity.class));
            }
        });
        bt_telemetria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfigActivity.this, MainActivity2.class));
            }
        });
        bt_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(ConfigActivity.this, MainActivity2.class));
                //writeToFile(ConfigActivity.this,filename,fileContents);
                //writeFileOnInternalStorage(ConfigActivity.this,filename,fileContents);
                new JsonTask().execute("http://192.168.4.1/mestrado/edit");

            }

        });

        bt_abrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(ConfigActivity.this, MainActivity2.class));
                //writeToFile(ConfigActivity.this,filename,fileContents);
                //writeFileOnInternalStorage(ConfigActivity.this,filename,fileContents);
                startActivity(new Intent(ConfigActivity.this,Grafico2Activity.class));
            }
        });

    }

    private void loadViews() {
        bt_iniciar_moni = (Button) findViewById(R.id.bt_iniciar_moni);
        bt_telemetria = (Button) findViewById(R.id.bt_telemetria);
        bt_salvar = (Button) findViewById(R.id.bt_salvar);
        bt_abrir = (Button) findViewById(R.id.bt_abrir);
    }

    private void writeToFile(Context mcoContext,String file,String data) {
        try {
            FileOutputStream fOut = openFileOutput(file,Context.MODE_APPEND);
            fOut.write(data.getBytes());
            fOut.close();
            Toast.makeText(getBaseContext(),"file saved",Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void writeFileOnInternalStorage(Context mcoContext,String sFileName, String sBody){
        File file = new File(mcoContext.getFilesDir(),"mydir");
        if(!file.exists()){
            file.mkdir();
        }

        try{
            File gpxfile = new File(file, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();

        }catch (Exception e){
            e.printStackTrace();

        }
    }

    public boolean writeToFile(String dataToWrite, String fileName) {
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Asking for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            return false;
        }
        Log.v("log", "Permission is granted");
        String directoryPath =
                Environment.getExternalStorageDirectory()
                        + File.separator
                        + "LOGS"
                        + File.separator;

        Log.d("log", "Dumping " + fileName + " At : " + directoryPath);

        // Create the fileDirectory.
        File fileDirectory = new File(directoryPath);

        // Make sure the directoryPath directory exists.
        if (!fileDirectory.exists()) {
            // Make it, if it doesn't exist
            if (fileDirectory.mkdirs()) {
                // Created DIR
                Log.i("log", "Log Directory Created Trying to Dump Logs");
            } else {
                // FAILED
                Log.e("log", "Error: Failed to Create Log Directory");
                return false;
            }
        } else {
            Log.i("log", "Log Directory Exist Trying to Dump Logs");
        }

        try {
            // Create FIle Objec which I need to write
            File fileToWrite = new File(directoryPath, fileName + ".txt");

            // ry to create FIle on card
            if (fileToWrite.createNewFile()) {
                //Create a stream to file path
                FileOutputStream outPutStream = new FileOutputStream(fileToWrite);
                //Create Writer to write STream to file Path
                OutputStreamWriter outPutStreamWriter = new OutputStreamWriter(outPutStream);
                // Stream Byte Data to the file

                outPutStreamWriter.append(dataToWrite);
                //Close Writer
                outPutStreamWriter.close();
                //Clear Stream
                outPutStream.flush();
                //Terminate STream
                outPutStream.close();
                Log.e("log", "Success");
                return true;
            } else {
                Log.e("log", "Error: Failed to Create Log File");
                return false;
            }

        } catch (IOException e) {
            Log.e("Exception", "Error: File write failed: " + e.toString());
            e.fillInStackTrace();
            return false;
        }
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(ConfigActivity.this);
            pd.setMessage("Favor Esperar! Salvando Dados");
            pd.setCancelable(false);
            pd.show();
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

            String buffer = "";
            int i = 0;
            ArrayList<Entry> values = new ArrayList<Entry>();

            for(char res : result.toCharArray()){
                if(res != ',')
                    buffer += res;
                else{
                    values.add(new Entry(i, Float.parseFloat(buffer)));
                    i++;
                    buffer = "";
                }
            }
            if (pd.isShowing()){
                pd.dismiss();
            }
            //writeToFile(result,GraficoActivity.this);
            writeToFile(result,"NOMETESTE3");
            //setData(values);
        }
    }

}


