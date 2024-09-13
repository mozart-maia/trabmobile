package com.example.conversor;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

 class AbrirBD extends SQLiteOpenHelper {
     private static final String NOME_BD = "historico_cotacao";
     private static final int VERSAO_BD = 1;

     public AbrirBD(Context c){
         super(c, NOME_BD, null, VERSAO_BD);
     }

     public void onCreate(SQLiteDatabase bd){
         bd.execSQL("create table historico_cotacao(" +
                 "_id integer primary key autoincrement, " +
                 "valor text not null, " +
                 "moeda_origem text not null, " +
                 "moeda_final text not null, " +
                 "cotacao text not null)");
     }

     public void onUpgrade(SQLiteDatabase bd, int vi, int vf){
         bd.execSQL("drop table historico_cotacao");
         onCreate(bd);
     }


}
