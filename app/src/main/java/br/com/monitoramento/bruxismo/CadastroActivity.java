package br.com.monitoramento.bruxismo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.objectbox.Box;
import io.objectbox.query.Query;

/**
 * Created by rafae on 13/06/2017.
 */

public class CadastroActivity extends AppCompatActivity{
    //EditText et_emg_comando;
    public EditText et_emg_nome;
    public EditText et_emg_idade;
    public EditText et_emg_peso;
    public EditText et_emg_tipo;
    public EditText et_emg_email;
    public Button bt_emg_update;
    public Button bt_emg_load;
    public Button bt_emg_check;
    public Button bt_emg_create;
    public TextView lst;
    public ArrayList<String> dados = new ArrayList <String>();

    private Box<PacienteBoxActivity> notesBox;
    private Query<PacienteBoxActivity> notesQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Carrega o layout que você vai usar nessa activity
        setContentView(R.layout.activity_cadastro);
        // Carregue todas as views do seu layout
        loadViews();

        //BoxStore boxStore = ((App) getApplication()).getBoxStore();
        //notesBox = boxStore.boxFor(PacienteBoxActivity.class);
//
        //// query all notes, sorted a-z by their text (https://docs.objectbox.io/queries)
        //notesQuery = notesBox.query().order(PacienteBoxActivity_.nome).build();
    }



    public void clear() {
        //et_emg_comando.setText(info.comando);
        et_emg_nome.setText("");
        et_emg_idade.setText("");
        et_emg_peso.setText("");
        et_emg_tipo.setText("");
        et_emg_email.setText("");

    }

    public static ArrayList<String> convertStringToArray(String str){
        ArrayList<String> data = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        for(char aux:str.toCharArray()){
            if(aux != ',')
                buffer.append(aux);
            else{
                data.add(buffer.toString());
                buffer.setLength(0);
            }

        }

        return data;
    }

    public void loadPaciente(View view) {
        Log.v("logSQL","Executando loadPaciente");

        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        ArrayList<String> result = dbHandler.loadHandler();
        //lst.setText(result.toString());
        et_emg_nome.setText(result.get(0));
        et_emg_idade.setText(result.get(1));
        et_emg_peso.setText(result.get(2));
        et_emg_tipo.setText(result.get(3));
        et_emg_email.setText(result.get(4));

        convertStringToArray(result.get(5));
    }


    public void attPaciente(View view) {
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        ArrayList<String> results = dbHandler.loadHandler();
        String nome, genero, email,dados;
        int idade,peso;

        if(et_emg_nome.getText().toString().isEmpty())
             nome = results.get(0);
        else
            nome = et_emg_nome.getText().toString();

        if(et_emg_idade.getText().toString().isEmpty())
            idade = Integer.parseInt(results.get(1));
        else
            idade = Integer.parseInt(et_emg_idade.getText().toString());

        if(et_emg_peso.getText().toString().isEmpty())
            peso = Integer.parseInt(results.get(2));
        else
            peso = Integer.parseInt(et_emg_peso.getText().toString());

        if(et_emg_tipo.getText().toString().isEmpty())
            genero = results.get(3);
        else
            genero = et_emg_tipo.getText().toString();

        if(et_emg_email.getText().toString().isEmpty())
            email = results.get(4);
        else
            email = et_emg_email.getText().toString();

        dados = results.get(5);

        Paciente paciente = new Paciente(1, nome, idade, peso, genero, email,dados);
        if(dbHandler.updateHandler(paciente))
            Toast.makeText(this, "Gravação Atualizada", Toast.LENGTH_LONG).show();
         else
            Toast.makeText(this, "ERRO NA GRAVAÇÃO", Toast.LENGTH_LONG).show();
        clear();
    }

    public void addPaciente(View view) {
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        //int id = Integer.parseInt(et_emg_comando.getText().toString());
        String nome = et_emg_nome.getText().toString();
        int idade = Integer.parseInt(et_emg_idade.getText().toString());
        int peso = Integer.parseInt(et_emg_peso.getText().toString());
        String genero = et_emg_tipo.getText().toString();
        String email = et_emg_email.getText().toString();

        Paciente paciente = new Paciente(1, nome, idade, peso, genero, email,"");
        dbHandler.addHandler(paciente);
        //et_emg_comando.setText("");
        et_emg_nome.setText("");
        et_emg_idade.setText("");
        et_emg_peso.setText("");
        et_emg_tipo.setText("");
        et_emg_email.setText("");
    }
    public void createTable(View view){
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        dbHandler.forceCreate();
        Toast.makeText(this, "Tabela Criada" , Toast.LENGTH_LONG).show();

    }
    public void delTable(View view){
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        dbHandler.deleteTable();
    }
    public void existTable(View view){
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        dbHandler.checkTable();
        Toast.makeText(this, dbHandler.checkTable() , Toast.LENGTH_SHORT).show();
    }
    private void loadViews() {
        //et_emg_comando = (EditText) findViewById(R.id.et_emg_comando);
        et_emg_nome = (EditText) findViewById(R.id.et_emg_nome);
        et_emg_idade = (EditText) findViewById(R.id.et_emg_idade);
        et_emg_peso = (EditText) findViewById(R.id.et_emg_peso);
        et_emg_tipo = (EditText) findViewById(R.id.et_emg_tipo);
        et_emg_email = (EditText) findViewById(R.id.et_emg_email);
        bt_emg_update = (Button) findViewById(R.id.bt_emg_update);
        bt_emg_load = (Button) findViewById(R.id.bt_emg_load);
        //bt_emg_check = (Button) findViewById(R.id.bt_emg_check);
        //bt_emg_create = (Button) findViewById(R.id.bt_emg_create);
        //lst = (TextView) findViewById(R.id.lst);
    }

}
