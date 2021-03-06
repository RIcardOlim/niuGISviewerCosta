package pt.novageo.niugisviewer.Main;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.disposables.Disposable;
import pt.novageo.niugisviewer.About.Activity_about;
import pt.novageo.niugisviewer.About.Activity_cma;
import pt.novageo.niugisviewer.About.Activity_escola;
import pt.novageo.niugisviewer.Api.ApiListener;
import pt.novageo.niugisviewer.Api.ApiManager;
import pt.novageo.niugisviewer.BuildConfig;
import pt.novageo.niugisviewer.DB_ponto.AppDatabase;
import pt.novageo.niugisviewer.Pontos.Activity_ListData;
import pt.novageo.niugisviewer.Pontos.Activity_Ponto;
import pt.novageo.niugisviewer.Pontos.Activity_informacao;
import pt.novageo.niugisviewer.About.Activity_ng;
import pt.novageo.niugisviewer.DB_layer.TileProviderFactory;
import pt.novageo.niugisviewer.R;
import pt.novageo.niugisviewer.Tabela.Cafe;
import pt.novageo.niugisviewer.Tabela.Escola;
import pt.novageo.niugisviewer.Tabela.Supermercado;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    SQLiteDatabase mydatabase;
    Geocoder geocoder;
    List<Address> address;
    String morada = "";
    double lat, lng;
    boolean dbEscola = false, dbCafe = false, dbSM = false;
    HttpURLConnection conexao = null;
    BufferedReader reader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final Intent sql = new Intent(this, Activity_Ponto.class);

        mydatabase = openOrCreateDatabase("niugisviewer", MODE_PRIVATE, null);

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS layers(name VARCHAR, title VARCHAR, active BOOLEAN);");
        mydatabase.execSQL("DELETE FROM Layers");
        mydatabase.execSQL("INSERT INTO layers(name, title, active) VALUES('0a4d9a280794d2f152feebbf7423340b', 'freguesias', 1);");
        mydatabase.execSQL("INSERT INTO layers(name, title, active) VALUES('28ef66cda0ee9a2eb81df5a79e898517', 'edificios', 1);");
        mydatabase.execSQL("INSERT INTO layers(name, title, active) VALUES('ab2cc899ed871afd2152e997c3934eac', 'vias', 1);");
        mydatabase.execSQL("INSERT INTO layers(name, title, active) VALUES('6406445d84559cbfe7d072f76438ff1d', 'ferrovias', 1);");
        mydatabase.execSQL("INSERT INTO layers(name, title, active) VALUES('b55d05c572d87837cb2c498f931f9d27', 'bancos', 0);");
        mydatabase.execSQL("INSERT INTO layers(name, title, active) VALUES('6168ea2df006cf2d0e104e7be88dff1b', 'bombas_gasolina', 0);");
        mydatabase.execSQL("INSERT INTO layers(name, title, active) VALUES('2d88045660bbf8fe2ce8a09a4af033ed', 'bombeiros', 0);");
        mydatabase.execSQL("INSERT INTO layers(name, title, active) VALUES('482bff4aebbd878a0660dd36d0505cc4', 'cafes', 0);");
        mydatabase.execSQL("INSERT INTO layers(name, title, active) VALUES('3541759ddf6a06a78ba914b2f56f9b44', 'conservatoria_registo_predial', 0);");
        mydatabase.execSQL("INSERT INTO layers(name, title, active) VALUES('37b9bad64eb1858d5b698095b7bc129c', 'correio', 0);");
        mydatabase.execSQL("INSERT INTO layers(name, title, active) VALUES('adfdd03c65e9dd24a7cddcab99c35162', 'escola', 0);");
        mydatabase.execSQL("INSERT INTO layers(name, title, active) VALUES('929aed75a215d6aeddd5e73c2f8ce206', 'estaçao_comboio', 0);");
        mydatabase.execSQL("INSERT INTO layers(name, title, active) VALUES('7799240f8dbd39c1f6fe306bcfa0f145', 'farmacias', 0);");
        mydatabase.execSQL("INSERT INTO layers(name, title, active) VALUES('610f3e271d9a29235ab5abc45d6e6e38', 'policia', 0);");
        mydatabase.execSQL("INSERT INTO layers(name, title, active) VALUES('73b4b7160e10fe06f4f2aa2d0c832163', 'restaurante', 0);");
        mydatabase.execSQL("INSERT INTO layers(name, title, active) VALUES('aa0d5ea150c6698d7a9dd3eb20fc7703', 'supermercados', 0);");

        setUpMapIfNeeded();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(1).setChecked(false);

        mapFragment.getMapAsync(this);

        geocoder = new Geocoder(this, Locale.getDefault());

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng position) {

                try {
                    address = geocoder.getFromLocation(position.latitude, position.longitude, 1);
                    morada = address.get(0).getAddressLine(0);
                } catch (IOException e) {

                    morada = "Morada não encontrada";
                }

                lat = position.latitude;
                lng = position.longitude;
                String coordLat = String.valueOf(lat);
                String coordLng = String.valueOf(lng);
                sql.putExtra("coordLat", coordLat);
                sql.putExtra("coordLng", coordLng);
                sql.putExtra("morada", morada);
                startActivity(sql);
            }
        });
    }

    protected void onResume() {

        super.onResume();
        ResetLayer();
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.layers_menu, menu);
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent about = new Intent(this, Activity_about.class);
        Intent cma = new Intent(this, Activity_cma.class);
        Intent ng = new Intent(this, Activity_ng.class);
        Intent es = new Intent(this, Activity_escola.class);

        int id = item.getItemId();

        if (id == R.id.layersmenu) {
            return true;
        }

        if (id == R.id.layer1) {
            Toast.makeText(MainActivity.this, R.string.freguesia, Toast.LENGTH_SHORT).show();
            if (item.isChecked()) {
                item.setChecked(false);
                this.mydatabase.execSQL("UPDATE layers SET active=0 WHERE title='freguesias';");
            } else {
                item.setChecked(true);
                this.mydatabase.execSQL("UPDATE layers SET active=1 WHERE title='freguesias';");
            }
        }

        if (id == R.id.layer2) {
            Toast.makeText(MainActivity.this, R.string.edif_cios, Toast.LENGTH_SHORT).show();
            if (item.isChecked()) {

                item.setChecked(false);
                this.mydatabase.execSQL("UPDATE layers SET active=0 WHERE title='edificios';");
            } else {

                item.setChecked(true);
                this.mydatabase.execSQL("UPDATE layers SET active=1 WHERE title='edificios';");
            }
        }

        if (id == R.id.layer3) {
            Toast.makeText(MainActivity.this, R.string.vias, Toast.LENGTH_SHORT).show();
            if (item.isChecked()) {
                item.setChecked(false);
                this.mydatabase.execSQL("UPDATE layers SET active=0 WHERE title='vias';");
            } else {
                item.setChecked(true);
                this.mydatabase.execSQL("UPDATE layers SET active=1 WHERE title='vias';");
            }
        }

        if (id == R.id.layer4) {
            Toast.makeText(MainActivity.this, R.string.ferrovias, Toast.LENGTH_SHORT).show();
            if (item.isChecked()) {
                item.setChecked(false);
                this.mydatabase.execSQL("UPDATE layers SET active=0 WHERE title='ferrovias';");
            } else {
                item.setChecked(true);
                this.mydatabase.execSQL("UPDATE layers SET active=1 WHERE title='ferrovias';");
            }
        }

        if (id == R.id.layer5) {
            Toast.makeText(MainActivity.this, R.string.bancos, Toast.LENGTH_SHORT).show();
            if (item.isChecked()) {
                item.setChecked(false);
                this.mydatabase.execSQL("UPDATE layers SET active=0 WHERE title='bancos';");
            } else {
                item.setChecked(true);
                this.mydatabase.execSQL("UPDATE layers SET active=1 WHERE title='bancos';");
            }
        }

        if (id == R.id.layer6) {
            Toast.makeText(MainActivity.this, R.string.bombas_de_gasolina, Toast.LENGTH_SHORT).show();
            if (item.isChecked()) {
                item.setChecked(false);
                this.mydatabase.execSQL("UPDATE layers SET active=0 WHERE title='bombas_gasolina';");
            } else {
                item.setChecked(true);
                this.mydatabase.execSQL("UPDATE layers SET active=1 WHERE title='bombas_gasolina';");
            }
        }

        if (id == R.id.layer7) {
            Toast.makeText(MainActivity.this, R.string.bombeiros, Toast.LENGTH_SHORT).show();
            if (item.isChecked()) {
                item.setChecked(false);
                this.mydatabase.execSQL("UPDATE layers SET active=0 WHERE title='bombeiros';");
            } else {
                item.setChecked(true);
                this.mydatabase.execSQL("UPDATE layers SET active=1 WHERE title='bombeiros';");
            }
        }

        if (id == R.id.layer8) {
            Toast.makeText(MainActivity.this, R.string.caf_s, Toast.LENGTH_SHORT).show();
            if (item.isChecked()) {
                item.setChecked(false);
                this.mydatabase.execSQL("UPDATE layers SET active=0 WHERE title='cafes';");
                dbCafe = false;
            } else {
                item.setChecked(true);
                this.mydatabase.execSQL("UPDATE layers SET active=1 WHERE title='cafes';");
                dbCafe = true;
            }
        }

        if (id == R.id.layer9) {
            Toast.makeText(MainActivity.this, R.string.esta_o_de_comboios, Toast.LENGTH_SHORT).show();
            if (item.isChecked()) {
                item.setChecked(false);
                this.mydatabase.execSQL("UPDATE layers SET active=0 WHERE title='estaçao_comboio';");
            } else {
                item.setChecked(true);
                this.mydatabase.execSQL("UPDATE layers SET active=1 WHERE title='estaçao_comboio';");
            }
        }

        if (id == R.id.layer10) {
            Toast.makeText(MainActivity.this, R.string.conservatoria, Toast.LENGTH_SHORT).show();
            if (item.isChecked()) {
                item.setChecked(false);
                this.mydatabase.execSQL("UPDATE layers SET active=0 WHERE title='conservatoria_registo_predial';");
            } else {
                item.setChecked(true);
                this.mydatabase.execSQL("UPDATE layers SET active=1 WHERE title='conservatoria_registo_predial';");
            }
        }

        if (id == R.id.layer11) {
            Toast.makeText(MainActivity.this, R.string.correios, Toast.LENGTH_SHORT).show();
            if (item.isChecked()) {
                item.setChecked(false);
                this.mydatabase.execSQL("UPDATE layers SET active=0 WHERE title='correio';");
            } else {
                item.setChecked(true);
                this.mydatabase.execSQL("UPDATE layers SET active=1 WHERE title='correio';");
            }
        }

        if (id == R.id.layer12) {
            Toast.makeText(MainActivity.this, R.string.escolas, Toast.LENGTH_SHORT).show();
            if (item.isChecked()) {
                item.setChecked(false);
                this.mydatabase.execSQL("UPDATE layers SET active=0 WHERE title='escola';");
                dbEscola = false;
            } else {
                item.setChecked(true);
                this.mydatabase.execSQL("UPDATE layers SET active=1 WHERE title='escola';");
                dbEscola = true;
            }
        }

        if (id == R.id.layer13) {
            Toast.makeText(MainActivity.this, R.string.farmacias, Toast.LENGTH_SHORT).show();
            if (item.isChecked()) {
                item.setChecked(false);
                this.mydatabase.execSQL("UPDATE layers SET active=0 WHERE title='farmacias';");
            } else {
                item.setChecked(true);
                this.mydatabase.execSQL("UPDATE layers SET active=1 WHERE title='farmacias';");
            }
        }

        if (id == R.id.layer14) {
            Toast.makeText(MainActivity.this, R.string.pol_cia, Toast.LENGTH_SHORT).show();
            if (item.isChecked()) {
                item.setChecked(false);
                this.mydatabase.execSQL("UPDATE layers SET active=0 WHERE title='policia';");
            } else {
                item.setChecked(true);
                this.mydatabase.execSQL("UPDATE layers SET active=1 WHERE title='policia';");
            }
        }

        if (id == R.id.layer15) {
            Toast.makeText(MainActivity.this, R.string.restaurantes, Toast.LENGTH_SHORT).show();
            if (item.isChecked()) {
                item.setChecked(false);
                this.mydatabase.execSQL("UPDATE layers SET active=0 WHERE title='restaurante';");
            } else {
                item.setChecked(true);
                this.mydatabase.execSQL("UPDATE layers SET active=1 WHERE title='restaurante';");
            }
        }

        if (id == R.id.layer16) {
            Toast.makeText(MainActivity.this, R.string.supermercados, Toast.LENGTH_SHORT).show();
            if (item.isChecked()) {
                item.setChecked(false);
                dbSM = false;
                this.mydatabase.execSQL("UPDATE layers SET active=0 WHERE title='supermercados';");
            } else {
                item.setChecked(true);
                this.mydatabase.execSQL("UPDATE layers SET active=1 WHERE title='supermercados';");
                dbSM = true;
            }
        }

        ResetLayer();

        if (id == R.id.action_about) {
            startActivity(about);
            return true;
        } else if (id == R.id.action_contacto) {
            startActivity(cma);
            return true;
        } else if (id == R.id.action_contacto2) {
            startActivity(ng);
            return true;
        } else if (id == R.id.action_contacto3) {
            startActivity(es);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        Intent view = new Intent(this, Activity_ListData.class);

        if (id == R.id.nav_camera) {

            if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            } else mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }

        if (id == R.id.action_view) {

            startActivity(view);
            return true;
        }

        if (id == R.id.action_niuGISdb) {

            enviarDados();
            /*new JSONTask().execute("http://agualva.niugis.com/websig/webservices/teste.php");
            return true;*/
        }

        ResetLayer();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    public void ResetLayer() {

        mMap.clear();
        setUpMap();
        checkDB();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng Agualva = new LatLng(38.77410061, -9.2924664);

        float zoomLevel = 13;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Agualva, zoomLevel));
        mMap.setOnInfoWindowClickListener(this);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        checkDB();
        //   mMap.setMyLocationEnabled(true);
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            } else {
                Log.e("WMSDEMO", "Map was null!");
            }
        }
    }

    private void setUpMap() {

        StringBuilder sb = new StringBuilder();
        Cursor resultSet = this.mydatabase.rawQuery("Select name from layers where active", null);
        Log.e("LAYERS: ", String.valueOf(resultSet.getCount()));
        resultSet.moveToFirst();
        if (resultSet.moveToFirst()) {
            do {
                if (sb.length() < 1)
                    sb.append(resultSet.getString(0));
                else
                    sb.append("," + resultSet.getString(0));
            } while (resultSet.moveToNext());
        }
        if (!resultSet.isClosed()) {
            resultSet.close();
        }
        Log.e("LAYERS: ", sb.toString());

        TileProvider wmsTileProvider = TileProviderFactory.getOsgeoWmsTileProvider(sb.toString());
        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(wmsTileProvider));
    }

    private void AddMarkerDB() {

        if(dbEscola) {

            List<Escola> escola = AppDatabase.getAppDatabase(this).escolaDao().getAll();
            for(Escola esc : escola) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(esc.getLatPonto(), esc.getLngPonto()))
                        .snippet(esc.getTipoPonto())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                        .title(esc.getNomePonto()));
            }
        }

        if(dbCafe) {

            List<Cafe> cafe = AppDatabase.getAppDatabase(this).cafeDao().getAll();
            for (Cafe caf : cafe) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(caf.getLatPonto(), caf.getLngPonto()))
                        .snippet(caf.getTipoPonto())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                        .title(caf.getNomePonto()));
            }
        }
        if(dbSM) {

        List<Supermercado> superm = AppDatabase.getAppDatabase(this).supermercadoDao().getAll();
        for(Supermercado SM : superm) {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(SM.getLatPonto(), SM.getLngPonto()))
                    .snippet(SM.getTipoPonto())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                    .title(SM.getNomePonto()));
            }
        }
    }

    private void checkDB() {

        if(dbEscola) {

            List<Escola> escola = AppDatabase.getAppDatabase(this).escolaDao().getAll();
            if(!escola.isEmpty()) {
                AddMarkerDB();
            } else Toast.makeText(this, "Nenhum ponto encontrado", Toast.LENGTH_SHORT).show();
        }

        if(dbCafe) {

            List<Cafe> cafe = AppDatabase.getAppDatabase(this).cafeDao().getAll();
            if(!cafe.isEmpty()) {
                AddMarkerDB();
            } else Toast.makeText(this, "Nenhum ponto encontrado", Toast.LENGTH_SHORT).show();
        }

        if(dbSM) {

            List<Supermercado> SM = AppDatabase.getAppDatabase(this).supermercadoDao().getAll();
            if(!SM.isEmpty()) {
                AddMarkerDB();
            } else Toast.makeText(this, "Nenhum ponto encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        int id = -1;
        String tipo = "woah";

        if(dbEscola) {

            String nome = marker.getTitle();
            Escola escola = AppDatabase.getAppDatabase(this).escolaDao().findDatabyNome(nome);
            id = escola.get_id();
            tipo = escola.getTipoPonto();
            dbEscola = false;
        }


        if(dbCafe) {

            String nome = marker.getTitle();
            Cafe cafe = AppDatabase.getAppDatabase(this).cafeDao().findDatabyNome(nome);
            id = cafe.get_id();
            tipo = cafe.getTipoPonto();
            dbCafe = false;
        }

        if(dbSM) {

            String nome = marker.getTitle();
            Supermercado superm = AppDatabase.getAppDatabase(this).supermercadoDao().findDatabyNome(nome);
            id = superm.get_id();
            tipo = superm.getTipoPonto();
            dbSM = false;
        }

        Intent inf = new Intent(this, Activity_informacao.class);
        inf.putExtra("ID", id);
        inf.putExtra("TIPO", tipo);
        startActivity(inf);
    }

    private void enviarDados(){

        List<Escola> listescola = AppDatabase.getAppDatabase(this).escolaDao().getAll();
        List<Cafe> listcafe = AppDatabase.getAppDatabase(this).cafeDao().getAll();
        List<Supermercado> listsm = AppDatabase.getAppDatabase(this).supermercadoDao().getAll();
        String json2file = "";
        String json = "";
        //device
       /* String instanceId = InstanceID.getInstance(this).getId();
        String serialNumber = Build.SERIAL;
        String device_code = serialNumber + "_" + instanceId;*/

        for(Escola escola : listescola){

            json = new GsonBuilder().create().toJson(escola, Escola.class);
            json2file += json;
        }

        for (Cafe cafe : listcafe){

            json = new GsonBuilder().create().toJson(cafe);
        }

        for (Supermercado sm : listsm){

            json = new GsonBuilder().create().toJson(sm);
        }

        try {

            Log.d(BuildConfig.NAME, "Pedido enviado");
            ApiManager.getInstance().postAddPonto("setsync",
                    "Ricardo",
                    "123456",
                    json2file,
                    new AddPontoRequestListener());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class AddPontoRequestListener implements ApiListener<Void> {


        @Override
        public void subscribe(Disposable disposable) {
            Log.d(BuildConfig.NAME, "Subscrito");
        }

        @Override
        public void onSuccess(String url) {

            Log.d(BuildConfig.NAME, "Sucesso!");
            Toast.makeText(MainActivity.this, "Enviado com Sucesso!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(String error) {
            Log.d(BuildConfig.NAME, "Falhou: " + error);
        }
    }

/*

    public class JSONTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {

                //conectar ao servidor

                String linha;
                URL url = new URL(params[0]);
                conexao = (HttpURLConnection) url.openConnection();
                conexao.setDoOutput(true);
                conexao.setRequestProperty("Content-Type", "application/json");
                conexao.setRequestProperty("Accept", "application/json");
                conexao.setRequestMethod("POST");
                conexao.connect();

                //enviar pedido

                JSONObject insObj = new JSONObject();
                insObj.put("operacao", "insert");
                insObj.put("entidade", "6");
                insObj.put("nome", "Café A");
                insObj.put("descricao", "Descrção do café");
                insObj.put("morada", "Rua tal");
                insObj.put("coordx", "8.1212");
                insObj.put("coordy", "9.321");

                JSONObject uptObj = new JSONObject();
                uptObj.put("operacao", "update");
                uptObj.put("entidade", "10");
                uptObj.put("nome", "Escola lá da rua");
                uptObj.put("descricao", "Descrição da escola");
                uptObj.put("morada", "Rua da escola");
                uptObj.put("coordx", "8.7282");
                uptObj.put("coordy", "9.59245");

                JSONArray pontArray = new JSONArray();

                pontArray.put(insObj);
                pontArray.put(uptObj);

                JSONObject jsonObj = new JSONObject();
                jsonObj.put("service", "setsync");
                jsonObj.put("user", "Ricardo");
                jsonObj.put("password", "123456");
                jsonObj.put("pontos", pontArray);

                String json = jsonObj.toString();

                OutputStreamWriter os = new OutputStreamWriter(conexao.getOutputStream());
                os.write(json);
                os.flush();
                os.close();

                //receber resposta

                InputStream stream = conexao.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                while ((linha = reader.readLine()) != null){

                    buffer.append(linha);
                }

                String finalJson = buffer.toString();
                Log.e("niuGISviewer", finalJson);

                JSONObject parentObject = new JSONObject(finalJson);
                JSONObject respostaObject = parentObject.getJSONObject("response");

                String resposta = respostaObject.getString("status");

                return resposta;

            } catch (MalformedURLException e) {

                e.printStackTrace();
                Toast.makeText(MainActivity.this, "[001]Conexão Falhada", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {

                e.printStackTrace();
                Toast.makeText(MainActivity.this, "[002]Conexão Falhada", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {

                e.printStackTrace();
                Toast.makeText(MainActivity.this, "[003]JSON Falhado", Toast.LENGTH_SHORT).show();
            } finally {

                if(conexao != null) {

                    conexao.disconnect();
                }

                try {

                    if(reader != null) {
                        reader.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "[001]Erro a fechar o reader", Toast.LENGTH_SHORT).show();
                }
            }
            return null;
        }

        protected void onPostExecute(String result){

            super.onPostExecute(result);
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }
*/

}
