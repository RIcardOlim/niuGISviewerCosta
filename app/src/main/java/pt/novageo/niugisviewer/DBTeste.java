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

    private static final int DATABASE_VERSION = 20;
    private static final String DATABASE_NOME = "Locais.db";

    //TABELA 1 - ESCOLA
    private static final String TABLE_ESCOLA = "escola";
    private static final String COLUMN_ID_ESCOLA = "_id";//coluna 0
    private static final String COLUMN_NOME_ESCOLA = "nomelocal";//coluna 1
    private static final String COLUMN_DESCRICAO_ESCOlA = "Descricao";//coluna 2
    private static final String COLUMN_TIPO_ESCOLA = "tipo";//coluna 3
    private static final String COLUMN_LAT_ESCOLA = "lat";//coluna 4
    private static final String COLUMN_LNG_ESCOLA = "lng";//coluna 5
    private static final String COLUMN_DATA_ESCOLA = "data";//coluna 6
    private static final String COLUMN_MORADA_ESCOLA = "morada";//coluna 7
    private static final String COLUMN_IMAGEM_ESCOLA = "imagem";//coluna 8

    //TABELA 2 - CAFÉS
    private static final String TABLE_CAFE = "cafe";
    private static final String COLUMN_ID_CAFE = "_id";//coluna 0
    private static final String COLUMN_NOME_CAFE = "nomelocal";//coluna 1
    private static final String COLUMN_DESCRICAO_CAFE = "Descricao";//coluna 2
    private static final String COLUMN_TIPO_CAFE = "tipo";//coluna 3
    private static final String COLUMN_LAT_CAFE = "lat";//coluna 4
    private static final String COLUMN_LNG_CAFE = "lng";//coluna 5
    private static final String COLUMN_DATA_CAFE = "data";//coluna 6
    private static final String COLUMN_MORADA_CAFE = "morada";//coluna 7
    private static final String COLUMN_IMAGEM_CAFE = "imagem";//coluna 8

    //TABELA 3 - SUPERMERCADOS
    private static final String TABLE_SM = "Supermercado";
    private static final String COLUMN_ID_SM = "_id";//coluna 0
    private static final String COLUMN_NOME_SM = "nomelocal";//coluna 1
    private static final String COLUMN_DESCRICAO_SM = "Descricao";//coluna 2
    private static final String COLUMN_TIPO_SM = "tipo";//coluna 3
    private static final String COLUMN_LAT_SM = "lat";//coluna 4
    private static final String COLUMN_LNG_SM = "lng";//coluna 5
    private static final String COLUMN_DATA_SM = "data";//coluna 6
    private static final String COLUMN_MORADA_SM = "morada";//coluna 7
    private static final String COLUMN_IMAGEM_SM = "imagem";//coluna 8


    //TABELA 1 - ESCOLA
    private static final String CRIAR_TABLE_ESCOLA = "CREATE TABLE " + TABLE_ESCOLA + "(" +
            COLUMN_ID_ESCOLA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NOME_ESCOLA + " TEXT, " +
            COLUMN_DESCRICAO_ESCOlA + " TEXT, " +
            COLUMN_TIPO_ESCOLA + " TEXT, " +
            COLUMN_LAT_ESCOLA + " VARCHAR(50), " +
            COLUMN_LNG_ESCOLA + " VARCHAR(50), " +
            COLUMN_DATA_ESCOLA + " DATETIME, " +
            COLUMN_MORADA_ESCOLA + " TEXT, " +
            COLUMN_IMAGEM_ESCOLA + " MEDIUMBLOB " +
            ");";

    //TABELA 2 - CAFÉS
    private static final String CRIAR_TABLE_CAFE = "CREATE TABLE " + TABLE_CAFE + "(" +
            COLUMN_ID_CAFE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NOME_CAFE + " TEXT, " +
            COLUMN_DESCRICAO_CAFE + " TEXT, " +
            COLUMN_TIPO_CAFE + " TEXT, " +
            COLUMN_LAT_CAFE + " VARCHAR(50), " +
            COLUMN_LNG_CAFE + " VARCHAR(50), " +
            COLUMN_DATA_CAFE + " DATETIME, " +
            COLUMN_MORADA_CAFE + " TEXT, " +
            COLUMN_IMAGEM_CAFE + " MEDIUMBLOB " +
            ");";

    //TABELA 3 - SUPERMERCADOS
    private static final String CRIAR_TABLE_SM = "CREATE TABLE " + TABLE_SM + "(" +
            COLUMN_ID_SM + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NOME_SM + " TEXT, " +
            COLUMN_DESCRICAO_SM + " TEXT, " +
            COLUMN_TIPO_SM + " TEXT, " +
            COLUMN_LAT_SM + " VARCHAR(50), " +
            COLUMN_LNG_SM + " VARCHAR(50), " +
            COLUMN_DATA_SM + " DATETIME, " +
            COLUMN_MORADA_SM + " TEXT, " +
            COLUMN_IMAGEM_SM + " MEDIUMBLOB " +
            ");";

    public DBTeste(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NOME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CRIAR_TABLE_ESCOLA);
        db.execSQL(CRIAR_TABLE_CAFE);
        db.execSQL(CRIAR_TABLE_SM);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ESCOLA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAFE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SM);
        onCreate(db);
    }

    //adicionar um registo na tabela escola
    public boolean addPontoEscola(String nome, String descricao, double lat, double lng, String morada, byte[] imagem) {

        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        values.put(COLUMN_NOME_ESCOLA, nome);
        values.put(COLUMN_DESCRICAO_ESCOlA, descricao);
        values.put(COLUMN_LAT_ESCOLA, lat);
        values.put(COLUMN_LNG_ESCOLA, lng);
        values.put(COLUMN_DATA_ESCOLA, getDateTime());
        values.put(COLUMN_MORADA_ESCOLA, morada);
        values.put(COLUMN_IMAGEM_ESCOLA, imagem);
        values.put(COLUMN_TIPO_ESCOLA, "Escola");
        db.insert(TABLE_ESCOLA, null, values);

        db.close();

        return true;
    }

    //adicionar um registo na tabela cafe
    public boolean addPontoCafe(String nome, String descricao, double lat, double lng, String morada, byte[] imagem) {

        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        values.put(COLUMN_NOME_CAFE, nome);
        values.put(COLUMN_DESCRICAO_CAFE, descricao);
        values.put(COLUMN_LAT_CAFE, lat);
        values.put(COLUMN_LNG_CAFE, lng);
        values.put(COLUMN_DATA_CAFE, getDateTime());
        values.put(COLUMN_MORADA_CAFE, morada);
        values.put(COLUMN_IMAGEM_CAFE, imagem);
        values.put(COLUMN_TIPO_CAFE, "Café");
        db.insert(TABLE_CAFE, null, values);

        db.close();

        return true;
    }

    //adicionar um registo na tabela supermercado
    public boolean addPontoSM(String nome, String descricao, double lat, double lng, String morada, byte[] imagem) {

        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        values.put(COLUMN_NOME_SM, nome);
        values.put(COLUMN_DESCRICAO_SM, descricao);
        values.put(COLUMN_LAT_SM, lat);
        values.put(COLUMN_LNG_SM, lng);
        values.put(COLUMN_DATA_SM, getDateTime());
        values.put(COLUMN_MORADA_SM, morada);
        values.put(COLUMN_IMAGEM_SM, imagem);
        values.put(COLUMN_TIPO_SM, "Supermercado");
        db.insert(TABLE_SM, null, values);

        db.close();

        return true;
    }

    //eliminar um registo da tabela cafe
    public boolean deletePontoCafe(int id) {

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CAFE + " WHERE " + COLUMN_ID_CAFE + "=\"" + id + "\";");

        return true;
    }

    //eliminar um registo da tabela escola
    public boolean deletePontoEscola(int id) {

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ESCOLA + " WHERE " + COLUMN_ID_ESCOLA + "=\"" + id + "\";");

        return true;
    }

    //eliminar um registo da tabela Supermercado
    public boolean deletePontoSM(int id) {

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_SM + " WHERE " + COLUMN_ID_SM + "=\"" + id + "\";");

        return true;
    }

    //imprimir a base de dados
    public Cursor getData(){

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_ESCOLA +
                " UNION SELECT * FROM " + TABLE_CAFE +
                " UNION SELECT * FROM " + TABLE_SM;
        Cursor data = db.rawQuery(query, null);
        return data;

    }

    public Cursor getTipobyNomeId (int id, String nome){

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_TIPO_CAFE + " FROM " + TABLE_CAFE + " WHERE " + COLUMN_NOME_CAFE + " = '" + nome + "' AND " + COLUMN_ID_CAFE + " = '" + id +
                "' UNION SELECT " + COLUMN_TIPO_ESCOLA + " FROM " + TABLE_ESCOLA + " WHERE " + COLUMN_NOME_ESCOLA + " = '" + nome + "' AND " + COLUMN_ID_ESCOLA + " = '" + id +
                "' UNION SELECT " + COLUMN_TIPO_SM + " FROM " + TABLE_SM + " WHERE " + COLUMN_NOME_SM + " = '" + nome + "' AND " + COLUMN_ID_SM + " = '" + id + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getIdbyNome(String nome){

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_ID_CAFE + " FROM " + TABLE_CAFE + " WHERE " + COLUMN_NOME_CAFE + " = '" + nome +
                "' UNION SELECT " + COLUMN_ID_ESCOLA + " FROM " + TABLE_ESCOLA + " WHERE " + COLUMN_NOME_ESCOLA + " = '" + nome +
                "' UNION SELECT " + COLUMN_ID_SM + " FROM " + TABLE_SM + " WHERE " + COLUMN_NOME_SM + " = '" + nome + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    private String getDateTime(){

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);

    }

    public Cursor getDataByIdCafe(int id){

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CAFE + " WHERE " + COLUMN_ID_CAFE + " = '" + id + "'";
        Cursor data = db.rawQuery(query, null);

        return data;
    }

    public Cursor getDataByIdEscola(int id){

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_ESCOLA + " WHERE " + COLUMN_ID_ESCOLA + " = '" + id + "'";
        Cursor data = db.rawQuery(query, null);

        return data;
    }

    public Cursor getDataByIdSM(int id){

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_SM + " WHERE " + COLUMN_ID_SM + " = '" + id + "'";
        Cursor data = db.rawQuery(query, null);

        return data;
    }

    public boolean UpdatePontoEscola(int id, String nome1, String desc, byte[] imagem){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        double lat, lng;
        String nome2, descr, morada, date;
        Cursor c = getDataByIdEscola(id);
        c.moveToFirst();
        lat = c.getDouble(4);
        lng = c.getDouble(5);
        date = c.getString(6);
        morada = c.getString(7);

        if(Objects.equals(nome1, "")) {

            nome2 = c.getString(1);
        } else {

            nome2 = nome1;
        }

        if(Objects.equals(desc, "")) {

            descr = c.getString(2);
        } else {

           descr = desc;
        }

        values.put(COLUMN_NOME_ESCOLA, nome2);
        values.put(COLUMN_DESCRICAO_ESCOlA, descr);
        values.put(COLUMN_LAT_ESCOLA, lat);
        values.put(COLUMN_LNG_ESCOLA, lng);
        values.put(COLUMN_DATA_ESCOLA, date);
        values.put(COLUMN_MORADA_ESCOLA, morada);
        values.put(COLUMN_IMAGEM_ESCOLA, imagem);
        values.put(COLUMN_TIPO_ESCOLA, "Escola");
        db.insert(TABLE_ESCOLA, null, values);

        db.execSQL("DELETE FROM " + TABLE_ESCOLA + " WHERE " + COLUMN_ID_ESCOLA + "=\"" + id + "\";");

        db.close();

        return true;
    }

    public boolean UpdatePontoCafe(int id, String nome1, String desc, byte[] imagem){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        double lat, lng;
        String nome2, descr, morada, date;
        Cursor c = getDataByIdCafe(id);
        c.moveToFirst();
        lat = c.getDouble(4);
        lng = c.getDouble(5);
        date = c.getString(6);
        morada = c.getString(7);

        if(Objects.equals(nome1, "")) {

            nome2 = c.getString(1);
        } else {

            nome2 = nome1;
        }

        if(Objects.equals(desc, "")) {

            descr = c.getString(2);
        } else {

            descr = desc;
        }

        values.put(COLUMN_NOME_CAFE, nome2);
        values.put(COLUMN_DESCRICAO_CAFE, descr);
        values.put(COLUMN_LAT_CAFE, lat);
        values.put(COLUMN_LNG_CAFE, lng);
        values.put(COLUMN_DATA_CAFE, date);
        values.put(COLUMN_MORADA_CAFE, morada);
        values.put(COLUMN_IMAGEM_CAFE, imagem);
        values.put(COLUMN_TIPO_CAFE, "Café");
        db.insert(TABLE_CAFE, null, values);

        db.execSQL("DELETE FROM " + TABLE_CAFE + " WHERE " + COLUMN_ID_CAFE + "=\"" + id + "\";");

        db.close();

        return true;
    }

    public boolean UpdatePontoSM(int id, String nome1, String desc, byte[] imagem){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        double lat, lng;
        String nome2, descr, morada, date;
        Cursor c = getDataByIdSM(id);
        c.moveToFirst();
        lat = c.getDouble(4);
        lng = c.getDouble(5);
        date = c.getString(6);
        morada = c.getString(7);

        if(Objects.equals(nome1, "")) {

            nome2 = c.getString(1);
        } else {

            nome2 = nome1;
        }

        if(Objects.equals(desc, "")) {

            descr = c.getString(2);
        } else {

            descr = desc;
        }

        values.put(COLUMN_NOME_SM, nome2);
        values.put(COLUMN_DESCRICAO_SM, descr);
        values.put(COLUMN_LAT_SM, lat);
        values.put(COLUMN_LNG_SM, lng);
        values.put(COLUMN_DATA_SM, date);
        values.put(COLUMN_MORADA_SM, morada);
        values.put(COLUMN_IMAGEM_SM, imagem);
        values.put(COLUMN_TIPO_SM, "Supermercado");
        db.insert(TABLE_SM, null, values);

        db.execSQL("DELETE FROM " + TABLE_SM + " WHERE " + COLUMN_ID_SM + "=\"" + id + "\";");

        db.close();

        return true;
    }

    public Cursor getDataEscola() {

        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_ESCOLA;
        Cursor data = db.rawQuery(query, null);
        return data;

        }

    public Cursor getDataCafe() {

        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_CAFE;
        Cursor data = db.rawQuery(query, null);
        return data;

        }

    public Cursor getDataSM() {

        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_SM;
        Cursor data = db.rawQuery(query, null);
        return data;

        }

    public Cursor getIdbyNomeEscola(String nome){

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_ID_ESCOLA + " FROM " + TABLE_ESCOLA + " WHERE " + COLUMN_NOME_ESCOLA + " = '" + nome + "'";
        Cursor data = db.rawQuery(query, null);
        return data;

        }

    public Cursor getIdbyNomeCafe(String nome){

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_ID_CAFE + " FROM " + TABLE_CAFE + " WHERE " + COLUMN_NOME_CAFE + " = '" + nome + "'";
        Cursor data = db.rawQuery(query, null);
        return data;

        }

    public Cursor getIdbyNomeSM(String nome){

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COLUMN_ID_SM + " FROM " + TABLE_SM + " WHERE " + COLUMN_NOME_SM + " = '" + nome + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
        }
    }

