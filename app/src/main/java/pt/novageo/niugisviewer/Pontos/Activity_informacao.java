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
import pt.novageo.niugisviewer.Tabela.Supermercado;

/**
 * Created by estagiario on 13/04/2017. (ºbº)
 */

public class Activity_informacao extends AppCompatActivity {

    TextView textLat, textLng ,textDesc, textNome, textData, textMorada;
    ImageView FotoView;
    String coorString, tipo;
    int id;
    Escola escola;
    Supermercado superm;

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

        if (Objects.equals(tipo, "Café")) {

        } else if (Objects.equals(tipo, "Escola")) {

            textNome.setText(escola.getNomePonto());
            textDesc.setText(escola.getDescricaoPonto());
            coorString = Double.toString(escola.getLatPonto());
            textLat.setText(coorString);
            coorString = Double.toString(escola.getLngPonto());
            textLng.setText(coorString);
            textData.setText(escola.getDataPonto());
            textMorada.setText(escola.getMoradaPonto());
        } else if (Objects.equals(tipo, "Supermercado")) {

            textNome.setText(superm.getNomePonto());
            textDesc.setText(superm.getDescPonto());
            coorString = Double.toString(superm.getLatPonto());
            textLat.setText(coorString);
            coorString = Double.toString(superm.getLngPonto());
            textLng.setText(coorString);
            textData.setText(superm.getDataPonto());
            textMorada.setText(superm.getMoradaPonto());
        }



        FotoView.setImageBitmap(ByteToImageView());
    }

    public void checkTipo() {

        if (Objects.equals(tipo, "Café")) {

        } else if (Objects.equals(tipo, "Escola")) {

            escola = AppDatabase.getAppDatabase(this).escolaDao().findDatabyID(id);
        } else if (Objects.equals(tipo, "Supermercado")) {

            superm = AppDatabase.getAppDatabase(this).supermercadoDao().findDatabyID(id);
        }
    }

    public void DeleteOnClick (View view) {

        Intent inf = new Intent(this, Activity_ListData.class);
        if (Objects.equals(tipo, "Café")) {

        } else if (Objects.equals(tipo, "Escola")) {

            AppDatabase.getAppDatabase(this).escolaDao().delete(escola);
        } else if (Objects.equals(tipo, "Supermercado")) {

            AppDatabase.getAppDatabase(this).supermercadoDao().delete(superm);
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

        byte[] fotoimage = new byte[0];

        if (Objects.equals(tipo, "Café")) {

        } else if (Objects.equals(tipo, "Escola")) {

            fotoimage = escola.getImagemPonto();
        } else if (Objects.equals(tipo, "Supermercado")) {

            fotoimage = superm.getImagemPonto();
        }

        Bitmap bitmap = BitmapFactory.decodeByteArray(fotoimage, 0, fotoimage.length);
        return bitmap;
    }
}
