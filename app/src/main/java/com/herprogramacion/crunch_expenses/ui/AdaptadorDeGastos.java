package com.herprogramacion.crunch_expenses.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.herprogramacion.crunch_expenses.R;
import com.herprogramacion.crunch_expenses.web.Gasto;

/**
 * Adaptador del recycler view
 */
public class AdaptadorDeGastos extends RecyclerView.Adapter<AdaptadorDeGastos.ViewHolder>{
    private Cursor cursor;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Campos respectivos de un item
        public TextView monto;
        public TextView etiqueta;
        public TextView fecha;
        public TextView idRemota;

        public ViewHolder(View v) {
            super(v);
            monto = (TextView) v.findViewById(R.id.monto);
            etiqueta = (TextView) v.findViewById(R.id.etiqueta);
            fecha = (TextView) v.findViewById(R.id.fecha);
            idRemota = (TextView) v.findViewById(R.id.idGasto);
        }

        @Override
        public void onClick(View view) {

        }
    }
    public static interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
    public static Gasto irAEditar(RecyclerView recyclerView, int position, View v){
        TextView idGasto = (TextView) v.findViewById(R.id.idGasto);
        TextView monto = (TextView) v.findViewById(R.id.monto);
        TextView etiqueta = (TextView) v.findViewById(R.id.etiqueta);
        TextView fecha = (TextView) v.findViewById(R.id.fecha);

        String montoEdit = (String)monto.getText();
        String etiquetaEdit = (String)etiqueta.getText();
        String fechaEdit = (String)fecha.getText();
        String idGastoEdit = (String)idGasto.getText();

        Gasto retorno = new Gasto(idGastoEdit, Integer.valueOf(montoEdit.substring(1)), etiquetaEdit, fechaEdit, "buscar");
        return  retorno;
    }

    public AdaptadorDeGastos(Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        if (cursor!=null)
        return cursor.getCount();
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        cursor.moveToPosition(i);

        String monto;
        String etiqueta;
        String fecha;
        String idRemota;

        monto = cursor.getString(1);
        etiqueta = cursor.getString(2);
        fecha = cursor.getString(3);
        idRemota = cursor.getString(5);


        viewHolder.monto.setText("$"+monto);
        viewHolder.etiqueta.setText(etiqueta);
        viewHolder.fecha.setText(fecha);
        viewHolder.idRemota.setText(idRemota);
    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }


}