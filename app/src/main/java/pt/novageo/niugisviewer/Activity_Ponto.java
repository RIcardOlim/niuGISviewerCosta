package pt.novageo.niugisviewer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by estagiario on 31/03/2017.
 *
 */

public class Activity_Ponto extends AppCompatActivity {

    EditText inserirnome;
    EditText inserirdesc;
    TextView textView12;
    DBTeste db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ponto);
        inserirnome = (EditText) findViewById(R.id.inserirnome);
        inserirdesc = (EditText) findViewById(R.id.inserirdesc);
        textView12 = (TextView) findViewById(R.id.textView12);
        db = new DBTeste(this, null, null, 2);
        printDatabase();

    }

    //adicionar registo da base de dados
    public void addButtonClicked(View view){

        Locais nome = new Locais(inserirnome.getText().toString());
        Locais desc = new Locais(inserirdesc.getText().toString());
        db.addPonto(nome, desc);
        printDatabase();

    }

    //apagar registo da base de dados
    public void deleteButtonClcked(View view){

        String inputText = inserirnome.getText().toString();
        db.deleteLocal(inputText);
        printDatabase();

    }

    public void printDatabase() {

        String dbString = db.databaseToString();
        textView12.setText(dbString);
        inserirnome.setText("");
        inserirdesc.setText("");

    }

}
