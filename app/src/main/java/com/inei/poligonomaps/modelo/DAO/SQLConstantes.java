package com.inei.poligonomaps.modelo.DAO;

public class SQLConstantes {
    public static String DB_PATH = "/data/data/com.inei.poligonomaps/databases/";
    public static String DB_NAME = "cartoinei.sqlite";
    public static String tb_poligono = "poligono";
    public static String tb_poligono2 = "poligono2";

    public static String datospoligono_id        = "id";
    public static String datospoligono_latitud   = "latitud";
    public static String datospoligono_longitud  = "longitud";

    public static final String SQL_CREATE_TABLE_POLIGONO2=
            "CREATE TABLE " + tb_poligono2  + "(" +
                    datospoligono_id       + " TEXT," +
                    datospoligono_latitud  + " TEXT," +
                    datospoligono_longitud + " TEXT"+");"
            ;

    public static final String[] COLUMNAS_TB_POLIGONO= {
            datospoligono_id,
            datospoligono_latitud,
            datospoligono_longitud
    };

    public static final String WHERE_CLAUSE_ID_TB_POLIGONO = "id=?";

}
