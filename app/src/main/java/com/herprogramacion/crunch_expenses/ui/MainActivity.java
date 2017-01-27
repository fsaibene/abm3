package com.herprogramacion.crunch_expenses.ui;

import android.app.ActivityOptions;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.herprogramacion.crunch_expenses.utils.Utilidades;
import com.herprogramacion.crunch_expenses.R;
import com.herprogramacion.crunch_expenses.provider.ContractParaGastos;
import com.herprogramacion.crunch_expenses.sync.SyncAdapter;
import com.herprogramacion.crunch_expenses.ui.ItemClickSupport.*;
import com.herprogramacion.crunch_expenses.web.Gasto;

import static com.herprogramacion.crunch_expenses.R.styleable.RecyclerView;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener  {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private AdaptadorDeGastos adapter;
    private TextView emptyView;
    private int item_layout;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.reciclador);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AdaptadorDeGastos(this);
        recyclerView.setAdapter(adapter);
        emptyView = (TextView) findViewById(R.id.recyclerview_data_empty);

        getSupportLoaderManager().initLoader(0, null, this);

        SyncAdapter.inicializarSyncAdapter(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Gasto edit = AdaptadorDeGastos.irAEditar(recyclerView,position,v);
                Toast.makeText(getApplicationContext(), "CLICKEO ITEM "+position, Toast.LENGTH_SHORT).show();

                showAddScreen(edit);
            }
        });
    }


    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void onClickFab(View v) {
        Intent intent = new Intent(this, InsertActivity.class);
        if (Utilidades.materialDesign())
            startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

        else startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sync) {
            SyncAdapter.sincronizarAhora(this, false);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        emptyView.setText("Cargando datos...");
        // Consultar todos los registros
        return new CursorLoader(
                this,
                ContractParaGastos.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
        emptyView.setText("");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private void showAddScreen(Gasto edit) {
        Intent intent = new Intent(getApplicationContext(), InsertActivity.class);
        intent.putExtra("ETIQUETA",edit.etiqueta);
        intent.putExtra("FECHA",edit.fecha);
        intent.putExtra("MONTO",String.valueOf(edit.monto));
        startActivity(intent);
//        startActivityForResult(intent, AddEditLawyerActivity.REQUEST_ADD_LAWYER);
    }

}
