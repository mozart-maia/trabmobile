package com.example.conversor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        new Thread(){
            public void run() {
                StringBuilder response = new StringBuilder();
                try {
                    BufferedReader in = getBufferedReader(response);
                    in.close();
                    Log.i("info_request", response.toString());
                } catch (Exception e) {
                    Log.e("error_request", e.toString());
                }

                runOnUiThread(
                        new Runnable() {
                            public void run() {
                                String usdbrl_allinfo = "";
                                Double onlyprice_usdbrl = 0.0;
                                try {
                                    JSONObject all = new JSONObject(response.toString());
                                    usdbrl_allinfo = all.getString("USDBRL");
                                    JSONObject price = new JSONObject(usdbrl_allinfo);
                                    onlyprice_usdbrl = price.getDouble("bid");

                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                Toast.makeText(MainActivity.this, "Buscando ultima cotação: " + onlyprice_usdbrl.toString() + "...", Toast.LENGTH_SHORT).show();
                                EditText et = findViewById(R.id.editTextCotacao);
                                et.setText(onlyprice_usdbrl.toString());
                            }
                        }
                );
            }

            private @NonNull BufferedReader getBufferedReader(StringBuilder response) throws IOException {
                URL url = new URL("https://economia.awesomeapi.com.br/json/last/USD-BRL,USD-EUR,EUR-BRL");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                int responseCode = connection.getResponseCode();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                return in;
            }
        }.start();

        Button btn_converter = findViewById(R.id.btn_converter);
        btn_converter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent("OPEN_RESULT");
                startActivity(i);
            }
        });
    }
}