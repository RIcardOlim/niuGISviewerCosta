package pt.novageo.niugisviewer;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by estagiario on 20/04/2017. (ºbº)
 */

public class Activity_mudar extends AppCompatActivity {

    EditText novonome, novodesc;
    DBTeste db;
    int id;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mudar);
        novonome = (EditText) findViewById(R.id.inserirnome);
        novodesc = (EditText) findViewById(R.id.inserirdesc);
        db = new DBTeste(this, null, null, 14);
        id = getIntent().getIntExtra("ID", 0);

    }

    public void UpdateOnClick(View view) {

        Intent inf = new Intent(this, Activity_informacao.class);
        db.UpdateNome(id, novonome.getText().toString());
        db.UpdateDesc(id, novodesc.getText().toString());
        inf.putExtra("ID", id);
        finish();
        startActivity(inf);

    }

}
