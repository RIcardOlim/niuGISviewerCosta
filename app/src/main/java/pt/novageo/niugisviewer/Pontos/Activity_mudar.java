package pt.novageo.niugisviewer.Pontos;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

import pt.novageo.niugisviewer.DB_ponto.DBTeste;
import pt.novageo.niugisviewer.Manifest;
import pt.novageo.niugisviewer.R;

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
    Boolean galeria, camera;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mudar);
        novonome = (EditText) findViewById(R.id.inserirnome);
        novodesc = (EditText) findViewById(R.id.inserirdesc);
        mudarFoto = (ImageView) findViewById(R.id.FotoMuda);
        db = new DBTeste(this, null, null, 20);
        id = getIntent().getIntExtra("ID", 0);
        tipo = getIntent().getStringExtra("TIPO");
        galeria = false;
        camera = false;
        checkTipo();
        mudarFoto.setImageBitmap(ByteToImageView());

    }

    public void checkTipo() {

        if (Objects.equals(tipo, "Café")) {

            c = db.getDataByIdCafe(id);
        } else if (Objects.equals(tipo, "Escola")) {

            c = db.getDataByIdEscola(id);
        } else if (Objects.equals(tipo, "Supermercado")) {

            c = db.getDataByIdSM(id);
        }
    }

    public void UpdateOnClick(View view) {

        Intent inf = new Intent(this, Activity_ListData.class);

        if (Objects.equals(tipo, "Café")) {

            db.UpdatePontoCafe(id, novonome.getText().toString(), novodesc.getText().toString(), imageViewToByte(mudarFoto));
        } else if (Objects.equals(tipo, "Escola")) {

            db.UpdatePontoEscola(id, novonome.getText().toString(), novodesc.getText().toString(), imageViewToByte(mudarFoto));
        } else if (Objects.equals(tipo, "Supermercado")) {

            db.UpdatePontoSM(id, novonome.getText().toString(), novodesc.getText().toString(), imageViewToByte(mudarFoto));
        }

        startActivity(inf);
    }

    public void MudarImagem(View view) {

        AlertDialog.Builder escolhe = new AlertDialog.Builder(Activity_mudar.this);
        escolhe.setTitle(R.string.escolhe);
        escolhe.setItems(R.array.Como_escolher_imagem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(which == 0){

                    Toast.makeText(Activity_mudar.this, "galeria", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(Activity_mudar.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
                    galeria = true;
                } else {

                    Toast.makeText(Activity_mudar.this, "câmera", Toast.LENGTH_SHORT).show();
                    camera = true;
                    abrirCamera();
                }

            }
        });

        AlertDialog alertDialog = escolhe.create();
        alertDialog.show();
    }

    public void  abrirCamera(){

        Intent it = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(it, REQUEST_CODE_GALLERY);
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

        if(galeria) {

            if(data != null) {

                Uri uri = data.getData();

                try {

                    InputStream stream = getContentResolver().openInputStream(uri);

                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    mudarFoto.setImageBitmap(bitmap);

                } catch (FileNotFoundException e) {

                    Toast.makeText(this, "Foto não encontrado", Toast.LENGTH_SHORT).show();
                }

            }
            galeria = false;
        }

        if(camera) {

            if (RESULT_OK == resultCode) {

                Bundle extras = data.getExtras();
                Bitmap bmp = (Bitmap) extras.get("data");

                mudarFoto = (ImageView) findViewById(R.id.FotoMuda);
                mudarFoto.setImageBitmap(bmp);
            }

            camera = false;
        }
    }

    public Bitmap ByteToImageView(){

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