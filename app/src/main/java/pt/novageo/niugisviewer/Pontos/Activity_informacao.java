package pt.novageo.niugisviewer.Pontos;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import pt.novageo.niugisviewer.DB_ponto.AppDatabase;
import pt.novageo.niugisviewer.R;
import pt.novageo.niugisviewer.Tabela.Escola;

/**
 * Created by estagiario on 13/04/2017. (ºbº)
 */

public class Activity_informacao extends AppCompatActivity {

    TextView textLat, textLng ,textDesc, textNome, textData, textMorada;
    ImageView FotoView;
    String dbstring, tipo;
    int id;
    Cursor c;
    Escola escola;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inf);
        textNome = (TextView) findViewById(R.id.textNome);
        textDesc = (TextView) findViewById(R.id.textDesc);
        textLat = (TextView) findViewById(R.id.textLat);
        textLng = (TextView) findViewById(R.id.textLng);
        textData = (TextView) findViewById(R.id.textData);
        textMorada = (TextView) findViewById(R.id.textMorada);
        FotoView = (ImageView) findViewById(R.id.imageView7);
        id = getIntent().getIntExtra("ID", 0);
        tipo = getIntent().getStringExtra("TIPO");
        publicar();

    }

    private void publicar() {

        checkTipo();

        c.moveToFirst();
        dbstring = c.getString(1);
        textNome.setText(dbstring);

        c.moveToFirst();
        dbstring = c.getString(2);
        textDesc.setText(dbstring);

        c.moveToFirst();
        dbstring = c.getString(4);
        textLat.setText(dbstring);

        c.moveToFirst();
        dbstring = c.getString(5);
        textLng.setText(dbstring);

        c.moveToFirst();
        dbstring = c.getString(6);
        textData.setText(dbstring);

        c.moveToFirst();
        dbstring = c.getString(7);
        textMorada.setText(dbstring);

        FotoView.setImageBitmap(ByteToImageView());

    }

    public void checkTipo() {

        if (Objects.equals(tipo, "Café")) {

        } else if (Objects.equals(tipo, "Escola")) {

            c = AppDatabase.getAppDatabase(this).escolaDao().findDatabyID(id);
        } else if (Objects.equals(tipo, "Supermercado")) {

        }
    }

    public void DeleteOnClick (View view) {

        Intent inf = new Intent(this, Activity_ListData.class);
        if (Objects.equals(tipo, "Café")) {

        } else if (Objects.equals(tipo, "Escola")) {

        escola = AppDatabase.getAppDatabase(this).escolaDao().findDataToDelbyID(id);
            AppDatabase.getAppDatabase(this).escolaDao().delete(escola);
        } else if (Objects.equals(tipo, "Supermercado")) {

        }
        finish();
        startActivity(inf);

    }

    public void MudarOnClick (View view) {

        Intent mudar = new Intent(this, Activity_mudar.class);
        mudar.putExtra("ID", id);
        mudar.putExtra("TIPO", tipo);
        finish();
        startActivity(mudar);

    }

    public Bitmap ByteToImageView(){

        byte[] fotoimage = c.getBlob(8);
        Bitmap bitmap = BitmapFactory.decodeByteArray(fotoimage, 0, fotoimage.length);
        return bitmap;
    }
}
