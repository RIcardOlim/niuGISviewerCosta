package pt.novageo.niugisviewer.Pontos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pt.novageo.niugisviewer.DB_ponto.AppDatabase;
import pt.novageo.niugisviewer.R;
import pt.novageo.niugisviewer.Tabela.Cafe;
import pt.novageo.niugisviewer.Tabela.Escola;
import pt.novageo.niugisviewer.Tabela.Supermercado;

public class Activity_ListData extends AppCompatActivity {

    private ListView listView1, listView2;
    String tipo;
    boolean escola, cafe, supermercado;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listdata);
        listView1 = (ListView) findViewById(R.id.listView);
        listView2 = (ListView) findViewById(R.id.listView);
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

        List<Escola> escola = AppDatabase.getAppDatabase(this).escolaDao().getAll();
        String nome;
        ArrayList<String> listData = new ArrayList<>();
        for (Escola esc : escola) {

            nome = esc.getNomePonto();
            listData.add(nome);
        }

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listView1.setAdapter(adapter);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Escola escola;
                String nome = listView1.getItemAtPosition(position).toString();
                escola = AppDatabase.getAppDatabase(Activity_ListData.this).escolaDao().findDatabyNome(nome);
                int itemID = -1;
                itemID = escola.get_id();
                if (itemID > -1) {
                    final Intent inf = new Intent(Activity_ListData.this, Activity_informacao.class);
                    inf.putExtra("ID", itemID);
                    inf.putExtra("TIPO", tipo);
                    finish();
                    startActivity(inf);
                } else
                    Toast.makeText(Activity_ListData.this, "Não existe esse ID", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ocuparListViewSM() {

        List<Supermercado> supermercado = AppDatabase.getAppDatabase(this).supermercadoDao().getAll();
        String nome;
        ArrayList<String> listData = new ArrayList<>();
        for(Supermercado superm : supermercado){

           nome = superm.getNomePonto();
           listData.add(nome);
        }

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listView1.setAdapter(adapter);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Supermercado SM;
                String nome = listView1.getItemAtPosition(position).toString();
                SM = AppDatabase.getAppDatabase(Activity_ListData.this).supermercadoDao().findDatabyNome(nome);
                int itemID = -1;
                itemID = SM.get_id();
                if(itemID > -1){
                    final Intent inf = new Intent(Activity_ListData.this, Activity_informacao.class);
                    inf.putExtra("ID", itemID);
                    inf.putExtra("TIPO", tipo);
                    finish();
                    startActivity(inf);
                } else Toast.makeText(Activity_ListData.this, "Não existe esse ID", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void ocuparListViewCafe() {

        List<Cafe> cafe = AppDatabase.getAppDatabase(this).cafeDao().getAll();
        String nome;
        ArrayList < String > listData = new ArrayList<>();
        for(Cafe caf : cafe){

            nome = caf.getNomePonto();
            listData.add(nome);
        }
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listView1.setAdapter(adapter);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cafe cafe;
                String nome = listView1.getItemAtPosition(position).toString();
                cafe = AppDatabase.getAppDatabase(Activity_ListData.this).cafeDao().findDatabyNome(nome);
                int itemID = -1;
                itemID = cafe.get_id();
                if(itemID > -1){
                    final Intent inf = new Intent(Activity_ListData.this, Activity_informacao.class);
                    inf.putExtra("ID", itemID);
                    inf.putExtra("TIPO", tipo);
                    finish();
                    startActivity(inf);
                } else Toast.makeText(Activity_ListData.this, "Não existe esse ID", Toast.LENGTH_SHORT).show();
            }
        });

    }


        @Override
        public void onBackPressed () {

            if (escola || cafe || supermercado) {

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