package com.inei.poligonomaps.modelo.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {
    private final Context myContext;
    private SQLiteDatabase myDatabase;

    public DataBaseHelper(@Nullable Context context) {
        super(context,SQLConstantes.DB_NAME,null,1);
        this.myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
