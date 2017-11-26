package com.example.adrian.agenda;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Adrian on 06/11/2017.
 */

public class ContactoActivity extends AppCompatActivity {

    TextView textNumero;
    ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);

        textNumero = (TextView) findViewById(R.id.text_numero);
        imageView = (ImageView) findViewById(R.id.image_llamada);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+textNumero.getText().toString()));
                    startActivity(intent);
                    return;
                }else{
                    Toast.makeText(getApplicationContext(), "Activa los permisos para acceder a las llamadas", Toast.LENGTH_LONG);
                }
            }
        });

        String nombre = getIntent().getStringExtra("nombre");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(nombre);

        String numero = getIntent().getStringExtra("numero");

        textNumero.setText(numero);
    }
}
