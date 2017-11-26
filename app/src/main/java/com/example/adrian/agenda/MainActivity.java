package com.example.adrian.agenda;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RecyclerView mRecyclerview;

    private List<Contacto> contactos;

    ContactoRecyclerViewAdapter adapter;

    FloatingActionButton fab;

    private Agenda agenda;
    public static final String nombre = "AGENDA_OBJECT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerView_contactos);

        agenda = (Agenda) SingletonMap.getInstance().get(this.nombre);
        if(agenda == null){
            agenda = new Agenda(getApplicationContext());
            SingletonMap.getInstance().put(this.nombre, agenda);
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEntryActivity.class);
                ActivityOptions activityOptions = ActivityOptions.makeScaleUpAnimation(getCurrentFocus(), (int) fab.getX(), (int) fab.getY(),200,500);
                startActivity(intent,activityOptions.toBundle());
                }
        });

        Cursor res = agenda.getAllData();
        contactos = new ArrayList<>();

        if (res.getCount() == 0) {
            Toast.makeText(this, getResources().getString(R.string.no_contacs), Toast.LENGTH_LONG).show();
        } else {
            while (res.moveToNext()) {
                Contacto c = new Contacto(res.getString(0), res.getString(1), res.getString(2));
                contactos.add(c);
            }
        }

        adapter = new ContactoRecyclerViewAdapter(contactos, this);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.setAdapter(adapter);

        if (getIntent().getBooleanExtra("creado", false)) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
            Snackbar.make(coordinatorLayout, getResources().getString(R.string.creation_contact_successful), Snackbar.LENGTH_LONG).show();
        }

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, getResources().getString(R.string.request_permission), Toast.LENGTH_LONG);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return true;
    }
}
