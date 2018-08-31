package br.com.monitoramento.bruxismo;

import android.content.Entity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MyDBHandler extends SQLiteOpenHelper {
    //information of database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "pacienteDB.db";
    public final String TABLE_NAME = "Paciente";
    public final String COLUMN_ID = "PacienteID";
    public final String COLUMN_NOME = "PacienteNome";
    public final String COLUMN_IDADE = "PacienteIdade";
    public final String COLUMN_PESO = "PacientePeso";
    public final String COLUMN_GENERO = "PacienteGenero";
    public final String COLUMN_EMAIL = "PacienteEmail";
    public final String COLUMN_DADOS = "PacienteDados";
    //initialize the database
    public MyDBHandler(Context context, String nome, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        forceCreate();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NOME + " TEXT," +
                COLUMN_IDADE + " LONG," +
                COLUMN_PESO + " LONG," +
                COLUMN_GENERO + " TEXT," +
                COLUMN_EMAIL + " TEXT," +
                COLUMN_DADOS + " TEXT " + ")";
         db.execSQL(CREATE_TABLE);
        Log.v("logSQL","CRIANDO A PORRA DO BD");
        //createEmpty();
    }
    public Cursor getuser() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ",
                null);
        return res;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {}


    public void forceCreate(){
        SQLiteDatabase db = this.getWritableDatabase();
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NOME + " TEXT," +
                COLUMN_IDADE + " LONG," +
                COLUMN_PESO + " LONG," +
                COLUMN_GENERO + " TEXT," +
                COLUMN_EMAIL + " TEXT," +
                COLUMN_DADOS + " TEXT " + ")";
        db.execSQL(CREATE_TABLE);
        createEmpty();
    }

    public void createEmpty(){
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + TABLE_NAME;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if(icount<=0)
            addHandler(new Paciente(
                    1,
                    "Nome",
                    0,
                    0,
                    "Genero",
                    "email",
                    "dados"
            ));
    }


    public ArrayList<String> loadHandler() {
        String result = "";
        String resultado = "";
        ArrayList<String> results = new ArrayList<String>();
        CadastroActivity cadstro = new CadastroActivity();

        String query = "Select * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            int result_0 = cursor.getInt(0);
            //results.add(String.valueOf(result_0));

            String result_1 = cursor.getString(1);
            results.add(result_1);

            int result_2 = cursor.getInt(2);
            results.add(String.valueOf(result_2));

            int result_3 = cursor.getInt(3);
            results.add(String.valueOf(result_3));

            String result_4 = cursor.getString(4);
            results.add(result_4);

            String result_5 = cursor.getString(5);
            results.add(result_5);

            String result_6 = cursor.getString(6);
            results.add(result_6);

            result += String.valueOf(result_0) + " " +
                    result_1 + " " +
                    result_2 + " " +
                    String.valueOf(result_3) + " " +
                    String.valueOf(result_4) + " " +
                    result_5 + " " +
                    System.getProperty("line.separator");
            resultado = result_1;
        }
        cursor.close();
        db.close();
        return results;
    }


    public void addHandler(Paciente paciente) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, paciente.getID());
        values.put(COLUMN_NOME, paciente.getPacienteNome());
        values.put(COLUMN_IDADE, paciente.getPacienteIdade());
        values.put(COLUMN_PESO, paciente.getPacientePeso());
        values.put(COLUMN_GENERO, paciente.getPacienteGenero());
        values.put(COLUMN_EMAIL, paciente.getPacienteEmail());
        values.put(COLUMN_DADOS, paciente.getPacienteDados());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void addData(String pacienteDados) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DADOS, pacienteDados);
        db.update(TABLE_NAME, values, COLUMN_ID + "=" + 1, null);
    }

    public Paciente findHandler(String pacientenome) {
        String query = "Select * FROM " + TABLE_NAME + "WHERE" + COLUMN_NOME + " = " + "'" + pacientenome + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Paciente student = new Paciente();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            student.setID(Integer.parseInt(cursor.getString(0)));
            student.setPacienteNome(cursor.getString(1));
            student.setPacienteIdade(Integer.parseInt(cursor.getString(2)));
            student.setPacientePeso(Integer.parseInt(cursor.getString(3)));
            student.setPacienteGenero(cursor.getString(4));
            student.setPacienteemail(cursor.getString(5));
            student.setPacienteDados(cursor.getString(6));
            cursor.close();
        } else {
            student = null;
        }
        db.close();
        return student;
    }

    public boolean deleteHandler(int ID) {

        boolean result = false;
        String query = "Select*FROM" + TABLE_NAME + "WHERE" + COLUMN_ID + "= '" + String.valueOf(ID) + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Paciente student = new Paciente();
        if (cursor.moveToFirst()) {
            student.setID(Integer.parseInt(cursor.getString(0)));

            db.delete(TABLE_NAME, COLUMN_ID + "=?",
                    new String[] {
                    String.valueOf(student.getID())
            });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
    public void deleteTable(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public String checkTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                + TABLE_NAME + "'", null);
        return String.valueOf(cursor.getCount());
    }

    public boolean updateHandler(Paciente paciente) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, paciente.getID());
        values.put(COLUMN_NOME, paciente.getPacienteNome());
        values.put(COLUMN_IDADE, paciente.getPacienteIdade());
        values.put(COLUMN_PESO, paciente.getPacientePeso());
        values.put(COLUMN_GENERO, paciente.getPacienteGenero());
        values.put(COLUMN_EMAIL, paciente.getPacienteEmail());
        values.put(COLUMN_DADOS, paciente.getPacienteDados());
        return db.update(TABLE_NAME, values, COLUMN_ID + "=" + 1, null) > 0;
    }
}