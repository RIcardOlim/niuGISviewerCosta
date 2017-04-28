package pt.novageo.niugisviewer;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class DBTeste extends SQLiteOpenHelper {

    Date date;

    private static final int DATABASE_VERSION = 9;
    private static final String DATABASE_NOME = "Locais.db";
    public static final String TABLE_LOCAIS = "locais";
    public static final String COLUMN_ID = "_id";//coluna 0
    public static final String COLUMN_NOMELOCAIS = "nomelocal";//coluna 1
    public static final String COLUMN_DESCRICAO = "Descricao";//coluna 2
    public static final String COLUMN_LAT = "lat";//coluna 3
    public static final String COLUMN_LNG = "lng";//coluna 4
    public static final String COLUMN_DATA = "data";//coluna 5

    public DBTeste(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NOME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_LOCAIS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMELOCAIS + " TEXT, " +
                COLUMN_DESCRICAO + " TEXT, " +
                COLUMN_LAT + " VARCHAR(50), " +
                COLUMN_LNG + " VARCHAR(50), " +
                COLUMN_DATA + " DATE " +
        ");";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCAIS);
        onCreate(db);
    }

    //adicionar um registo
    public boolean addPonto(String nome, String descricao, double lat, double lng) {

        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        values.put(COLUMN_NOMELOCAIS, nome);
        values.put(COLUMN_DESCRICAO, descricao);
        values.put(COLUMN_LAT, lat);
        values.put(COLUMN_LNG, lng);
        values.put(COLUMN_DATA, getDateTime());
        db.insert(TABLE_LOCAIS, null, values);
        db.close();

        return true;
    }

    //eliminar um registo
    public boolean deletePonto(int id) {

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_LOCAIS + " WHERE " + COLUMN_ID + "=\"" + id + "\";");

        return true;
    }

    //imprimir a base de dados
    public Cursor getData(){

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_LOCAIS;
        Cursor data = db.rawQuery(query, null);
        return data;

    }

    public Cursor getId(){

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_ID + " FROM " + TABLE_LOCAIS;
        Cursor data = db.rawQuery(query, null);
        return data;

    }


    public Cursor getIdbyNome(String nome){

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_ID + " FROM " + TABLE_LOCAIS + " WHERE " + COLUMN_NOMELOCAIS + " = '" + nome + "'";
        Cursor data = db.rawQuery(query, null);
        return data;

    }

    public Cursor getDataById(int id){

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_LOCAIS + " WHERE " + COLUMN_ID + " = '" + id + "'";
        Cursor data = db.rawQuery(query, null);
        return data;

    }

    public boolean UpdateNome(int id, String nome) {


        if(Objects.equals(nome, "")) {
            return false;
        } else {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("UPDATE " + TABLE_LOCAIS + " SET " + COLUMN_NOMELOCAIS + " = '" + nome + "' WHERE " + COLUMN_ID + " = '" + id + "';");
            db.close();
            return true;
        }

    }

    public boolean UpdateDesc(int id, String desc) {


        if(Objects.equals(desc, "")) {
            return false;
        } else {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("UPDATE " + TABLE_LOCAIS + " SET " + COLUMN_DESCRICAO + " = '" + desc + "' WHERE " + COLUMN_ID + " = '" + id + "';");
            db.close();
            return true;
        }

    }

    private String getDateTime(){

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);

    }

}