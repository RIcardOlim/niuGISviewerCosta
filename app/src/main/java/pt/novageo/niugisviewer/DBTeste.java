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



    private static final int DATABASE_VERSION = 14;
    private static final String DATABASE_NOME = "Locais.db";
    private static final String TABLE_LOCAIS = "locais";
    private static final String COLUMN_ID = "_id";//coluna 0
    private static final String COLUMN_NOMELOCAIS = "nomelocal";//coluna 1
    private static final String COLUMN_DESCRICAO = "Descricao";//coluna 2
    private static final String COLUMN_LAT = "lat";//coluna 3
    private static final String COLUMN_LNG = "lng";//coluna 4
    private static final String COLUMN_DATA = "data";//coluna 5
    private static final String COLUMN_MORADA = "morada";//coluna 6
    private static final String COLUMN_IMAGEM = "imagem";//coluna 7

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
                COLUMN_DATA + " DATETIME, " +
                COLUMN_MORADA + " TEXT, " +
                COLUMN_IMAGEM + " BLOB " +

        ");";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCAIS);
        onCreate(db);
    }

    //adicionar um registo
    public boolean addPonto(String nome, String descricao, double lat, double lng, String morada, byte[] imagem) {

        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        values.put(COLUMN_NOMELOCAIS, nome);
        values.put(COLUMN_DESCRICAO, descricao);
        values.put(COLUMN_LAT, lat);
        values.put(COLUMN_LNG, lng);
        values.put(COLUMN_DATA, getDateTime());
        values.put(COLUMN_MORADA, morada);
        values.put(COLUMN_IMAGEM, imagem);
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

    private String getDateTime(){

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);

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
}