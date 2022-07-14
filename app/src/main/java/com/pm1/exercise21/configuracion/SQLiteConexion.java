package com.pm1.exercise21.configuracion;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteConexion extends SQLiteOpenHelper{

    public SQLiteConexion(Context context, String dbname, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbname, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //crear la tabla personas
        sqLiteDatabase.execSQL(Transacciones.CreateTableVideo);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldver, int newver) {
        //Eliminar las tablas al eliminar la info de DB
        sqLiteDatabase.execSQL(Transacciones.DropTableVideo);
        onCreate(sqLiteDatabase);
    }
}
