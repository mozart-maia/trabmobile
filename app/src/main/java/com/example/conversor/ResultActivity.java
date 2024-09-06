package com.example.conversor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent i = getIntent();
        Bundle b = i.getExtras();

        Log.i("info_result", b.getString("valor"));
        Log.i("info_result", b.getString("resultado"));
        Log.i("info_result", b.getString("moeda_origem"));
        Log.i("info_result", b.getString("moeda_final"));

        TextView tv_moeda_origem = findViewById(R.id.textView_moeda_origem);
        TextView tv_moeda_final = findViewById(R.id.textView_moedafinal);
        TextView tv_valor = findViewById(R.id.textView_valor);
        TextView tv_resultado = findViewById(R.id.textView_resultado);

        tv_moeda_origem.setText(b.getString("moeda_origem"));
        tv_moeda_final.setText(b.getString("moeda_final"));
        tv_resultado.setText(b.getString("resultado"));
        tv_valor.setText(b.getString("valor"));



        Button btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}