package pt.novageo.niugisviewer;

/**
 * Created by estagiario on 31/03/2017.
 *
 */

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

public class DBTeste extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 6;
    private static final String DATABASE_NOME = "Locais.db";
    public static final String TABLE_LOCAIS = "locais";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NOMELOCAIS = "nomelocal";
    public static final String COLUMN_DESCRICAO = "Descricao";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LNG = "lng";

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

    //adicionar um novo local
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

    //eliminar um novo local
    public boolean deleteLocal(String nomelocal) {

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_LOCAIS + " WHERE " + COLUMN_NOMELOCAIS + "=\"" + nomelocal + "\";");

        return true;
    }

    //imprimir a base de dados como string
    public String databaseToString() {

        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_LOCAIS + " WHERE 1";

        //cursor serve para apontar ao lugar do resultado
        Cursor c = db.rawQuery(query, null);
        //move para primeira linha
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("_id")) != null) {
                dbString += "ID:";
                dbString += c.getString(c.getColumnIndex("_id"));
                dbString += ", Nome:";
                dbString += c.getString(c.getColumnIndex("nomelocal"));
                dbString += ", Descricao:";
                dbString += c.getString(c.getColumnIndex("Descricao"));
                dbString += ", Coord.:";
                dbString += c.getString(c.getColumnIndex("lat"));
                dbString += ", ";
                dbString += c.getString(c.getColumnIndex("lng"));
                dbString += "\n";

            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }


}