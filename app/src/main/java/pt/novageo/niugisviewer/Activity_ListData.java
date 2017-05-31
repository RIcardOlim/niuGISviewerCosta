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
import java.util.Objects;

public class Activity_ListData extends AppCompatActivity {

    DBTeste db;
    private ListView listView1, listView2;
    String tipo;
    boolean escola, cafe, supermercado;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listdata);
        listView1 = (ListView) findViewById(R.id.listView);
        listView2 = (ListView) findViewById(R.id.listView);
        db = new DBTeste(this, null, null, 20);
        escola = false;
        cafe = false;
        supermercado = false;
        tabelaListView();
    }

    private void tabelaListView() {

        ArrayList<String> listData = new ArrayList<>();
        listData.add("Escola");
        listData.add("Café");
        listData.add("Supermercado");
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listView2.setAdapter(adapter);

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tipo = listView1.getItemAtPosition(position).toString();
                if (Objects.equals(tipo, "Escola")) {

                    escola = true;
                    ocuparListViewEscola();
                } else if (Objects.equals(tipo, "Café")) {

                    cafe = true;
                    ocuparListViewCafe();
                } else if (Objects.equals(tipo, "Supermercado")) {

                    supermercado = true;
                    ocuparListViewSM();
                }
            }
        });

    }

    private void ocuparListViewEscola() {

        Cursor data = db.getDataEscola();
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()){

            listData.add(data.getString(1));
        }
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listView1.setAdapter(adapter);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nome = listView1.getItemAtPosition(position).toString();
                Cursor data = db.getIdbyNomeEscola(nome);
                int itemID = -1;
                while(data.moveToNext()){

                    itemID = data.getInt(0);
                }
                if(itemID > -1){
                    final Intent inf = new Intent(Activity_ListData.this, Activity_informacao.class);
                    inf.putExtra("ID", itemID);
                    inf.putExtra("TIPO", tipo);
                    data.close();
                    db.close();
                    finish();
                    startActivity(inf);

                } else Toast.makeText(Activity_ListData.this, "Não existe esse ID", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ocuparListViewCafe() {

        Cursor data = db.getDataCafe();
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()){

            listData.add(data.getString(1));
        }
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listView1.setAdapter(adapter);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nome = listView1.getItemAtPosition(position).toString();
                Cursor data = db.getIdbyNomeCafe(nome);
                int itemID = -1;
                while(data.moveToNext()){

                    itemID = data.getInt(0);
                }
                if(itemID > -1){
                    final Intent inf = new Intent(Activity_ListData.this, Activity_informacao.class);
                    inf.putExtra("ID", itemID);
                    inf.putExtra("TIPO", tipo);
                    data.close();
                    db.close();
                    finish();
                    startActivity(inf);

                } else Toast.makeText(Activity_ListData.this, "Não existe esse ID", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ocuparListViewSM() {

        Cursor data = db.getDataSM();
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()){

            listData.add(data.getString(1));
        }
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listView1.setAdapter(adapter);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nome = listView1.getItemAtPosition(position).toString();
                Cursor data = db.getIdbyNomeSM(nome);
                int itemID = -1;
                while(data.moveToNext()){

                    itemID = data.getInt(0);
                }
                if(itemID > -1){
                    final Intent inf = new Intent(Activity_ListData.this, Activity_informacao.class);
                    inf.putExtra("ID", itemID);
                    inf.putExtra("TIPO", tipo);
                    data.close();
                    db.close();
                    finish();
                    startActivity(inf);

                } else Toast.makeText(Activity_ListData.this, "Não existe esse ID", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {

        if(escola || cafe || supermercado){

            escola = false;
            cafe = false;
            supermercado = false;
            tabelaListView();
        } else {

            super.onBackPressed();
            finish();
        }
    }
}
