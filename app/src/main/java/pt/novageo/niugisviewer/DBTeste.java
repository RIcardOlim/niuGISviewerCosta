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

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NOME = "Locais.db";
    public static final String TABLE_LOCAIS = "locais";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NOMELOCAIS = "nomelocal";
    public static final String COLUMN_DESCRICAO = "Descricao";


    public DBTeste(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NOME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_LOCAIS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMELOCAIS + " TEXT, " +
                COLUMN_DESCRICAO + " TEXT " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCAIS);
        onCreate(db);
    }

    //adicionar um novo local
    public boolean addPonto(Locais Locais1, Locais Locais2) {
        ContentValues values1= new ContentValues();
        ContentValues values2= new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        values1.put(COLUMN_NOMELOCAIS, Locais1.get_nomelocal());
        values2.put(COLUMN_NOMELOCAIS, Locais2.get_descricao());
        db.insert(TABLE_LOCAIS, null, values1);
        db.insert(TABLE_LOCAIS, null, values2);
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

        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("nomelocal"))!= null){
               dbString += c.getString(c.getColumnIndex("nomelocal"));
                dbString += ", ";
                dbString += c.getString(c.getColumnIndex("Descricao"));
                dbString += "\n";
            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }

}
