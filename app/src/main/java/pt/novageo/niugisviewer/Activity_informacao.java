package pt.novageo.niugisviewer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by estagiario on 13/04/2017. (ºbº)
 */

public class Activity_informacao extends AppCompatActivity {

    TextView textLat, textLng ,textDesc, textNome;
    DBTeste db;
    String dbstring;
    int id;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inf);
        textNome = (TextView) findViewById(R.id.textNome);
        textDesc = (TextView) findViewById(R.id.textDesc);
        textLat = (TextView) findViewById(R.id.textLat);
        textLng = (TextView) findViewById(R.id.textLng);
        id = getIntent().getIntExtra("ID", 0);
        db = new DBTeste(this, null, null, 6);
        publicar();

    }

    private void publicar() {

        Cursor c = db.getDataById(id);

        c.moveToFirst();
        dbstring = c.getString(1);
        textNome.setText(dbstring);

        c.moveToFirst();
        dbstring = c.getString(2);
        textDesc.setText(dbstring);

        c.moveToFirst();
        dbstring = c.getString(3);
        textLat.setText(dbstring);

        c.moveToFirst();
        dbstring = c.getString(4);
        textLng.setText(dbstring);

    }

    public void DeleteOnClick (View view) {

        Intent inf = new Intent(this, Activity_ListData.class);
        db.deletePonto(id);
        finish();
        startActivity(inf);

    }

    public void MudarOnClick (View view) {

        Intent mudar = new Intent(this, Activity_mudar.class);
        mudar.putExtra("ID", id);
        finish();
        startActivity(mudar);

    }

}
