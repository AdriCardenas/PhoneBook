package com.example.adrian.agenda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrian on 08/11/2017.
 */

public class Agenda {
    private DatabaseHelper databaseHelper;

    public Agenda(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public Cursor getAllData() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + DatabaseHelper.TABLE_NAME, null);
        return res;
    }

    public boolean insertData(String nombre, String numero) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.CONTACT_NAME, nombre);
        contentValues.put(DatabaseHelper.CONTACT_NUMBER, numero);
        //Inserta un usuario si hay conflicto lo sobrescribe
        long result = db.insertWithOnConflict(DatabaseHelper.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<Contacto> searchUser(String nombre) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        List<Contacto> contactos = new ArrayList<>();
        String sql = String.format("SELECT * FROM %s WHERE %s like ?", DatabaseHelper.TABLE_NAME, DatabaseHelper.CONTACT_NAME);
        String[] selectionArg = {nombre+"%"};
        Cursor res = db.rawQuery(sql, selectionArg);
        while(res.moveToNext()){
            Contacto c = new Contacto(res.getString(0),res.getString(1),res.getString(2));
            contactos.add(c);
        }
        return contactos;
    }
}
