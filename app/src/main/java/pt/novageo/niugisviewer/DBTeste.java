package pt.novageo.niugisviewer;

/**
 * Created by estagiario on 31/03/2017.
 *
 */

import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

public class DBTeste extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 6;
    private static final String DATABASE_NOME = "Locais.db";
    public static final String TABLE_LOCAIS = "locais";
    public static final String COLUMN_ID = "_id";//coluna 0
    public static final String COLUMN_NOMELOCAIS = "nomelocal";//coluna 1
    public static final String COLUMN_DESCRICAO = "Descricao";//coluna 2
    public static final String COLUMN_LAT = "lat";//coluna 3
    public static final String COLUMN_LNG = "lng";//coluna 4

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
                COLUMN_LNG + " VARCHAR(50) " +
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

        ContentValues values1 = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        values1.put(COLUMN_NOMELOCAIS, nome);
        values1.put(COLUMN_DESCRICAO, descricao);
        values1.put(COLUMN_LAT, lat);
        values1.put(COLUMN_LNG, lng);
        db.insert(TABLE_LOCAIS, null, values1);
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

    public Cursor getId(){

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_ID + " FROM " + TABLE_LOCAIS;
        Cursor data = db.rawQuery(query, null);
        return data;

    }

}