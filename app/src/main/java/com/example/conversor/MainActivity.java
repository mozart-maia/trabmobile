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
import java.util.Random;

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


        Double cotacaoUSDBRL = 0.0;
        Double cotacaoBRLUSD = 0.0;



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
                // codigo da requisicao para cotacao com base nas moedas escolhidas
                runOnUiThread(
                        new Runnable() {
                            public void run() {
                                String usdbrl_allinfo = "";
                                double onlyprice_usdbrl = 0.0;
                                try {
                                    JSONObject all = new JSONObject(response.toString());

                                    Button btn_de = findViewById(R.id.button_de);
                                    String de_text = btn_de.getText().toString();
                                    if (de_text.equals("USD")){
                                        usdbrl_allinfo = all.getString("USDBRL");
                                        JSONObject price = new JSONObject(usdbrl_allinfo);
                                        onlyprice_usdbrl = price.getDouble("bid");
                                    } else {
                                        usdbrl_allinfo = all.getString("BRLUSD");
                                        JSONObject price = new JSONObject(usdbrl_allinfo);
                                        onlyprice_usdbrl = price.getDouble("bid");
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                Toast.makeText(MainActivity.this, "Buscando ultima cotação: " + Double.toString(onlyprice_usdbrl) + "...", Toast.LENGTH_SHORT).show();
                                EditText et = findViewById(R.id.editTextCotacao);
                                et.setText(Double.toString(onlyprice_usdbrl));
                            }
                        }
                );
            }

            private @NonNull BufferedReader getBufferedReader(StringBuilder response) throws IOException {
                // api que estou utilizando
                URL url = new URL("https://economia.awesomeapi.com.br/json/last/USD-BRL,BRL-USD");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
//                int responseCode = connection.getResponseCode();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                return in;
            }
        }.start();

        Button btn_converter = findViewById(R.id.btn_converter);
        Button btn_de = findViewById(R.id.button_de);
        Button btn_para = findViewById(R.id.button_para);
        Button btn_swap = findViewById(R.id.button_swap);
        EditText et_valor = findViewById(R.id.editText_valor);
        EditText et_cotacao = findViewById(R.id.editTextCotacao);

        btn_swap.setOnClickListener(v -> {
            String de_text = btn_de.getText().toString();
            if (de_text.equals("USD")) {
                btn_de.setText("BRL");
                btn_para.setText("USD");
            } else {
                btn_de.setText("USD");
                btn_para.setText("BRL");
            }


        });
        // ao clicar no botao converter vai executar as seguitnes ações
        btn_converter.setOnClickListener(v -> {
            double cotacao = Double.parseDouble(String.valueOf(et_cotacao.getText()));
            double valor = Double.parseDouble(String.valueOf(et_valor.getText()));
            String resultado = String.format("%.2f",valor / cotacao);

            //codigo para inserir calculo de cotacao ao banco de dados:
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    HistoricoCotacao hc = new HistoricoCotacao();
                    // gerando um id aleatorio para inserir no banco de dados
                    Random rand = new Random();
                    int nr = rand.nextInt(1000000000);
                    hc.setId(nr);
                    hc.setValor(String.valueOf(valor));
                    Log.i("valor_banco", String.valueOf(et_valor.getText()));
                    hc.setCotacao(String.valueOf(cotacao));
                    hc.setMoedaOrigem("USD");
                    hc.setMoedaFinal("BRL");
                    BD bd = new BD(getBaseContext());
                    bd.inserir(hc);
                }
            });

            // enviando para a segunda activity atraves de intent
            Bundle b = new Bundle();
            b.putString("valor", String.valueOf(valor));
            b.putString("resultado", resultado);
            b.putString("moeda_origem", "USD");
            b.putString("moeda_final", "BRL");
            Intent i = new Intent("OPEN_RESULT");
            i.putExtras(b);
            startActivity(i);

        });
    }
}