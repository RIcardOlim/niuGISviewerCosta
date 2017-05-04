package pt.novageo.niugisviewer;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by estagiario on 13/04/2017. (ºbº)
 */

public class Activity_informacao extends AppCompatActivity {

    TextView textLat, textLng ,textDesc, textNome, textData, textMorada, textEstado;
    DBTeste db;
    ImageView FotoView;
    String dbstring;
    int id;

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
        db = new DBTeste(this, null, null, 14);
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

        c.moveToFirst();
        dbstring = c.getString(5);
        textData.setText(dbstring);

        c.moveToFirst();
        dbstring = c.getString(6);
        textMorada.setText(dbstring);

        FotoView.setImageBitmap(ByteToImageView());

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

    public Bitmap ByteToImageView(){

        Cursor c = db.getDataById(id);

        c.moveToFirst();
        byte[] fotoimage = c.getBlob(7);
        Bitmap bitmap = BitmapFactory.decodeByteArray(fotoimage, 0, fotoimage.length);

        return bitmap;
    }

}
