package pt.novageo.niugisviewer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

/**
 * Created by estagiario on 20/04/2017. (ºbº)
 */

public class Activity_mudar extends AppCompatActivity {

    EditText novonome, novodesc;
    ImageView mudarFoto;
    DBTeste db;
    String tipo;
    int id;
    Cursor c;
    final int REQUEST_CODE_GALLERY = 970;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mudar);
        novonome = (EditText) findViewById(R.id.inserirnome);
        novodesc = (EditText) findViewById(R.id.inserirdesc);
        mudarFoto = (ImageView) findViewById(R.id.FotoMuda);
        db = new DBTeste(this, null, null, 20);
        id = getIntent().getIntExtra("ID", 0);
        tipo = getIntent().getStringExtra("TIPO");
        mudarFoto.setImageBitmap(ByteToImageView());
    }

    public void UpdateOnClick(View view) {

        Intent inf = new Intent(this, Activity_informacao.class);

        if (Objects.equals(tipo, "Café")) {

            db.UpdateNomeCafe(id, novonome.getText().toString());
            db.UpdateDescCafe(id, novodesc.getText().toString());
            db.UpdateFotoCafe(id, imageViewToByte(mudarFoto));
        } else if (Objects.equals(tipo, "Escola")) {

            db.UpdateNomeEscola(id, novonome.getText().toString());
            db.UpdateDescEscola(id, novodesc.getText().toString());
            db.UpdateFotoEscola(id, imageViewToByte(mudarFoto));
        } else if (Objects.equals(tipo, "Supermercado")) {

            db.UpdateNomeSM(id, novonome.getText().toString());
            db.UpdateDescSM(id, novodesc.getText().toString());
            db.UpdateFotoSM(id, imageViewToByte(mudarFoto));
        }

        inf.putExtra("ID", id);
        inf.putExtra("TIPO", tipo);
        finish();
        startActivity(inf);
    }

    public void MudarImagem(View view) {

        ActivityCompat.requestPermissions(Activity_mudar.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                Intent img = new Intent(Intent.ACTION_PICK);
                img.setType("image/*");
                startActivityForResult(img, REQUEST_CODE_GALLERY);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = data.getData();

        try {

            InputStream stream = getContentResolver().openInputStream(uri);

            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            mudarFoto.setImageBitmap(bitmap);

        } catch (FileNotFoundException e ) {

            Toast.makeText(this, "Foto não encontrado", Toast.LENGTH_SHORT).show();

        }

    }

    public Bitmap ByteToImageView(){

        Cursor c = db.getDataById(id);

        c.moveToFirst();
        byte[] fotoimage = c.getBlob(8);
        Bitmap bitmap = BitmapFactory.decodeByteArray(fotoimage, 0, fotoimage.length);

        return bitmap;

    }

    private byte[] imageViewToByte(ImageView image){

        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        return byteArray;
    }

}
