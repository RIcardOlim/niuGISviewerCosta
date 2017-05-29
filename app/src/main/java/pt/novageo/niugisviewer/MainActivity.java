package pt.novageo.niugisviewer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    SQLiteDatabase mydatabase;
    DBTeste db;
    Geocoder geocoder;
    List<Address> address;
    String morada = "";
    double lat, lng;
    boolean dbEscola = false, dbCafe = false, dbSM = false, GPS = false;
    private static final int PERMS_REQUEST_CODE = 123;

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
/*
        if(!hasPermissions()){
            requestPerms();
        }
        */
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
        navigationView.getMenu().getItem(1).setChecked(true);

        mapFragment.getMapAsync(this);

        db = new DBTeste(this, null, null, 20);

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
            Toast.makeText(MainActivity.this, "Freguesia", Toast.LENGTH_SHORT).show();
            if (item.isChecked()) {
                item.setChecked(false);
                this.mydatabase.execSQL("UPDATE layers SET active=0 WHERE title='freguesias';");
            } else {
                item.setChecked(true);
                this.mydatabase.execSQL("UPDATE layers SET active=1 WHERE title='freguesias';");
            }
        }

        if (id == R.id.layer2) {
            if (item.isChecked()) {
                item.setChecked(false);
                Toast.makeText(MainActivity.this, "Edificios", Toast.LENGTH_SHORT).show();
                this.mydatabase.execSQL("UPDATE layers SET active=0 WHERE title='edificios';");
            } else {
                item.setChecked(true);
                Toast.makeText(MainActivity.this, "Edificios", Toast.LENGTH_SHORT).show();
                this.mydatabase.execSQL("UPDATE layers SET active=1 WHERE title='edificios';");
            }
        }

        if (id == R.id.layer3) {
            Toast.makeText(MainActivity.this, "Vias", Toast.LENGTH_SHORT).show();
            if (item.isChecked()) {
                item.setChecked(false);
                this.mydatabase.execSQL("UPDATE layers SET active=0 WHERE title='vias';");
            } else {
                item.setChecked(true);
                this.mydatabase.execSQL("UPDATE layers SET active=1 WHERE title='vias';");
            }
        }

        if (id == R.id.layer4) {
            Toast.makeText(MainActivity.this, "Ferrovias", Toast.LENGTH_SHORT).show();
            if (item.isChecked()) {
                item.setChecked(false);
                this.mydatabase.execSQL("UPDATE layers SET active=0 WHERE title='ferrovias';");
            } else {
                item.setChecked(true);
                this.mydatabase.execSQL("UPDATE layers SET active=1 WHERE title='ferrovias';");
            }
        }

        if (id == R.id.layer5) {
            Toast.makeText(MainActivity.this, "Bancos", Toast.LENGTH_SHORT).show();
            if (item.isChecked()) {
                item.setChecked(false);
                this.mydatabase.execSQL("UPDATE layers SET active=0 WHERE title='bancos';");
            } else {
                item.setChecked(true);
                this.mydatabase.execSQL("UPDATE layers SET active=1 WHERE title='bancos';");
            }
        }

        if (id == R.id.layer6) {
            Toast.makeText(MainActivity.this, "Bombas de Gasolina", Toast.LENGTH_SHORT).show();
            if (item.isChecked()) {
                item.setChecked(false);
                this.mydatabase.execSQL("UPDATE layers SET active=0 WHERE title='bombas_gasolina';");
            } else {
                item.setChecked(true);
                this.mydatabase.execSQL("UPDATE layers SET active=1 WHERE title='bombas_gasolina';");
            }
        }

        if (id == R.id.layer7) {
            Toast.makeText(MainActivity.this, "Bombeiros", Toast.LENGTH_SHORT).show();
            if (item.isChecked()) {
                item.setChecked(false);
                this.mydatabase.execSQL("UPDATE layers SET active=0 WHERE title='bombeiros';");
            } else {
                item.setChecked(true);
                this.mydatabase.execSQL("UPDATE layers SET active=1 WHERE title='bombeiros';");
            }
        }

        if (id == R.id.layer8) {
            Toast.makeText(MainActivity.this, "Cafés", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(MainActivity.this, "Estação de Comboios", Toast.LENGTH_SHORT).show();
            if (item.isChecked()) {
                item.setChecked(false);
                this.mydatabase.execSQL("UPDATE layers SET active=0 WHERE title='estaçao_comboio';");
            } else {
                item.setChecked(true);
                this.mydatabase.execSQL("UPDATE layers SET active=1 WHERE title='estaçao_comboio';");
            }
        }

        if (id == R.id.layer10) {
            Toast.makeText(MainActivity.this, "Conservatório do Registo Predial", Toast.LENGTH_SHORT).show();
            if (item.isChecked()) {
                item.setChecked(false);
                this.mydatabase.execSQL("UPDATE layers SET active=0 WHERE title='conservatoria_registo_predial';");
            } else {
                item.setChecked(true);
                this.mydatabase.execSQL("UPDATE layers SET active=1 WHERE title='conservatoria_registo_predial';");
            }
        }

        if (id == R.id.layer11) {
            Toast.makeText(MainActivity.this, "Correios", Toast.LENGTH_SHORT).show();
            if (item.isChecked()) {
                item.setChecked(false);
                this.mydatabase.execSQL("UPDATE layers SET active=0 WHERE title='correio';");
            } else {
                item.setChecked(true);
                this.mydatabase.execSQL("UPDATE layers SET active=1 WHERE title='correio';");
            }
        }

        if (id == R.id.layer12) {
            Toast.makeText(MainActivity.this, "Escolas", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(MainActivity.this, "Farmácias", Toast.LENGTH_SHORT).show();
            if (item.isChecked()) {
                item.setChecked(false);
                this.mydatabase.execSQL("UPDATE layers SET active=0 WHERE title='farmacias';");
            } else {
                item.setChecked(true);
                this.mydatabase.execSQL("UPDATE layers SET active=1 WHERE title='farmacias';");
            }
        }

        if (id == R.id.layer14) {
            Toast.makeText(MainActivity.this, "Polícia", Toast.LENGTH_SHORT).show();
            if (item.isChecked()) {
                item.setChecked(false);
                this.mydatabase.execSQL("UPDATE layers SET active=0 WHERE title='policia';");
            } else {
                item.setChecked(true);
                this.mydatabase.execSQL("UPDATE layers SET active=1 WHERE title='policia';");
            }
        }

        if (id == R.id.layer15) {
            Toast.makeText(MainActivity.this, "Restaurantes", Toast.LENGTH_SHORT).show();
            if (item.isChecked()) {
                item.setChecked(false);
                this.mydatabase.execSQL("UPDATE layers SET active=0 WHERE title='restaurante';");
            } else {
                item.setChecked(true);
                this.mydatabase.execSQL("UPDATE layers SET active=1 WHERE title='restaurante';");
            }
        }

        if (id == R.id.layer16) {
            Toast.makeText(MainActivity.this, "Supermercados", Toast.LENGTH_SHORT).show();
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
        // Because the demo WMS layer we are using is just a white background map, switch the base layer
        // to satellite so we can see the WMS overlay.
        // mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    private void AddMarkerDB() {

        if(dbEscola) {

            Cursor c = db.getDataEscola();
            c.moveToFirst();
            do {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(c.getDouble(4), c.getDouble(5)))
                        .snippet(c.getString(3))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                        .title(c.getString(1)));
                c.moveToNext();
            } while (!c.isAfterLast());
        }

        if(dbCafe) {

            Cursor c = db.getDataCafe();
            c.moveToFirst();
            do {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(c.getDouble(4), c.getDouble(5)))
                        .snippet(c.getString(3))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                        .title(c.getString(1)));
                c.moveToNext();
            } while (!c.isAfterLast());
        }

        if(dbSM) {

            Cursor c = db.getDataSM();
            c.moveToFirst();
            do {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(c.getDouble(4), c.getDouble(5)))
                        .snippet(c.getString(3))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                        .title(c.getString(1)));
                c.moveToNext();
            } while (!c.isAfterLast());
        }

    }

    private void checkDB() {

        if(dbEscola) {

            Cursor check = db.getDataEscola();
            if(check.moveToFirst()) {
                AddMarkerDB();
            } else Toast.makeText(this, "Nenhum ponto encontrado", Toast.LENGTH_SHORT).show();
        }

        if(dbCafe) {

            Cursor check = db.getDataCafe();
            if(check.moveToFirst()) {
                AddMarkerDB();
            } else Toast.makeText(this, "Nenhum ponto encontrado", Toast.LENGTH_SHORT).show();
        }

        if(dbSM) {

            Cursor check = db.getDataSM();
            if(check.moveToFirst()) {
                AddMarkerDB();
            } else Toast.makeText(this, "Nenhum ponto encontrado", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        String nome = marker.getTitle();
        Cursor c = db.getIdbyNome(nome);
        c.moveToFirst();
        int id = c.getInt(0);
        c = db.getTipobyNomeId(id, nome);
        c.moveToFirst();
        String tipo = c.getString(0);
        Intent inf = new Intent(this, Activity_informacao.class);
        inf.putExtra("ID", id);
        inf.putExtra("TIPO", tipo);
        startActivity(inf);

    }

    private boolean hasPermissions() {

        int res = 0;

        String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};


        for (String perms : permissions) {
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)) {
                return false;
            }
        }
        return true;
    }

    private void requestPerms() {

        String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed = true;

        switch (requestCode) {
            case PERMS_REQUEST_CODE:

                for (int res : grantResults) {
                    // Se o utilizador autorizar
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }
                break;
            default:
                //se o utilizador nao autorizar
                allowed = false;
                break;
        }

        if (allowed) {
            Toast.makeText(this, "Autorizado", Toast.LENGTH_SHORT).show();
        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.MAPS_RECEIVE)) {

                    Toast.makeText(this, "Não autorizado", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}