package pt.novageo.niugisviewer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;


/**
 * Created by estagiario on 31/03/2017.
 *
 */

public class Activity_Ponto extends AppCompatActivity {

    EditText inserirnome, inserirdesc;
    ImageView FotoView;
    DBTeste db;
    String coordLat, coordLng, morada, nome, desc;
    double lat, lng;
    final int REQUEST_CODE_GALLERY = 969;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ponto);
        inserirnome = (EditText) findViewById(R.id.inserirnome);
        inserirdesc = (EditText) findViewById(R.id.inserirdesc);
        FotoView = (ImageView) findViewById(R.id.imageView8);
        coordLat = getIntent().getStringExtra("coordLat");
        coordLng = getIntent().getStringExtra("coordLng");
        morada = getIntent().getStringExtra("morada");
        lat = Double.parseDouble(coordLat);
        lng = Double.parseDouble(coordLng);
        db = new DBTeste(this, null, null, 14);

        resetText();
       // Toast.makeText(this, coordLat + coordLng, Toast.LENGTH_SHORT).show();

    }

    //adicionar registo da base de dados
    public void addButtonClicked(View view){

        nome = inserirnome.getText().toString();
        desc = inserirdesc.getText().toString();

        if(Objects.equals(nome, "")) {
            Toast.makeText(this, "Nome do ponto obrigatório", Toast.LENGTH_SHORT).show();
        } else {
            if(Objects.equals(desc, "")) {
                Toast.makeText(this, "Descrição do ponto obrigatório", Toast.LENGTH_SHORT).show();
            } else {
                db.addPonto(nome, desc, lat, lng, morada, imageViewToByte(FotoView));
                Toast.makeText(this, "Ponto Adicionado", Toast.LENGTH_SHORT).show();
                resetText();
            }
        }
    }

    public void ViewButtonClcked(View view){

        Intent view2 = new Intent(this, Activity_ListData.class);
        db.close();
        finish();
        startActivity(view2);

    }

    public void MudarImagem(View view) {

        ActivityCompat.requestPermissions(Activity_Ponto.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);

    }

    private void resetText() {

        inserirnome.setText("");
        inserirdesc.setText("");
        FotoView.setImageResource(R.mipmap.ponto);

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
            FotoView.setImageBitmap(bitmap);

        } catch (FileNotFoundException e ) {

            Toast.makeText(this, "Foto não encontrado", Toast.LENGTH_SHORT).show();

        }

    }

    private byte[] imageViewToByte(ImageView image){

        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        return byteArray;
    }

}
