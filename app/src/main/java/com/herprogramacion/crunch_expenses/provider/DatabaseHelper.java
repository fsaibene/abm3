package com.herprogramacion.crunch_expenses.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.herprogramacion.crunch_expenses.web.Gasto;

/**
 * Clase envoltura para el gestor de Bases de datos
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    public DatabaseHelper(Context context,
                          String name,
                          SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }
    public DatabaseHelper(Context context) {
        super(context, ProviderDeGastos.DATABASE_NAME, null, ProviderDeGastos.DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase database) {
        createTable(database); // Crear la tabla "gasto"
    }

    /**
     * Crear tabla en la base de datos
     *
     * @param database Instancia de la base de datos
     */
    private void createTable(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractParaGastos.GASTO + " (" +
                ContractParaGastos.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractParaGastos.Columnas.MONTO + " TEXT, " +
                ContractParaGastos.Columnas.ETIQUETA + " TEXT, " +
                ContractParaGastos.Columnas.FECHA + " TEXT, " +
                ContractParaGastos.Columnas.DESCRIPCION + " TEXT," +
                ContractParaGastos.Columnas.ID_REMOTA + " TEXT UNIQUE," +
                ContractParaGastos.Columnas.ESTADO + " INTEGER NOT NULL DEFAULT "+ ContractParaGastos.ESTADO_OK+"," +
                ContractParaGastos.Columnas.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try { db.execSQL("drop table " + ContractParaGastos.GASTO); }
        catch (SQLiteException e) { }
        onCreate(db);
    }
    public Cursor getGastoById(String idGasto) {
        Log.i("LLEGO aca",idGasto);
        Cursor c = getReadableDatabase().query(
                ContractParaGastos.Columnas.TABLE_NAME,
                null,
                ContractParaGastos.Columnas.ID_REMOTA + " = ?",
                new String[]{idGasto},
                null,
                null,
                null);
        return c;
    }
    public int deleteGasto(String idGasto) {
        return getWritableDatabase().delete(
                ContractParaGastos.Columnas.TABLE_NAME,
                ContractParaGastos.Columnas.ID_REMOTA + " = ?",
                new String[]{idGasto});
    }
    public int updateGasto(String monto, ContentValues content) {
        return getWritableDatabase().update(
                ContractParaGastos.Columnas.TABLE_NAME,
                content,
                ContractParaGastos.Columnas.MONTO + " = ?",
                new String[]{monto});
    }

}
