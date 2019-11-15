package com.inei.poligonomaps.modelo.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.Polygon;
import com.inei.poligonomaps.modelo.Pojos.Poligono;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Data {
    Context contexto;
    SQLiteOpenHelper sqLiteOpenHelper;
    SQLiteDatabase sqLiteDatabase;

    public  Data (Context context) throws IOException {
        this.contexto = context;
        sqLiteOpenHelper = new DataBaseHelper(contexto);
        createDataBase();
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if(!dbExist){
            sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
            sqLiteDatabase.close();
            try{
                copyDataBase();
                sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
                sqLiteDatabase.execSQL(SQLConstantes.SQL_CREATE_TABLE_POLIGONO2);
                sqLiteDatabase.close();
            }catch (IOException e){
                throw new Error("Error: copiando base de datos");
            }
        }
    }

    public void copyDataBase() throws IOException{
        InputStream myInput = contexto.getAssets().open(SQLConstantes.DB_NAME);
        String outFileName = SQLConstantes.DB_PATH + SQLConstantes.DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) != -1){
            if (length > 0){
                myOutput.write(buffer,0,length);
            }
        }
        myOutput.flush();
        myInput.close();
        myOutput.close();

    }

    public void open() throws SQLException {
        String myPath = SQLConstantes.DB_PATH + SQLConstantes.DB_NAME;
        sqLiteDatabase = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void close(){
        if(sqLiteDatabase != null){
            sqLiteDatabase.close();
        }
    }

    public boolean checkDataBase(){
        try{
            String myPath = SQLConstantes.DB_PATH + SQLConstantes.DB_NAME;
            sqLiteDatabase = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);
        }catch (Exception e){
            File dbFile = new File(SQLConstantes.DB_PATH + SQLConstantes.DB_NAME);
            return dbFile.exists();
        }
        if (sqLiteDatabase != null) sqLiteDatabase.close();

        return sqLiteDatabase != null ? true : false;
    }

    public void insertarDatosService(Poligono poligono){
        ContentValues contentValues = poligono.toValues();
        sqLiteDatabase.insert(SQLConstantes.tb_poligono,null,contentValues);
    }

    public Poligono getPoligono(String id){
        Poligono poligono = new Poligono();
        String[] whereArgs = new String[]{id};
        Cursor cursor = null;
        try{
            cursor = sqLiteDatabase.query(SQLConstantes.tb_poligono,
                    SQLConstantes.COLUMNAS_TB_POLIGONO,SQLConstantes.WHERE_CLAUSE_ID_TB_POLIGONO,whereArgs,null,null,null);
            if(cursor.getCount() == 1){
                cursor.moveToFirst();
                poligono.setId(cursor.getInt(cursor.getColumnIndex(SQLConstantes.datospoligono_id)));
                poligono.setLatitud(cursor.getDouble(cursor.getColumnIndex(SQLConstantes.datospoligono_latitud)));
                poligono.setLongitud(cursor.getInt(cursor.getColumnIndex(SQLConstantes.datospoligono_longitud)));
            }
        }finally{
            if(cursor != null) cursor.close();
        }
        return poligono;
    }

    public ArrayList<Poligono> getPoligonos(String id){
        ArrayList<Poligono> poligonos = new ArrayList<Poligono>();
        String[] whereArgs = new String[]{id};
        Cursor cursor = null;
        try{
            cursor = sqLiteDatabase.query(SQLConstantes.tb_poligono,
                    SQLConstantes.COLUMNAS_TB_POLIGONO,SQLConstantes.WHERE_CLAUSE_ID_TB_POLIGONO,whereArgs,null,null,null);
            while(cursor.moveToNext()){
                Poligono poligono = new Poligono();
                poligono.setId(cursor.getInt(cursor.getColumnIndex(SQLConstantes.datospoligono_id)));
                poligono.setLatitud(cursor.getDouble(cursor.getColumnIndex(SQLConstantes.datospoligono_latitud)));
                poligono.setLongitud(cursor.getDouble(cursor.getColumnIndex(SQLConstantes.datospoligono_longitud)));
                poligonos.add(poligono);
            }
        }finally {
            if(cursor != null) cursor.close();
        }
        return poligonos;
    }


}
