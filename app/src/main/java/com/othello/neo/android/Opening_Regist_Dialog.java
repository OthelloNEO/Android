package com.othello.neo.android;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.Date;

/**
 * Created by Tomoyuki on 2017/04/23.
 */

public class Opening_Regist_Dialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View regist_view = inflater.inflate(R.layout.regist_view, null);

        final String record = getArguments().getString("RecordMoves");

        ((RadioButton) regist_view.findViewById(R.id.radioButton)).setChecked(true);
        if ((record.length() >= 2) && ((record.substring(0, 2).equals("C4")) | (record.substring(0, 2).equals("D3")))) {
            ((RadioButton) regist_view.findViewById(R.id.radioButton2)).setChecked(true);
        }

        return builder.setTitle("棋譜の登録")
                .setMessage(record)
                .setView(regist_view)
                .setPositiveButton("登録する",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //XXX getActivity()でいいんかこれ??
                                SQLiteDatabase db = new DBhelper(getActivity()).getWritableDatabase();
                                try {
                                    //現在日時=登録日時を取得
                                    final Date nowDate = new Date();

                                    //評価値を取得
                                    Spinner sp = (Spinner) regist_view.findViewById(R.id.spinner_eval);
                                    final String evalValue = (String) sp.getSelectedItem();

                                    //turnを取得
                                    RadioGroup rg = (RadioGroup) regist_view.findViewById(R.id.RadioGroup);
                                    int id = rg.getCheckedRadioButtonId();
                                    RadioButton rb = (RadioButton) regist_view.findViewById(id);
                                    final String turn = (String) rb.getText();

                                    int missTiming = 1;
                                    if (turn.equals("white")) {
                                        missTiming = 2;
                                    }

                                    ContentValues cv = new ContentValues();
                                    cv.put("recordMoves", record);
                                    cv.put("myTurn", turn);
                                    cv.put("evalValue", evalValue);
                                    cv.put("dateTime", nowDate.toString());
                                    cv.put("missTiming", missTiming);
                                    db.insert("game_record", null, cv);
                                } finally {
                                    db.close();
                                }
                            }
                        })
                .setNegativeButton("キャンセル",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                .create();
    }
}
