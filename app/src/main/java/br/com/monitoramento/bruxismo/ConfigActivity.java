package br.com.monitoramento.bruxismo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Created by rafae on 08/11/2017.
 */

public class ConfigActivity extends AppCompatActivity implements ArquivoDialogListener,
        SpinnerListener {

    Button bt_salvar;
    Button bt_abrir;
    Button bt_iniciar_moni;
    Button bt_telemetria;
    ProgressDialog pd;
    Cursor cursor;
    EditText input;
    String nomeArquivo;

    private static final int REQUEST_CODE = 3132;
    private static final int MY_INTENT_CLICK = 23122;



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
                startActivity(new Intent(ConfigActivity.this, SetupDadosActivity.class));
            }
        });


        bt_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();

            }
        });

        bt_abrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File sdCardRoot = Environment.getExternalStorageDirectory();
                File yourDir = new File(sdCardRoot, "LOGS");
                ArrayList<String> name = new ArrayList<String>();
                yourDir.listFiles();
                for (File f : yourDir.listFiles())
                    if (f.isFile())
                        name.add(f.getName());
                SpinnerActivity.spinnerData = name;
                //name.add("dadosFiltrados.txt");
                //SpinnerActivity.spinnerData = name;
                openSpinner();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_INTENT_CLICK)
            if(data != null) {
               Grafico2Activity.selectedImagePath = data.getData();

            }
    }

    private void getImage() {
        //MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        //intent.setType("text/xls");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Complete action using"),
                    MY_INTENT_CLICK);
        }catch (Exception e) {
            e.getMessage();
        }
    }

    public void openDialog(){
        ArquivoDialog arquivoDialog = new ArquivoDialog();
        arquivoDialog.show(getSupportFragmentManager(), "Salvar nome do Arquivo");
    }

    public void openSpinner(){
        SpinnerActivity spinner = new SpinnerActivity();
        spinner.show(getSupportFragmentManager(), "Selecione o arquivo que deseja abrir");
    }

    @Override
    public void applyTexts(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
        new JsonTask().execute("http://192.168.4.1/mestrado/edit");
    }

    @Override
    public void applySpinner(String nomeArquivo) {
        Grafico2Activity.nomeAbrirArquivo = nomeArquivo;
        startActivity(new Intent(ConfigActivity.this,Grafico2Activity.class));
    }

    private void creatXlsx(ArrayList<String> vals,String nomeArquivo){
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Asking for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
        Log.v("log", "Permission is granted");
        MyDBHandler db = new MyDBHandler(this,null,null,1);
        Cursor cursor = db.getuser();
        File sd = Environment.getExternalStorageDirectory();

        String csvFile = nomeArquivo+".xls";

        File directory = new File(sd.getAbsolutePath(),"LOGS");
        //create directory if not exist
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }
        try {

            //file path
            File file = new File(directory, csvFile);
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("pt", "BR"));
            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("userList", 0);
            // column and row
            sheet.addCell(new Label(0, 0, "Nome"));
            sheet.addCell(new Label(1, 0, "Idade"));
            sheet.addCell(new Label(2, 0, "Peso"));
            sheet.addCell(new Label(3, 0, "Genero"));
            sheet.addCell(new Label(4, 0, "Email"));
            sheet.addCell(new Label(5, 0, "Dados"));
           // if (cursor.moveToFirst()) {
                //do {
            cursor.moveToFirst();
                    String nome = cursor.getString(cursor.getColumnIndex(db.COLUMN_NOME));
                    String idade = cursor.getString(cursor.getColumnIndex(db.COLUMN_IDADE));
                    String peso = cursor.getString(cursor.getColumnIndex(db.COLUMN_PESO));
                    String genero = cursor.getString(cursor.getColumnIndex(db.COLUMN_GENERO));
                    String email = cursor.getString(cursor.getColumnIndex(db.COLUMN_EMAIL));

                    int i = cursor.getPosition() + 1;
                    sheet.addCell(new Label(0, i, nome));
                    sheet.addCell(new Label(1, i, idade));
                    sheet.addCell(new Label(2, i, peso));
                    sheet.addCell(new Label(3, i, genero));
                    sheet.addCell(new Label(4, i, email));
                    int c = 0, r= 0, cont = 0;

                    for(String result : vals) {
                        sheet.addCell(new Label(5 + c, i + r, result));
                        r++;
                        if(r == 60000){
                            r = 0;
                            c++;
                        }
                        Log.v("ValsXLS","Columns = "+ String.valueOf(c)+" Row = "+
                                String.valueOf(r+(c*60000)));
                    //}
                //} //while (cursor.moveToNext());
            }
            //closing cursor
            cursor.close();
            workbook.write();
            workbook.close();
            Toast.makeText(getApplication(),
                    "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();

        } catch(Exception e){
            e.printStackTrace();
        }finally {
            if (pd.isShowing()){
                pd.dismiss();
            }
        }
    }
    private void loadViews() {
        bt_iniciar_moni = (Button) findViewById(R.id.bt_iniciar_moni);
        bt_telemetria = (Button) findViewById(R.id.bt_telemetria);
        bt_salvar = (Button) findViewById(R.id.bt_salvar);
        bt_abrir = (Button) findViewById(R.id.bt_abrir);
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
            ArrayList<String> vals = new ArrayList<String>();

            for(char res : result.toCharArray()){
                if(res != ',')
                    buffer += res;
                else{
                    values.add(new Entry(i, Float.parseFloat(buffer)));
                    vals.add(buffer);
                    i++;
                    buffer = "";
                }
            }

            //writeToFile(result,GraficoActivity.this);
            writeToFile(result,nomeArquivo);
            //MyDBHandler db = new MyDBHandler(getApplicationContext(),null,null,1);
            //db.addData(vals);
            creatXlsx(vals,nomeArquivo);
            //setData(values);

        }
    }

}


