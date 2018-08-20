package br.com.monitoramento.bruxismo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import br.com.monitoramento.bruxismo.client.GetLeitura.GetLeitura;
import br.com.monitoramento.bruxismo.client.GetLeitura.GetLeituraResponse;
import okhttp3.OkHttpClient;

/**
 * Created by rafae on 13/06/2017.
 */

public class CadastroActivity extends AppCompatActivity{
    EditText et_emg_comando;
    EditText et_emg_nome;
    EditText et_emg_idade;
    EditText et_emg_peso;
    EditText et_emg_tipo;
    EditText et_emg_email;
    Button bt_emg_update;
    Button bt_emg_load;
    TextView lst;
    public ArrayList<String> dados = new ArrayList <String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Carrega o layout que você vai usar nessa activity
        setContentView(R.layout.activity_cadastro);
        // Carregue todas as views do seu layout
        loadViews();
        //runThread();
        // Seta o que o botão vai fazer
        //bt_emg_update.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        efetivarCadastro();
        //        Log.e("logRecord","Inicindo Gravação");
        //        Log.e("logSQL","Realizando Update no SQL");
        //    }
        //});
        //bt_emg_load.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        //loadPaciente(v);
        //        Log.e("logSQL","Buscando Informações no BD");
        //    }
        //});

    }

    private void loadViews() {
        et_emg_comando = (EditText) findViewById(R.id.et_emg_comando);
        et_emg_nome = (EditText) findViewById(R.id.et_emg_nome);
        et_emg_idade = (EditText) findViewById(R.id.et_emg_idade);
        et_emg_peso = (EditText) findViewById(R.id.et_emg_peso);
        et_emg_tipo = (EditText) findViewById(R.id.et_emg_tipo);
        et_emg_email = (EditText) findViewById(R.id.et_emg_email);
        bt_emg_update = (Button) findViewById(R.id.bt_emg_update);
        bt_emg_load = (Button) findViewById(R.id.bt_emg_load);
        lst = (TextView) findViewById(R.id.lst);
    }

    public void setInfo(GetLeituraResponse info) {
//        et_emg_comando.setText(info.comando);
//        et_emg_nome.setText(info.nome);
//        et_emg_idade.setText(info.idade);
//        et_emg_peso.setText(info.peso);
//        et_emg_tipo.setText(info.tipo);

    }

    private void efetivarCadastro(){
        //new GetLeitura(CadastroActivity.this);
        dados.add(et_emg_comando.getText().toString());
        dados.add(et_emg_nome.getText().toString());
        dados.add(et_emg_idade.getText().toString());
        dados.add(et_emg_peso.getText().toString());
        dados.add(et_emg_tipo.getText().toString());
        dados.add(et_emg_email.getText().toString());
        if(writeToFile(dados,"dadosCadastro"))
            Toast.makeText(this, "Dados Gravados com sucesso!!", Toast.LENGTH_LONG).show();
    }

    public boolean writeToFile(ArrayList<String> dados, String fileName) {
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Asking for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3132);
            return false;
        }
        Log.v("logRecord", "Permission is granted");
        String directoryPath =
                Environment.getExternalStorageDirectory()
                        + File.separator
                        + "LOGS"
                        + File.separator
                        + "CADASTRO"
                        + File.separator;

        Log.d("logRecord", "Dumping " + fileName + " At : " + directoryPath);

        // Create the fileDirectory.
        File fileDirectory = new File(directoryPath);

        // Make sure the directoryPath directory exists.
        if (!fileDirectory.exists()) {
            // Make it, if it doesn't exist
            if (fileDirectory.mkdirs()) {
                // Created DIR
                Log.i("logRecord", "Log Directory Created Trying to Dump Logs");
            } else {
                // FAILED
                Log.e("logRecord", "Error: Failed to Create Log Directory");
                return false;
            }
        } else {
            Log.i("logRecord", "Log Directory Exist Trying to Dump Logs");
        }

        try {
            // Create FIle Objec which I need to write
            File fileToWrite = new File(directoryPath, fileName + ".txt");

            // ry to create FIle on card
                   if (fileToWrite.createNewFile()) {
                       recordFile(fileToWrite);
                       return true;
                   } else {
                       Log.e("logRecord", "Documento já existente. APAGANDO");
                       if(DeleteRecursive(fileToWrite)) {
                           Log.e("logRecord", "Criando documento após apagar antigo");
                           recordFile(fileToWrite);
                       }
                       return true;
                   }

        } catch (IOException e) {
            Log.e("logRecord", "Error: File write failed: " + e.toString());
            e.fillInStackTrace();
            return false;
        }
    }
    private void recordFile(File fileToWrite){
        //Create a stream to file path
        FileOutputStream outPutStream = null;
        try {
            outPutStream = new FileOutputStream(fileToWrite);

            //Create Writer to write STream to file Path
            OutputStreamWriter outPutStreamWriter = new OutputStreamWriter(outPutStream);
            // Stream Byte Data to the file

            for (String dataToWrite : dados)
                outPutStreamWriter.append(dataToWrite + "\n");

            //Close Writer
            outPutStreamWriter.close();
            //Clear Stream
            outPutStream.flush();
            //Terminate STream
            outPutStream.close();
            Log.e("logRecord", "Success");
        }catch (IOException e) {
            Log.e("logRecord", "Error: File write failed: " + e.toString());
            e.fillInStackTrace();
        }
    }

    public static boolean DeleteRecursive(File fileOrDirectory)
    {
        if (fileOrDirectory.isDirectory())
        {
            for (File child : fileOrDirectory.listFiles())
            {
                DeleteRecursive(child);
            }
        }

        return fileOrDirectory.delete();
    }


    public void updatePaciente(View view) {
        MyDBHandler dbHandler = new MyDBHandler(this, null,
                null, 1);
        boolean result = dbHandler.updateHandler(
                        Integer.parseInt(et_emg_comando.getText().toString()),
                        et_emg_nome.getText().toString(),
                        Integer.parseInt(et_emg_idade.getText().toString()),
                        Integer.parseInt(et_emg_peso.getText().toString()),
                        et_emg_tipo.getText().toString(),
                        et_emg_email.getText().toString()
                );
        if (result) {
            et_emg_comando.setText("");
            et_emg_nome.setText("");
            et_emg_idade.setText("");
            et_emg_peso.setText("");
            et_emg_tipo.setText("");
            et_emg_email.setText("");
            Toast.makeText(this, "Gravação Atualizada", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(this, "ERRO NA GRAVAÇÃO", Toast.LENGTH_LONG).show();
    }


    public void loadPaciente(View view) {
        Log.v("logSQL","Executando loadPaciente");

        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        Log.v("logSQL",dbHandler.loadHandler().toString());
        lst.setText(dbHandler.loadHandler().toString());
        //et_emg_comando.setText("");
        //et_emg_nome.setText("");
        //et_emg_idade.setText("");
        //et_emg_peso.setText("");
        //et_emg_tipo.setText("");
        //et_emg_email.setText("");
    }


    public void addPaciente(View view) {
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        int id = Integer.parseInt(et_emg_comando.getText().toString());
        String nome = et_emg_nome.getText().toString();
        int idade = Integer.parseInt(et_emg_idade.getText().toString());
        int peso = Integer.parseInt(et_emg_peso.getText().toString());
        String genero = et_emg_tipo.getText().toString();
        String email = et_emg_email.getText().toString();

        Paciente paciente = new Paciente(id, nome, idade, peso, genero, email);
        dbHandler.addHandler(paciente);
        et_emg_comando.setText("");
        et_emg_nome.setText("");
        et_emg_idade.setText("");
        et_emg_peso.setText("");
        et_emg_tipo.setText("");
        et_emg_email.setText("");
    }

    private void runThread() {

        new Thread() {
            public void run() {
                while (true) {
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                //new GetLeitura(CadastroActivity.this);
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
