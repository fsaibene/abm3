package com.herprogramacion.crunch_expenses.ui;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.herprogramacion.crunch_expenses.utils.Utilidades;
import com.herprogramacion.crunch_expenses.R;
import com.herprogramacion.crunch_expenses.provider.ContractParaGastos;
import com.herprogramacion.crunch_expenses.provider.DatabaseHelper;
import com.herprogramacion.crunch_expenses.sync.SyncAdapter;
import com.herprogramacion.crunch_expenses.web.Gasto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.IllegalFormatCodePointException;

/**
 * Actividad de inserción para los gastos
 */
public class InsertActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    EditText monto;
    Spinner etiqueta;
    TextView fecha;
    TextView idGastoTV;
    EditText descripcion;
    DatabaseHelper mDbHelper;
    Button btnAgregar;
    Button btnBorrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        mDbHelper = new DatabaseHelper(getApplicationContext());
        setToolbar();

        monto = (EditText) findViewById(R.id.monto);
        etiqueta = (Spinner) findViewById(R.id.categoria);
        idGastoTV = (TextView) findViewById(R.id.idGasto);
        fecha = (TextView) findViewById(R.id.fecha);
        descripcion = (EditText) findViewById(R.id.descripcion);
        btnAgregar = (Button) findViewById(R.id.boton);
        btnBorrar = (Button) findViewById(R.id.botonBorrar);

        fecha.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DateDialog().show(getSupportFragmentManager(), "DatePicker");
                    }
                }
        );
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if (extras!=null){
            String idGasto = extras.getString("ID");

            Gasto aux = this.getGasto(idGasto);
            String aux1 = String.valueOf(aux.monto);
            monto.setText(aux1);
            fecha.setText(aux.fecha);
            descripcion.setText(aux.descripcion);
            idGastoTV.setText(aux.idGasto);
            btnAgregar.setText("GUARDAR");
            switch (aux.etiqueta){
                case "Transporte":
                    etiqueta.setSelection(1);
                    break;
                case "Comida":
                    etiqueta.setSelection(0);
                    break;
                case "Diversión":
                    etiqueta.setSelection(2);
                    break;
            }
        } else {
            btnBorrar.setVisibility(View.GONE);
        }
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void alClickearBoton(View v) {
        String montoText = monto.getText().toString();
        String etiquetaText = etiqueta.getSelectedItem().toString();
        String fechaText = fecha.getText().toString();
        String descripcionText = descripcion.getText().toString();

        ContentValues values = new ContentValues();
        values.put(ContractParaGastos.Columnas.MONTO, montoText);
        values.put(ContractParaGastos.Columnas.ETIQUETA, etiquetaText);
        values.put(ContractParaGastos.Columnas.FECHA, fechaText);
        values.put(ContractParaGastos.Columnas.DESCRIPCION, descripcionText);
        Toast.makeText(getApplicationContext(), "Entro agregarBtn", Toast.LENGTH_SHORT).show();
        String opcion = btnAgregar.getText().toString();
        Log.i("OPCION:", opcion);
        if(opcion.length() >= 8) {
            Toast.makeText(getApplicationContext(), "Entro agregarBtn", Toast.LENGTH_SHORT).show();
            values.put(ContractParaGastos.Columnas.PENDIENTE_INSERCION, 1);
            getContentResolver().insert(ContractParaGastos.CONTENT_URI, values);
            SyncAdapter.sincronizarAhora(this, true);
            if (Utilidades.materialDesign())
                finishAfterTransition();
            else finish();

        } else {
            Log.i("ACTUALIZACION", "SE VA A ACTUALIZAR");
            int retorno = mDbHelper.updateGasto(montoText, values);
            if(retorno == 1){
//            getContentResolver().delete(ContractParaGastos.CONTENT_URI, ContractParaGastos.Columnas.MONTO+" = ?", new String[]{montoText});
                Gasto g = new Gasto(idGastoTV.getText().toString(),Integer.parseInt(montoText), etiquetaText, fechaText, descripcionText);
                SyncAdapter s = new SyncAdapter(getApplicationContext(), true);
                s.setGastoABorrar(g);//
                s.realizarSincronizacionRemotaUpdate();
                if (Utilidades.materialDesign())
                    finishAfterTransition();
                else finish();
                Toast.makeText(getApplicationContext(), "Se actualizo el registro SQLITE", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "FALLO UPDATE", Toast.LENGTH_SHORT).show();
            }
        }

    }
    public void alClickearBotonBorrar(View v) {
        String idGastoText = idGastoTV.getText().toString();
        int retorno = mDbHelper.deleteGasto(idGastoText);
        if(retorno == 1){
            Toast.makeText(getApplicationContext(), "Se elimino el registro SQLITE", Toast.LENGTH_SHORT).show();
            Gasto g = new Gasto(idGastoText,Integer.parseInt(idGastoText), idGastoText, idGastoText, idGastoText);
            SyncAdapter s = new SyncAdapter(getApplicationContext(), true);
            s.setGastoABorrar(g);//
            s.realizarSincronizacionRemotaBorrados();
            if (Utilidades.materialDesign())
                finishAfterTransition();
            else finish();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "MAL AHI", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(year + "-" + monthOfYear + "-" + dayOfMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outDate = dateFormat.format(date);

        fecha.setText(outDate);
    }

    public Gasto getGasto(String id){
        Cursor r = mDbHelper.getGastoById(id);
        if (r.getCount()>= 1){
            Log.i("TIENE", "tiene");
        }else {
            Log.i("NO TIENE", "NO TIENE");
        }
        if(r.moveToFirst()) {
            int etiquetaIndex = r.getColumnIndex(ContractParaGastos.Columnas.ETIQUETA);
            int descripcionIndex = r.getColumnIndex(ContractParaGastos.Columnas.DESCRIPCION);
            int montoIndex = r.getColumnIndex(ContractParaGastos.Columnas.MONTO);
            int fechaIndex = r.getColumnIndex(ContractParaGastos.Columnas.FECHA);

            String etiqueta = r.getString(etiquetaIndex);
            String descripcion = r.getString(descripcionIndex);
            String montoN = r.getString(montoIndex);
            String fecha = r.getString(fechaIndex);
            Gasto retorno = new Gasto(id, Integer.parseInt(montoN), etiqueta, fecha, descripcion);
            return retorno;
        } else {
            return null;
        }
    }

}
