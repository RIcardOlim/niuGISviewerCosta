package pt.novageo.niugisviewer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import android.util.Log;

public class TileProviderFactory {



    public static WMSTileProvider getOsgeoWmsTileProvider(String layerStr) {


/*
        List<String> names = dh.selectAll();

        StringBuilder sb = new StringBuilder();
        for (String name : names) {
            if (sb.length()<1)
                sb.append(name);
            else
                sb.append("," + name);
        }

        StringBuilder sb = new StringBuilder();
        Cursor resultSet = mydatabase.rawQuery("Select * from layers where active=true",null);
        resultSet.moveToFirst();
        if (resultSet.moveToFirst()) {
            do {
                if (sb.length()<1)
                    sb.append(resultSet.getString(0));
                else
                    sb.append("," + resultSet.getString(0));
            } while (resultSet.moveToNext());
        }
        if (!resultSet.isClosed()) {
            resultSet.close();
        }
*/
        Log.e("TRING LAYERS", layerStr);

        final String WMS_FORMAT_STRING =
                "http://amadora.niugis.com/cgi-bin/mapserv" +
                        "?service=WMS" +
                        "&MAP=/var/www/htdocs/websig/v5/mapfile/agualva/agualva.map" +
                        "&version=1.1.1" +
                        "&request=GetMap" +
                        "&layers=" +
                            layerStr +
                        "&bbox=%f,%f,%f,%f" +
                        "&width=256" +
                        "&height=256" +
                        "&srs=EPSG:900913" +
                        "&format=image/png" +
                        "&transparent=true";


        WMSTileProvider tileProvider = new WMSTileProvider(256,256) {

            @Override
            public synchronized URL getTileUrl(int x, int y, int zoom) {
                double[] bbox = getBoundingBox(x, y, zoom);
                String s = String.format(Locale.US, WMS_FORMAT_STRING, bbox[MINX],
                        bbox[MINY], bbox[MAXX], bbox[MAXY]);
                Log.d("WMSDEMO", s);

                URL url = null;
                try {
                    url = new URL(s);
                } catch (MalformedURLException e) {
                    throw new AssertionError(e);
                }
                return url;
            }
        };
        return tileProvider;
    }
}