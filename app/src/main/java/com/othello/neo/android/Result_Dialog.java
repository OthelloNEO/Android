package com.othello.neo.android;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by Tomoyuki on 2017/04/23.
 */

//対局終了時に、Board_Viewのfinish_checkから呼ばれる,結果表示ダイアログ
public class Result_Dialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //ダイアログを生成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //ダイアログの設定
        String black_Disc_num = getArguments().getString("black_Disc_num");
        String white_Disc_num = getArguments().getString("white_Disc_num");
        String result_title = "白の勝ち";
        if (Integer.parseInt(black_Disc_num) == Integer.parseInt(white_Disc_num)) {
            result_title = "引き分け";
        } else if (Integer.parseInt(black_Disc_num) > Integer.parseInt(white_Disc_num)) {
            result_title = "黒の勝ち";
        }
        return builder.setTitle("結果  " + result_title)
                .setMessage("黒： " + black_Disc_num + "  白: " + white_Disc_num)
                .create();
    }
}
