package com.example.sergio.examen2evmunicio;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Actividad3 extends AppCompatActivity {

    Spinner spinner;
    SoundPool soundPool;
    int idAudio, idShot,idExplosion;

    void goBack(View v){
        Intent intent = new Intent(Actividad3.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad3);

        //cargar el spinner
        spinner = (Spinner) findViewById(R.id.spinnerAudio);
        //cargarlo desde archivo
        ArrayList<String> audios = new ArrayList<String>();
        try{
            InputStream fraw = getResources().openRawResource(R.raw.sonidos);
            BufferedReader brin = new BufferedReader(new InputStreamReader(fraw));
            String linea = brin.readLine();
            while (linea != null){
                //para mantener los idiomas
                if (linea.equals("audio"))
                    audios.add(getResources().getString(R.string.audio));
                else if (linea.equals("disparo"))
                    audios.add(getResources().getString(R.string.shot));
                else if (linea.equals("explosion"))
                    audios.add(getResources().getString(R.string.explosion));
                linea = brin.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        final String[] audioArray = new String[audios.size()];
        for(int i = 0;i<audios.size();i++){
            audioArray[i] = audios.get(i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,audioArray);
        spinner.setAdapter(adapter);

        //cargar el soundPool
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        idAudio = soundPool.load(this,R.raw.audio,0);
        idShot = soundPool.load(this,R.raw.disparo,0);
        idExplosion = soundPool.load(this,R.raw.explosion,0);

        //listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //los logs debug no se ejecutan fuera del entorno de desarrollo, se pueden dejar
                Log.d("SPINNER","L: " + l);
                Log.d("SPINNER","I: " + i);
                //tanto L como I cogen el índice
                if (l==0)
                    soundPool.play(idAudio,1,1,0,0,1);
                else if (l==1)
                    soundPool.play(idShot,1,1,0,0,1);
                else if (l==2)
                    soundPool.play(idExplosion,1,1,0,0,1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //no hacer nada
            }
        });
    }
}
