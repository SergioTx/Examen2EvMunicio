package com.example.sergio.examen2evmunicio;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class Actividad1 extends AppCompatActivity {

    private String urlVitoria = "http://xml.tutiempo.net/xml/8043.xml";
    private String urlBilbao = "http://xml.tutiempo.net/xml/8050.xml";
    private String urlDonostia = "http://xml.tutiempo.net/xml/4917.xml";

    private final int PERMISION_INTERNET = 1;
    private TextView text;
    private String ciudad;
    private List<String> datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad1);

        text = (TextView)findViewById(R.id.txt_temperaturas);
    }

    void getBilbao(View v){
        Log.d("BILBAO","entra");
        LoadXml loader = new LoadXml();
        loader.execute(urlBilbao);
        ciudad = getString(R.string.bilbao);
    }

    void getVitoria(View v){
        Log.d("VITORIA","entra");
        LoadXml loader = new LoadXml();
        loader.execute(urlVitoria);
        ciudad = getString(R.string.vitoria);
    }

    void getSanSebastian(View v){
        Log.d("SAN-SEBASTIAN","entra");
        LoadXml loader = new LoadXml();
        loader.execute(urlDonostia);
        ciudad = getString(R.string.sansebastian);
    }

    void goBack(View v){
        Intent intent = new Intent(Actividad1.this, MainActivity.class);
        startActivity(intent);
    }

    class LoadXml extends AsyncTask<String,Integer,Boolean>{

        @Override
        protected Boolean doInBackground(String... params) {
            XmlParser parser = new XmlParser(params[0]);
            datos = parser.returnTimeTempSky();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            String hour = datos.get(0);
            String temperature = datos.get(1);
            String sky = datos.get(2);

            Log.d("XML",hour + "");
            Log.d("XML",temperature + "");
            Log.d("XML",sky + "");

            text.setText(getText(R.string.local_weather) + " " + ciudad + "\n\n"
                    + getString(R.string.hour) + ": " + hour + "\n"
                    + getString(R.string.temp) + ": " + temperature + "\n"
                    + getString(R.string.sky) + ": " + sky + "\n");
        }
    }
}



