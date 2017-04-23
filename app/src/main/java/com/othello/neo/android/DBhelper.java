package com.othello.neo.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tomoyuki on 2017/04/23.
 */

public class DBhelper extends SQLiteOpenHelper {
    static final private String DBNAME = "sample.sqlite";
    static final private int VERSION = 1;

    public DBhelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /* 以下のような表を作成 (値や文字はサンプル値)
           棋譜ID    棋譜txt      想定手番     評価値      最新読み込み日時        間違えた場面
          recordID  recordMoves    myTurn     evalValue         DateTime            missTiming
             0       F5F6E6...       black        +4            yyyy/mm/...              14

 <用途> 棋譜ID→主キー。また、登録日時の降順でもある。
        棋譜txt→暗記チャレンジモードでは、優先度から自動で選ばれたものの棋譜txtとの整合性をチェックする
        想定手番→暗記手番の識別
        評価値→評価値を基準に暗記チャレンジする進行を選びたいとき用。また、優先度付けの一材料でもある。
        最新読み込み日時→基本的にこれが古いものは優先度が高くなる
        間違えた場面→暗記チャレンジモードをはじめる時に、覚えているところまでは自動で進めておくため。初期値はデフォルト制約で1(=初手)にしてある。
         */

        db.execSQL("CREATE TABLE game_record (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "recordMoves TEXT, " +
                "myTurn TEXT, " +
                "evalValue INTEGER, " +
                "dateTime TEXT, " +
                "missTiming INTEGER )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old_v, int new_v) {
        db.execSQL("DROP TABLE IF EXISTS game_record");
        onCreate(db);
    }
}
