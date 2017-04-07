package pt.novageo.niugisviewer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by estagiario on 31/03/2017.
 *
 */

public class Activity_Ponto extends AppCompatActivity {

    EditText inserirnome, inserirdesc;
    TextView textView12;
    DBTeste db;
    String coordLat, coordLng;
    double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ponto);
        inserirnome = (EditText) findViewById(R.id.inserirnome);
        inserirdesc = (EditText) findViewById(R.id.inserirdesc);
        textView12 = (TextView) findViewById(R.id.textView12);
        coordLat = getIntent().getStringExtra("coordLat");
        coordLng = getIntent().getStringExtra("coordLng");
        lat = Double.parseDouble(coordLat);
        lng = Double.parseDouble(coordLng);
        db = new DBTeste(this, null, null, 6);
        printDatabase();
    //    Toast.makeText(this, coordLat, Toast.LENGTH_SHORT).show();
    //    Toast.makeText(this, coordLng, Toast.LENGTH_SHORT).show();

    }

    //adicionar registo da base de dados
    public void addButtonClicked(View view){

        db.addPonto(inserirnome.getText().toString(), inserirdesc.getText().toString(), lat, lng);
        Toast.makeText(this, "Ponto Adicionado", Toast.LENGTH_SHORT).show();
        printDatabase();

    }

    //apagar registo da base de dados
    public void deleteButtonClcked(View view){

        String inputText = inserirnome.getText().toString();
        db.deleteLocal(inputText);
        Toast.makeText(this, "Ponto Removido", Toast.LENGTH_SHORT).show();
        printDatabase();

    }

    public void printDatabase() {

        String dbString = db.databaseToString();
        textView12.setText(dbString);
        inserirnome.setText("");
        inserirdesc.setText("");

    }

}
