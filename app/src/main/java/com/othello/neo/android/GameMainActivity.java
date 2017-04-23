package com.othello.neo.android;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Arrays;

import static com.othello.neo.android.Constant_number.*;

/**
 * Created by Tomoyuki on 2017/04/23.
 */

//public class GameMainActivity extends AppCompatActivity {
public class GameMainActivity extends AppCompatActivity {

    private DBhelper helper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main);

        //盤面初期化
        init();

        //データベースヘルパーの取得
        helper = new DBhelper(this);
    }

    //メニュー定義ファイルをもとにオプションメニューを生成
    //棋譜管理や暗記スケジューリングの設定のために用意した
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    //メニュー選択時の処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item0:
                init();
                final Board_View bw = (Board_View) findViewById(R.id.board_view);
                bw.invalidate();
                break;
        }
        return true;
    }

    //盤面や状態の初期化
    public void init() {
        Player_Board = 0x0000000810000000L;
        Opponent_Board = 0x0000001008000000L;
        Turn = BLACK_TURN;
        Num_Move_Step = 1;
        //一手戻る用の配列2つをともに0で一括初期化
        Arrays.fill(Rev_For_Undo, 0);
        Arrays.fill(Touch_Board_For_Undo, 0);
        Arrays.fill(Touch_BitFlag, -1);
        Arrays.fill(IsPass, false);
    }

    //初手に戻るボタン *init()と違って、配列の状態は保持したまま初手に戻る
    public void btnBackToStart_onClick(View view) {
        Player_Board = 0x0000000810000000L;
        Opponent_Board = 0x0000001008000000L;
        Turn = BLACK_TURN;
        Num_Move_Step = 1;
        final Board_View bw = (Board_View) findViewById(R.id.board_view);
        bw.invalidate();
    }

    //Android標準装備のBackキーに「一手前に戻る機能」をつける
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (Num_Move_Step - 1 == 0) return false; //戻り先が無い(=初期配置状態)ならばfalse
            final Board_View bw = (Board_View) findViewById(R.id.board_view);
            if (IsPass[Num_Move_Step - 1] == true) {
                Opponent_Board ^= Rev_For_Undo[Num_Move_Step - 1];
                Player_Board ^= Touch_Board_For_Undo[Num_Move_Step - 1] | Rev_For_Undo[Num_Move_Step - 1];
                bw.board_swap_player(Player_Board, Opponent_Board);
            } else {
                Player_Board ^= Rev_For_Undo[Num_Move_Step - 1];
                Opponent_Board ^= Touch_Board_For_Undo[Num_Move_Step - 1] | Rev_For_Undo[Num_Move_Step - 1];
            }
            bw.invalidate();
            bw.board_swap_player(Player_Board, Opponent_Board);
            //戻ったらそれより先(＝手数後ろ)の状態は捨てる
            Touch_Board_For_Undo[Num_Move_Step] = 0;
            Rev_For_Undo[Num_Move_Step] = 0;
            Touch_BitFlag[Num_Move_Step] = -1;
            IsPass[Num_Move_Step] = false;
            //現在何手目かは、-1をすればよい
            Num_Move_Step -= 1;
            return true;
        }
        return false;
    }

    //一手進むボタン
    public void btnProceed_onClick(View view) {
        //進み先があれば処理を実行
        if (Rev_For_Undo[Num_Move_Step] != 0) {
            final Board_View bw = (Board_View) findViewById(R.id.board_view);
            Player_Board ^= Rev_For_Undo[Num_Move_Step] | Touch_Board_For_Undo[Num_Move_Step];
            Opponent_Board ^= Rev_For_Undo[Num_Move_Step];
            if (IsPass[Num_Move_Step] == true) {
                bw.board_swap_player(Player_Board, Opponent_Board);
            }
            bw.invalidate();
            bw.board_swap_player(Player_Board, Opponent_Board);
            //現在何手目かは、+1をすればよい
            Num_Move_Step += 1;
        }
    }

    //最終手まで進むボタン
    public void btnProceedToFinal_onClick(View view) {
        final Board_View bw = (Board_View) findViewById(R.id.board_view);
        while (Rev_For_Undo[Num_Move_Step] != 0) {
            btnProceed_onClick(view);
        }
        bw.invalidate();
    }

    //棋譜登録ボタン
    public void btnRegistration_onClick(View view) {
        DialogFragment dialog = new Opening_Regist_Dialog();
        String RecordMoves = ""; //棋譜用文字列(F5F6...形式)
        int i = 1;
        while (Touch_BitFlag[i] != -1) {
            RecordMoves = RecordMoves + toCoordinates(Touch_BitFlag[i]);
            i++;
        }
        //フラグメントにRecordMovesを渡す
        Bundle args = new Bundle();
        args.putString("RecordMoves", RecordMoves);
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "regist_dialog");
    }

    //タッチした位置が左上から何ビット右か(touch_BitFlag)をもとに、それを文字座標に変換。棋譜用。
    public String toCoordinates(int touch_BitFlag) {
        String coordinates = ""; //返す座標文字列(F5やH6など)
        switch (touch_BitFlag % 8) {
            case 0:
                coordinates = "A" + String.valueOf((touch_BitFlag - 0) / 8 + 1);
                break;
            case 1:
                coordinates = "B" + String.valueOf((touch_BitFlag - 1) / 8 + 1);
                break;
            case 2:
                coordinates = "C" + String.valueOf((touch_BitFlag - 2) / 8 + 1);
                break;
            case 3:
                coordinates = "D" + String.valueOf((touch_BitFlag - 3) / 8 + 1);
                break;
            case 4:
                coordinates = "E" + String.valueOf((touch_BitFlag - 4) / 8 + 1);
                break;
            case 5:
                coordinates = "F" + String.valueOf((touch_BitFlag - 5) / 8 + 1);
                break;
            case 6:
                coordinates = "G" + String.valueOf((touch_BitFlag - 6) / 8 + 1);
                break;
            case 7:
                coordinates = "H" + String.valueOf((touch_BitFlag - 7) / 8 + 1);
                break;
        }
        return coordinates;
    }

    public void result_Dialog() {
        DialogFragment Result_Dialog = new Result_Dialog();
        Bundle args = new Bundle();
        String black_Disc_num, white_Disc_num;
        //終局時の手番を基準に、石数をカウントし、Result_Dialogに渡す
        if (Turn == BLACK_TURN) {
            black_Disc_num = String.valueOf(Long.bitCount(Player_Board));
            white_Disc_num = String.valueOf(Long.bitCount(Opponent_Board));
        } else {
            white_Disc_num = String.valueOf(Long.bitCount(Player_Board));
            black_Disc_num = String.valueOf(Long.bitCount(Opponent_Board));
        }
        args.putString("black_Disc_num", black_Disc_num);
        args.putString("white_Disc_num", white_Disc_num);
        Result_Dialog.setArguments(args);
        Result_Dialog.show(getFragmentManager(), "result_dialog");
    }

}
