package pt.novageo.niugisviewer;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by estagiario on 07/04/2017.
 *
 */

public class Activity_ListData extends AppCompatActivity {

    DBTeste db;
    private ListView listView;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listdata);
        listView = (ListView) findViewById(R.id.listView);
        db = new DBTeste(this, null, null, 6);
        ocuparListView();

    }

    private void ocuparListView() {

        Cursor data = db.getData();
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()){
            listData.add(data.getString(1));
        }
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listView.setAdapter(adapter);
    }

}
