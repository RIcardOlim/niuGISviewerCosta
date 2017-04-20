package pt.novageo.niugisviewer;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nome = listView.getItemAtPosition(position).toString();
                Cursor data = db.getIdbyNome(nome);
                int itemID = -1;
                while(data.moveToNext()){
                    itemID = data.getInt(0);

                }
                if(itemID > -1){
                    final Intent inf = new Intent(Activity_ListData.this, Activity_informacao.class);
                    inf.putExtra("ID", itemID);
                    data.close();
                    db.close();
                    finish();
                    startActivity(inf);

                } else Toast.makeText(Activity_ListData.this, "Não existe esse ID", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
