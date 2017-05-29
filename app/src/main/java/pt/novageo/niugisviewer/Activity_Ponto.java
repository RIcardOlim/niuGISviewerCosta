package pt.novageo.niugisviewer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
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
    String coordLat, coordLng, morada, nome, desc, tipo;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    double lat, lng;
    Boolean galeria, camera;
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
        galeria = false;
        camera = false;
        db = new DBTeste(this, null, null, 20);

        spinner = (Spinner) findViewById(R.id.IDspinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.Tipo_de_Ponto, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getBaseContext(),parent.getItemAtPosition(position) + " foi selecionado", Toast.LENGTH_SHORT).show();
                tipo = spinner.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        resetText();

    }

    //adicionar registo da base de dados
    public void addButtonClicked(View view){

        tipo = spinner.getSelectedItem().toString();
        nome = inserirnome.getText().toString();
        desc = inserirdesc.getText().toString();

        try{
        if(Objects.equals(nome, "")) {
            Toast.makeText(this, "Nome do ponto obrigatório", Toast.LENGTH_SHORT).show();

            } else if (Objects.equals(tipo, "Escola")) {

                db.addPontoEscola(nome, desc, lat, lng, morada, imageViewToByte(FotoView));
                Toast.makeText(this, "Ponto Adicionado", Toast.LENGTH_SHORT).show();
                resetText();
            } else if (Objects.equals(tipo, "Café")) {

                db.addPontoCafe(nome, desc, lat, lng, morada, imageViewToByte(FotoView));
                Toast.makeText(this, "Ponto Adicionado", Toast.LENGTH_SHORT).show();
                resetText();
            } else if (Objects.equals(tipo, "Supermercado")) {

            db.addPontoSM(nome, desc, lat, lng, morada, imageViewToByte(FotoView));
            Toast.makeText(this, "Ponto Adicionado", Toast.LENGTH_SHORT).show();
            resetText();
                }
            } catch (Exception e){

            e.printStackTrace();
        }
    }

    public void ViewButtonClcked(View view){

        Intent view2 = new Intent(this, Activity_ListData.class);
        db.close();
        finish();
        startActivity(view2);
    }

    public void MudarImagem(View view) {

        AlertDialog.Builder escolhe = new AlertDialog.Builder(Activity_Ponto.this);
        escolhe.setTitle(R.string.escolhe);
        escolhe.setItems(R.array.Como_escolher_imagem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(which == 0){

                    Toast.makeText(Activity_Ponto.this, "galeria", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(Activity_Ponto.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
                    galeria = true;
                } else {

                    Toast.makeText(Activity_Ponto.this, "câmera", Toast.LENGTH_SHORT).show();
                    camera = true;
                }

            }
        });

        AlertDialog alertDialog = escolhe.create();

        alertDialog.show();

   }

    private void resetText() {

        inserirnome.setText("");
        inserirdesc.setText("");
        FotoView.setImageResource(R.mipmap.ponto);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(galeria) {

            if (requestCode == REQUEST_CODE_GALLERY) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent img = new Intent(Intent.ACTION_PICK);
                    img.setType("image/*");
                    startActivityForResult(img, REQUEST_CODE_GALLERY);

                }
            }

            galeria = false;
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(galeria) {

            Uri uri = data.getData();

            try {

                InputStream stream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                FotoView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {

                Toast.makeText(this, "Foto não encontrado", Toast.LENGTH_SHORT).show();
            }
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
