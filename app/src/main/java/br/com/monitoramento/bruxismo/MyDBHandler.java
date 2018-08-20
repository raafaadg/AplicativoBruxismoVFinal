package br.com.monitoramento.bruxismo;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyDBHandler extends SQLiteOpenHelper {
    //information of database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "pacienteDB.db";
    public static final String TABLE_NAME = "Paciente";
    public static final String COLUMN_ID = "PacienteID";
    public static final String COLUMN_NOME = "PacienteNome";
    public static final String COLUMN_IDADE = "PacienteIdade";
    public static final String COLUMN_PESO = "PacientePeso";
    public static final String COLUMN_GENERO = "PacienteGenero";
    public static final String COLUMN_EMAIL = "PacienteEmail";
    //initialize the database
    public MyDBHandler(Context context, String nome, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NOME + " TEXT," +
                COLUMN_IDADE + " LONG," +
                COLUMN_PESO + " LONG," +
                COLUMN_GENERO + " TEXT," +
                COLUMN_EMAIL + " TEXT " + ")";
         db.execSQL(CREATE_TABLE);
        Log.v("logSQL","CRIANDO A PORRA DO BD");
        //Paciente paciente = new Paciente(0,"Nome",0,
        //        0,"Genero","Email");
        //addHandler(paciente);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {}

    public String loadHandler() {
        String result = "";

        String query = "Select * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            int result_0 = cursor.getInt(0);
            String result_1 = cursor.getString(1);
            int result_2 = cursor.getInt(2);
            int result_3 = cursor.getInt(3);
            String result_4 = cursor.getString(4);
            String result_5 = cursor.getString(5);
            result += String.valueOf(result_0) + " " +
                    result_1 + " " +
                    result_2 + " " +
                    result_3 + " " +
                    result_4 + " " +
                    result_5 + " " +
                    System.getProperty("line.separator");
        }
        cursor.close();
        db.close();
        Log.e("logSQL",result);
        return result;

    }

    public List loadHandler2(){
        SQLiteDatabase db = this.getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                COLUMN_ID,
                COLUMN_NOME,
                COLUMN_IDADE,
                COLUMN_PESO,
                COLUMN_GENERO,
                COLUMN_EMAIL
        };

// Filter results WHERE "title" = 'My Title'
        String selection = COLUMN_NOME+ " = ?";
        String[] selectionArgs = { "rafaeldg" };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                COLUMN_ID + " DESC";

        Cursor cursor = db.query(
                TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );
        List itemIds = new ArrayList<>();
        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(COLUMN_ID));
            itemIds.add(itemId);
        }
        cursor.close();
        return itemIds;
    }

    public void addHandler(Paciente paciente) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, paciente.getID());
        values.put(COLUMN_NOME, paciente.getPacienteNome());
        values.put(COLUMN_IDADE, paciente.getPacienteIdade());
        values.put(COLUMN_PESO, paciente.getPacientePeso());
        values.put(COLUMN_GENERO, paciente.getPacienteGenero());
        values.put(COLUMN_EMAIL, paciente.getPacienteEmail());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
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

    public boolean updateHandler(int ID, String nome, int idade, int peso,
                                 String genero, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(COLUMN_ID, ID);
        args.put(COLUMN_NOME, nome);
        args.put(COLUMN_IDADE, idade);
        args.put(COLUMN_PESO, peso);
        args.put(COLUMN_GENERO, genero);
        args.put(COLUMN_EMAIL, email);
        return db.update(TABLE_NAME, args, COLUMN_ID + "=" + ID, null) > 0;
    }
}