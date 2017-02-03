package com.herprogramacion.crunch_expenses.utils;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.herprogramacion.crunch_expenses.provider.ContractParaGastos;
import com.herprogramacion.crunch_expenses.web.Gasto;

import org.json.JSONException;
import org.json.JSONObject;

public class Utilidades {
    // Indices para las columnas indicadas en la proyección
    public static final int COLUMNA_ID = 0;
    public static final int COLUMNA_ID_REMOTA = 1;
    public static final int COLUMNA_MONTO = 2;
    public static final int COLUMNA_ETIQUETA = 3;
    public static final int COLUMNA_FECHA = 4;
    public static final int COLUMNA_DESCRIPCION = 5;

    /**
     * Determina si la aplicación corre en versiones superiores o iguales
     * a Android LOLLIPOP
     *
     * @return booleano de confirmación
     */
    public static boolean materialDesign() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * Copia los datos de un gasto almacenados en un cursor hacia un
     * JSONObject
     *
     * @param c cursor
     * @return objeto jason
     */
    public static JSONObject deCursorAJSONObject(Cursor c) {
        JSONObject jObject = new JSONObject();
        String idGasto;
        String monto;
        String etiqueta;
        String fecha;
        String descripcion;

        idGasto = c.getString(COLUMNA_ID_REMOTA);
        monto = c.getString(COLUMNA_MONTO);
        etiqueta = c.getString(COLUMNA_ETIQUETA);
        fecha = c.getString(COLUMNA_FECHA);
        descripcion = c.getString(COLUMNA_DESCRIPCION);

        try {
            jObject.put(ContractParaGastos.Columnas.ID_REMOTA, idGasto);
            jObject.put(ContractParaGastos.Columnas.MONTO, monto);
            jObject.put(ContractParaGastos.Columnas.ETIQUETA, etiqueta);
            jObject.put(ContractParaGastos.Columnas.FECHA, fecha);
            jObject.put(ContractParaGastos.Columnas.DESCRIPCION, descripcion);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Cursor a JSONObject", String.valueOf(jObject));

        return jObject;
    }
    public static JSONObject deGastoAJSONObject(Gasto g) {
        JSONObject jObject = new JSONObject();
        String idGasto;
        String monto;
        String etiqueta;
        String fecha;
        String descripcion;

        monto = String.valueOf(g.monto);
        etiqueta = g.etiqueta;
        idGasto = g.idGasto;
        fecha = g.fecha;
        descripcion = g.descripcion;

        try {
            jObject.put("idGasto", idGasto);
            jObject.put(ContractParaGastos.Columnas.MONTO, monto);
            jObject.put(ContractParaGastos.Columnas.ETIQUETA, etiqueta);
            jObject.put(ContractParaGastos.Columnas.FECHA, fecha);
            jObject.put(ContractParaGastos.Columnas.DESCRIPCION, descripcion);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Gasto a JSONObject", String.valueOf(jObject));

        return jObject;
    }
}
