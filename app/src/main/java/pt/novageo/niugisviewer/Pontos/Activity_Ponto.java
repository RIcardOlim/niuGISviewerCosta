package pt.novageo.niugisviewer.Pontos;

import android.app.Activity;
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
import android.util.Log;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import pt.novageo.niugisviewer.DB_ponto.AppDatabase;
import pt.novageo.niugisviewer.Manifest;
import pt.novageo.niugisviewer.R;
import pt.novageo.niugisviewer.Tabela.Cafe;
import pt.novageo.niugisviewer.Tabela.Escola;
import pt.novageo.niugisviewer.Tabela.Supermercado;


/**
 * Created by estagiario on 31/03/2017.
 *
 */

public class Activity_Ponto extends AppCompatActivity {

    EditText inserirnome, inserirdesc;
    ImageView FotoView;
    String coordLat, coordLng, morada, nome, desc, tipo;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    double lat, lng;
    Boolean galeria, camera;
    private Escola escola;
    private Supermercado superm;
    private Cafe cafe;
    final int REQUEST_CODE_GALLERY = 1;

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
        escola = new Escola();
        superm = new Supermercado();
        cafe = new Cafe();
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


        try {
            if (Objects.equals(nome, "")) {
                Toast.makeText(this, "Nome do ponto obrigatório", Toast.LENGTH_SHORT).show();

            } else if (Objects.equals(tipo, "Escola")) {

                escola.setNomePonto(inserirnome.getText().toString());
                escola.setDescPonto(inserirdesc.getText().toString());
                escola.setLatPonto(lat);
                escola.setLngPonto(lng);
                escola.setMoradaPonto(morada);
                escola.setTipoPonto(spinner.getSelectedItem().toString());
                escola.setDataPonto(getDateTime());
                escola.setImagemPonto(imageViewToByte(FotoView));
                AppDatabase.getAppDatabase(this).escolaDao().insert(escola);
                Toast.makeText(this, "Ponto Adicionado", Toast.LENGTH_SHORT).show();
                resetText();
            } else if (Objects.equals(tipo, "Café")) {

                cafe.setNomePonto(inserirnome.getText().toString());
                cafe.setDescPonto(inserirdesc.getText().toString());
                cafe.setLatPonto(lat);
                cafe.setLngPonto(lng);
                cafe.setMoradaPonto(morada);
                cafe.setTipoPonto(spinner.getSelectedItem().toString());
                cafe.setDataPonto(getDateTime());
                cafe.setImagemPonto(imageViewToByte(FotoView));
                AppDatabase.getAppDatabase(this).cafeDao().insert(cafe);
                Toast.makeText(this, "Ponto Adicionado", Toast.LENGTH_SHORT).show();
                resetText();
            } else if (Objects.equals(tipo, "Supermercado")) {

                superm.setNomePonto(inserirnome.getText().toString());
                superm.setDescPonto(inserirdesc.getText().toString());
                superm.setLatPonto(lat);
                superm.setLngPonto(lng);
                superm.setMoradaPonto(morada);
                superm.setTipoPonto(spinner.getSelectedItem().toString());
                superm.setDataPonto(getDateTime());
                superm.setImagemPonto(imageViewToByte(FotoView));
                AppDatabase.getAppDatabase(this).supermercadoDao().insert(superm);
                Toast.makeText(this, "Ponto Adicionado", Toast.LENGTH_SHORT).show();
                resetText();
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void ViewButtonClcked(View view){

        Intent view2 = new Intent(this, Activity_ListData.class);
     //   db.close();
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
                    ActivityCompat.requestPermissions(Activity_Ponto.this, new String[] {Manifest.permission.CAMERA}, REQUEST_CODE_GALLERY);
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

    private String getDateTime(){

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(galeria) {

            if (requestCode == REQUEST_CODE_GALLERY) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("niuGISViewer", "Tem permissão");
                    Intent img = new Intent(Intent.ACTION_PICK);
                    img.setType("image/*");
                    startActivityForResult(img, REQUEST_CODE_GALLERY);

                }
            }

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        if(camera) {

            if (requestCode == REQUEST_CODE_GALLERY) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.e("niuGISViewer", "aceitou");

                    Intent it = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(it, REQUEST_CODE_GALLERY);
               } else {

                    Log.e("niuGISViewer", "recusou");
                }

            }

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(galeria) {

            if(data != null) {

                Uri uri = data.getData();

                if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(this, "wow", Toast.LENGTH_SHORT).show();
                }

                try {

                    InputStream stream = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    FotoView.setImageBitmap(bitmap);

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

                FotoView = (ImageView) findViewById(R.id.imageView8);
                FotoView.setImageBitmap(bmp);
            }

            camera = false;
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
