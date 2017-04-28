package pt.novageo.niugisviewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by estagiario on 31/03/2017.
 *
 */

public class Activity_Ponto extends AppCompatActivity {

    EditText inserirnome, inserirdesc;
    DBTeste db;
    String coordLat, coordLng;
    double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ponto);
        inserirnome = (EditText) findViewById(R.id.inserirnome);
        inserirdesc = (EditText) findViewById(R.id.inserirdesc);
        coordLat = getIntent().getStringExtra("coordLat");
        coordLng = getIntent().getStringExtra("coordLng");
        lat = Double.parseDouble(coordLat);
        lng = Double.parseDouble(coordLng);
        db = new DBTeste(this, null, null, 9);
        resetText();
       // Toast.makeText(this, coordLat + coordLng, Toast.LENGTH_SHORT).show();

    }

    //adicionar registo da base de dados
    public void addButtonClicked(View view){

        db.addPonto(inserirnome.getText().toString(), inserirdesc.getText().toString(), lat, lng);
        Toast.makeText(this, "Ponto Adicionado", Toast.LENGTH_SHORT).show();
        resetText();

    }

    public void ViewButtonClcked(View view){

        Intent view2 = new Intent(this, Activity_ListData.class);
        db.close();
        finish();
        startActivity(view2);


    }

    public void resetText() {

        inserirnome.setText("");
        inserirdesc.setText("");

    }

}
