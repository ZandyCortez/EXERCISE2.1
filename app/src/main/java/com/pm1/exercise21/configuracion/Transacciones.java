package com.pm1.exercise21.configuracion;

public class Transacciones {
//  DASE DE DATOS DE SQLITE
    public static final String NameDataBase = "pm01_DB";
//  TABLA DE SQLITE
    public static final String tablaVideo = "tblvideo";
//  CAMPOS DE LA TABLA VIDEO
    public static final String id = "id";
    public static final String video = "video";
//  TRANSACCIONES DDL de VIDEO
    public static final String CreateTableVideo = "CREATE TABLE tblvideo (id INTEGER PRIMARY KEY AUTOINCREMENT,video BLOB)";
    public static final String DropTableVideo = "DROP TABLE IF EXISTS tblvideo";
    public static final String test1 = "select * from tblvideo";
}
