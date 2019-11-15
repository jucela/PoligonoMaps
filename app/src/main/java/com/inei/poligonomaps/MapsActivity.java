package com.inei.poligonomaps;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.inei.poligonomaps.modelo.DAO.Data;
import com.inei.poligonomaps.modelo.DAO.SQLConstantes;
import com.inei.poligonomaps.modelo.IOHelper;
import com.inei.poligonomaps.modelo.Pojos.Poligono;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import static android.os.Environment.getExternalStorageDirectory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener
{

    private GoogleMap mMap;
    private static final int LOCATION_REQUEST_CODE = 1;
    private Polyline line;
    private Polygon poligon;
    private Marker marker;
    private ArrayList<LatLng> listPoints = new ArrayList<LatLng>() ;
    Data data;
    Context context;
    public static final String TAG = "logcat";
    private FloatingActionButton fab3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        FloatingActionButton fab = findViewById(R.id.fab1);
        FloatingActionButton fab2 = findViewById(R.id.fab2);
        fab3 = findViewById(R.id.fab3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 //String format = String.format(Locale.getDefault(),"Lat/Lng = (%f,%f)", latLng.latitude, latLng.longitude);
                try {
                    data = new Data(getApplicationContext());
                    for (int i=0;i<listPoints.size();i++)
                    {   data = new Data(context);
                        data.open();
                        double latitud  = listPoints.get(i).latitude;
                        double longitud  = listPoints.get(i).longitude;
                        Poligono poligono = new Poligono(1,latitud,longitud);
                        data.insertarDatosService(poligono);
                        //line.remove();
                        //listPoints.clear();
                        //mMap.clear();
                    }
                    Toast.makeText(MapsActivity.this,"Se Guardo Polygono",Toast.LENGTH_SHORT).show();
                    listPoints.clear();
                    data.close();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MapsActivity.this,"Error al guardar polyline",Toast.LENGTH_SHORT).show();
                }

                //Toast.makeText(MapsActivity.this,""+listPoints.get(1).latitude+"/"+listPoints.get(1).longitude,Toast.LENGTH_LONG).show();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    generateXML("1");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(MapsActivity.this,""+listPoints.get(1).latitude+"/"+listPoints.get(1).longitude,Toast.LENGTH_LONG).show();
            }
        });



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);





    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Coordenadas INEI
        LatLng inei= new LatLng(-12.0666134,-77.0452641);
        mMap.addMarker(new MarkerOptions().position(inei).title("Marcador"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(inei,5));
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        //mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setOnMapClickListener(this);

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.setMyLocationEnabled(true);
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permsRequestCode = 100;
            String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                mMap.setMyLocationEnabled(true);
                Log.e(TAG, "ya tiene privilegios");
            }
            else
            {requestPermissions(perms, permsRequestCode);
                Log.e(TAG, "muestra mensaje de privilegios");
            }
        }
        else
        {
            Log.e(TAG, "Es una version menor a API 23");
        }



    }

    //Permisos para boton de ubicacion
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            // ¿Permisos asignados?
            if (permissions.length > 0 && permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {
                Toast.makeText(this, "Error de permisos", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        //line = mMap.addPolyline(new PolylineOptions());
        listPoints.add(latLng);
        poligon = mMap.addPolygon(new PolygonOptions().addAll(listPoints));



        if(listPoints.size() > 1){

//            line.setPoints(listPoints);
//            line.setColor(0xffff0000);
//            line.setWidth(5);
            poligon.setPoints(listPoints);
            poligon.setFillColor(0xffff0000);
            poligon.setStrokeWidth(5);
            //poligon.setHoles(Collections.singletonList(listPoints));
        }

           marker = mMap.addMarker(new MarkerOptions()
           .position(latLng)
           .title(""+latLng)
           //.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_action_pin))


        );
    }

    public void generateXML(String id) throws IOException {
        String nombreArchivo = "aaaa.xml";
        ArrayList<Poligono> poligonos;

        Data data = new Data(this);
        data.open();
        poligonos = data.getPoligonos(id);
        data.close();

        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setOutput(writer);
            serializer.startDocument("utf-8", true);
            serializer.startTag("", "DATOS");
            serializer.attribute("", "id",id);
            if(poligonos.size()>0){
                serializer.startTag("", "POLIGONO");
                for(Poligono poligono:poligonos) {
                    serializer.startTag("", "COORDENADA");
                    writeRowXml(serializer, "ID", String.valueOf(poligono.getId()));
                    writeRowXml(serializer, SQLConstantes.datospoligono_latitud, String.valueOf(poligono.getLatitud()));
                    writeRowXml(serializer, SQLConstantes.datospoligono_longitud, String.valueOf(poligono.getLongitud()));
                    serializer.endTag("", "COORDENADA");
                }
                serializer.endTag("", "POLIGONO");
            }
            serializer.endTag("", "DATOS");
            serializer.endDocument();
            String result = writer.toString();
            File nuevaCarpeta = new File(getExternalStorageDirectory(), "POLIGONO");
            nuevaCarpeta.mkdirs();
            File file = new File(nuevaCarpeta, nombreArchivo);
            IOHelper.writeToFile(file,result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void writeRowXml(XmlSerializer s, String campo,String valor){
        try {
            s.startTag("", campo);
            if(valor != null) s.text(valor);
            else s.text("");
            s.endTag("", campo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void verifyPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permsRequestCode = 100;
            String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            int accessFinePermission    = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            int writeExternalPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (accessFinePermission == PackageManager.PERMISSION_GRANTED && writeExternalPermission == PackageManager.PERMISSION_GRANTED){
//                mMap.setMyLocationEnabled(true);
                Log.e(TAG, "ya tiene privilegios");
            }
            else
            {requestPermissions(perms, permsRequestCode);
                Log.e(TAG, "muestra mensaje de privilegios");
            }
        }
        else
        {
            Log.e(TAG, "Es una version menor a API 23");
        }


    }




}
