package com.example.conversor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class BD {
    private SQLiteDatabase bd;

    public BD(Context c){
        AbrirBD bdCore = new AbrirBD(c);
        bd = bdCore.getWritableDatabase();
    }

    public void inserir(HistoricoCotacao hc){
        ContentValues cv = new ContentValues();
        cv.put("valor", hc.getValor());
        cv.put("moeda_origem", hc.getMoedaOrigem());
        cv.put("moeda_final", hc.getMoedaFinal());
        cv.put("cotacao", hc.getCotacao());

        bd.insert("historico_cotacao", null, cv);
    }

    public List<HistoricoCotacao> buscar(){
        List<HistoricoCotacao> listahc = new ArrayList<HistoricoCotacao>();
        String[] colunas = new String[]{"_id", "valor", "moeda_origem", "moeda_final", "cotacao"};
        Cursor c = bd.query("historico_cotacao", colunas, null, null, null, null, "_id");

        if (c.getCount() == 0){
            c.moveToFirst();
            do {
                HistoricoCotacao hc = new HistoricoCotacao();
                hc.setId(c.getInt(0));
                hc.setValor(c.getString(1));
                hc.setMoedaOrigem(c.getString(2));
                hc.setMoedaFinal(c.getString(3));
                hc.setCotacao(c.getString(4));

                listahc.add(hc);
            }while(c.moveToNext());
        } else {
            System.out.println("SEM DADOS");
        }
        return listahc;
    }
}
