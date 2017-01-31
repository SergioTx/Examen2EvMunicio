package com.example.sergio.examen2evmunicio;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

/*
    WebServices
 */
public class Actividad2 extends AppCompatActivity {

    //la URL hay que ponerla de esta forma, como indica la web en el apartado POST
    String baseUrl = "http://www.webservicex.net/periodictable.asmx/GetAtomicNumber";

    String symbol, atnum, atwei, boilingPoint, density;
    EditText element;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad2);

        element = (EditText) findViewById(R.id.txt_element);
        text = (TextView) findViewById(R.id.atomicText);
    }

    void getElementInfo(View v){
        AsyncPost task = new AsyncPost();
        task.execute(element.getText().toString());
    }

    void goBack(View v){
        Intent intent = new Intent(Actividad2.this, MainActivity.class);
        startActivity(intent);
    }

    private class AsyncPost extends AsyncTask<String,Void,Void>{

        /*
        Tarea asíncrona que lee los valores del webservice
         */
        @Override
        protected Void doInBackground(String... params) {
            HttpURLConnection conn;
            try {
                URL url = new URL(baseUrl);
                String param = "ElementName=" + URLEncoder.encode(params[0],"UTF-8");
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setFixedLengthStreamingMode(param.getBytes().length);
                conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                PrintWriter out = new PrintWriter((conn.getOutputStream()));
                out.print(param);
                out.close();

                String result = "";

                Scanner inStream = new Scanner(conn.getInputStream());
                while (inStream.hasNextLine()){
                    result = inStream.nextLine();
                    Log.d("LeerDatos",result);
                    if (result.indexOf("AtomicNumber")>0){
                        atnum = result.replace("&lt;AtomicNumber&gt;","").replace("&lt;/AtomicNumber&gt;","");
                    } else if (result.indexOf("Symbol")>0){
                        symbol = result.replace("&lt;Symbol&gt;","").replace("&lt;/Symbol&gt;","");
                    } else if (result.indexOf("AtomicWeight")>0){
                        atwei = result.replace("&lt;AtomicWeight&gt;","").replace("&lt;/AtomicWeight&gt;","");
                    } else if (result.indexOf("Density")>0){
                        density = result.replace("&lt;Density&gt;","").replace("&lt;/Density&gt;","");
                    } else if (result.indexOf("BoilingPoint")>0) {
                        boilingPoint = result.replace("&lt;BoilingPoint&gt;", "").replace("&lt;/BoilingPoint&gt;", "");
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /*
        Rellenar el campo de texto con los valores
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            text.setText(
                    getString(R.string.symbol) + " " + symbol + "\n" +
                    getString(R.string.atomic_number) + " " + atnum + "\n" +
                    getString(R.string.atomic_weight) + " " + atwei + "\n" +
                    getString(R.string.boiling_point) + " " + boilingPoint + "\n" +
                    getString(R.string.denstiy) + " " + density
            );
        }
    }
}
