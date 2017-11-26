package com.example.adrian.agenda;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class AddEntryActivity extends AppCompatActivity {


    TextInputLayout textInputNombre;
    TextInputLayout textInputNumero;

    private Agenda agenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        agenda = (Agenda) SingletonMap.getInstance().get(MainActivity.nombre);
        if(agenda == null){
            agenda = new Agenda(this);
            SingletonMap.getInstance().put(MainActivity.nombre, agenda);
        }

        textInputNombre = (TextInputLayout) findViewById(R.id.nombre);
        textInputNumero = (TextInputLayout) findViewById(R.id.numero);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.insert, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_insert) {
            if (camposCorrectos()) {
                createSimpleDialog(agenda).show();
            } else {
                Toast.makeText(this, getResources().getString(R.string.incorrect_fields), Toast.LENGTH_LONG).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @NonNull
    private String getNombre() {
        return textInputNombre.getEditText().getText().toString();
    }

    @NonNull
    private String getNumero() {
        return textInputNumero.getEditText().getText().toString();
    }


    private boolean camposCorrectos() {
        return textInputNumero.getEditText().getText() != null && !textInputNumero.getEditText().getText().toString().equals("") && textInputNumero.getEditText().getText().toString().length() == 9 && textInputNombre.getEditText().getText() != null && !textInputNombre.getEditText().getText().toString().equals("");
    }

    private AlertDialog createSimpleDialog(final Agenda agenda) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getResources().getString(R.string.warning))
                .setMessage(getResources().getString(R.string.warning_question))
                .setPositiveButton(getResources().getString(R.string.accept),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                agenda.insertData(getNombre(), getNumero());
                                Intent intent = new Intent(AddEntryActivity.this, MainActivity.class);
                                intent.putExtra("creado", true);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //nothing
                            }
                        });

        return builder.create();
    }
}
