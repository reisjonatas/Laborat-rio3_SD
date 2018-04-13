package com.example.william.lab3.lab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class PaisesDataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_NOME,  MySQLiteHelper.COLUMN_LATITUDE,MySQLiteHelper.COLUMN_LONGITUDE};

    public PaisesDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void createPais(String nome,String latitue,String longitude) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NOME, nome);
        values.put(MySQLiteHelper.COLUMN_LATITUDE, latitue);
        values.put(MySQLiteHelper.COLUMN_LONGITUDE, longitude);

        long insertId = database.insert(MySQLiteHelper.TABLE_PAISES, null,
                values);
        /*Cursor cursor = database.query(MySQLiteHelper.TABLE_PAISES,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Comment newComment = cursorToComment(cursor);
        cursor.close();*/

    }

    public void deletePais(Pais pais) {
        String nome = pais.getName();
        System.out.println("Pais deleted with Name: " + nome);
        database.delete(MySQLiteHelper.TABLE_PAISES, MySQLiteHelper.COLUMN_NOME
                + " = " + nome, null);
    }

    public Pais getPais(String nome) {
        String query = "Select * From paises Where nome = 'Albania'";
        Cursor  c = database.rawQuery(query,null);
        c.moveToFirst();
        Pais p = new Pais();
        p.setName(nome);
        p.setLatitude(c.getString(c.getColumnIndex("latitude")));
        p.setLongitude(c.getString(c.getColumnIndex("longitude")));
        return p;
    }

    public List<Pais> getAllPaises() {
        List<Pais> paises = new ArrayList<Pais>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_PAISES,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Pais pais = cursorToPais(cursor);
            paises.add(pais);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return paises;
    }

    private Pais cursorToPais(Cursor cursor) {
        Pais pais = new Pais();
        pais.setName(cursor.getString(1));
        pais.setLatitude(cursor.getString(2));
        pais.setLongitude(cursor.getString(3));
        return pais;
    }
}
