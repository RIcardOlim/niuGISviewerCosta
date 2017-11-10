package pt.novageo.niugisviewer.DB_ponto;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import pt.novageo.niugisviewer.Tabela.Escola;

/**
 * Created by ricardo on 09-11-2017.
 * :)
 */

public class DBInitializer {

    private static final String TAG = DBInitializer.class.getName();

    public static void populateAsync(@NonNull final AppDatabase db, Escola escola) {
        PopulateDbAsync task = new PopulateDbAsync(db, escola);
        task.execute();
    }

    private static Escola addPonto(final AppDatabase db, Escola escola) {
        db.escolaDao().insert(escola);
        return escola;
    }

    private static void populateWithTestData(AppDatabase db, Escola escola) {

        addPonto(db, escola);

        List<Escola> userList = db.escolaDao().getAll();
        Log.d(DBInitializer.TAG, "Rows Count: " + userList.size());
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;
        private final Escola mescola;

        PopulateDbAsync(AppDatabase db, Escola escola) {
            mDb = db;
            mescola = escola;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(mDb, mescola);
            return null;
        }

    }
}
