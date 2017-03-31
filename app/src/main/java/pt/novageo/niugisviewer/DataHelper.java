package pt.novageo.niugisviewer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataHelper {

    private static final String DATABASE_NAME = "@string/niugisviewer";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME    = "@string/layers";

    private SQLiteDatabase db;

    private SQLiteStatement insertStmt;
    //private SQLiteStatement updateStmt;
    private static final String INSERT = "insert into "
            + TABLE_NAME + " (@string/name, title, active) values (?,?, true)";
    //private static final String UPDATE = "update " + TABLE_NAME + " set active = ?";

    public DataHelper(Context context) {
        OpenHelper openHelper = new OpenHelper(context);
        this.db = openHelper.getWritableDatabase();
        this.insertStmt = this.db.compileStatement(INSERT);
        //this.updateStmt = this.db.compileStatement(UPDATE);
    }

    public long insert(String name, String title) {
        this.insertStmt.bindString(1, name);
        this.insertStmt.bindString(2, title);
        return this.insertStmt.executeInsert();
    }
/*
    public long update(boolean active) {
        this.updateStmt.bindString(1, String.valueOf(active));
        return this.updateStmt.executeUpdateDelete();
    }
*/
    public void deleteAll() {
        this.db.delete(TABLE_NAME, null, null);
    }

    public List<String> selectAll() {
        List<String> list = new ArrayList<>();
        Cursor cursor = this.db.query(TABLE_NAME, new String[] { "@string/name" },
                null, null, null, null, "@string/name desc");
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }

    private static class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("@string/CREATE TABLE " + TABLE_NAME + " (@string/id_INTEGER_PRIMARY_KEY, name TEXT, title TEXT, active BOOLEAN)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("@string/Example", "Upgrading database, this will drop tables and recreate.");
            db.execSQL("@string/DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}