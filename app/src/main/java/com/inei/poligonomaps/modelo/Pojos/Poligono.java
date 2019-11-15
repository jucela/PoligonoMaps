package com.inei.poligonomaps.modelo.Pojos;

import android.content.ContentValues;

import com.inei.poligonomaps.modelo.DAO.SQLConstantes;

public class Poligono {

    private int id;
    private double latitud;
    private double longitud;

    public Poligono(int id, double latitud, double longitud) {
        this.id = id;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Poligono() {
        this.id=0;
        this.id=0;
        this.id=0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public ContentValues toValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLConstantes.datospoligono_id,id);
        contentValues.put(SQLConstantes.datospoligono_latitud,latitud);
        contentValues.put(SQLConstantes.datospoligono_longitud,longitud);
        return contentValues;
    }
}
