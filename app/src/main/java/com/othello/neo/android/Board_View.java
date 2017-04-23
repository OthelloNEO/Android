package com.othello.neo.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import static com.othello.neo.android.Constant_number.*;

/**
 * Created by Tomoyuki on 2017/04/23.
 */

public class Board_View extends View {

    public Board_View(Context context) {
        super(context);
    }

    public Board_View(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Board_View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final float display_Width = getWidth();
        Square_Length = display_Width / BOARD_SIZE; //1マスの縦と横の長さ(正方形)

        //盤面(緑)の描画
        Paint board_paint = new Paint();
        board_paint.setColor(Color.GREEN);
        canvas.drawRect(0, 0, getWidth(), display_Width, board_paint);

        //盤面の線引き
        Paint board_line = new Paint();
        board_line.setColor(Color.BLACK);
        board_line.setStrokeWidth(BOARD_LINE_STORKE_WIDTH);
        for (int i = 0; i <= BOARD_SIZE; i++) {
            canvas.drawLine(0, i * Square_Length, display_Width, i * Square_Length, board_line);  //横線
            canvas.drawLine(i * Square_Length, 0, i * Square_Length, display_Width, board_line);  //縦線
        }

        //黒ボッチの描画(内側に4つの点を描く)
        Paint dot_inside = new Paint();
        dot_inside.setColor(Color.BLACK);
        dot_inside.setStrokeWidth(10);
        canvas.drawPoint(2 * Square_Length, 2 * Square_Length, dot_inside); //左上
        canvas.drawPoint(2 * Square_Length, (BOARD_SIZE - 2) * Square_Length, dot_inside); //左下
        canvas.drawPoint((BOARD_SIZE - 2) * Square_Length, 2 * Square_Length, dot_inside); //右上
        canvas.drawPoint((BOARD_SIZE - 2) * Square_Length, (BOARD_SIZE - 2) * Square_Length, dot_inside); //右下

        //黒石の描画設定
        Paint black_Disc = new Paint();
        black_Disc.setColor(Color.BLACK);
        black_Disc.setStyle(Paint.Style.FILL);
        black_Disc.setAntiAlias(true);
        //白石の描画設定
        Paint white_Disc = new Paint();
        white_Disc.setColor(Color.WHITE);
        white_Disc.setStyle(Paint.Style.FILL);
        white_Disc.setAntiAlias(true);

        //メイン描画！！
        //ToDo 割り算をシフト演算に書き直すこと。(他の箇所も同様)
        for (int i = 0; i < 64; i++) {
            //playerサイドの描写
            if (checkBit(Player_Board, i)) {
                int x = i % BOARD_SIZE;
                int y = i / BOARD_SIZE;
                if (Turn == BLACK_TURN) {
                    canvas.drawCircle(x * Square_Length + Square_Length / 2, y * Square_Length + Square_Length / 2, Square_Length / 2 - CIRCLE_RADIUS_PARAM, black_Disc);
                } else {
                    canvas.drawCircle(x * Square_Length + Square_Length / 2, y * Square_Length + Square_Length / 2, Square_Length / 2 - CIRCLE_RADIUS_PARAM, white_Disc);
                }
            }
            //opponentサイドの描写
            if (checkBit(Opponent_Board, i)) {
                int x = i % BOARD_SIZE;
                int y = i / BOARD_SIZE;
                if (Turn == BLACK_TURN) {
                    canvas.drawCircle(x * Square_Length + Square_Length / 2, y * Square_Length + Square_Length / 2, Square_Length / 2 - 2, white_Disc);
                } else {
                    canvas.drawCircle(x * Square_Length + Square_Length / 2, y * Square_Length + Square_Length / 2, Square_Length / 2 - 2, black_Disc);
                }
            }
        }
    }

    //1ビットずつ左にシフトしていく,ビットチェック関数
    public boolean checkBit(long board, int n) {
        return (((board >>> 63 - n) & 1) == 1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touch_X = event.getX();
        float touch_Y = event.getY();
        //ボード外のタッチは無視する
        if (touch_Y > Square_Length * BOARD_SIZE) {
            return false;
        }
        //タッチした座標が左上(0,0)から何ビット右なのかを計算
        int touch_BitFlag = (int) (touch_Y / Square_Length) * 8 + (int) (touch_X / Square_Length);
        //タップしたマスだけにビットが立っている,touch_boardを作成
        //ToDo 以下のtouch_boardの代入分岐はどう考えても冗長。
        long touch_board;
        if (touch_BitFlag == 0) {
            touch_board = 0x8000000000000000L;
        } else {
            touch_board = (long) Math.pow(2, 63 - touch_BitFlag);
        }

        //タップした位置におけるかどうかをcan_put関数でチェックし、trueなら処理を行う
        if (can_put(Player_Board, Opponent_Board, touch_board)) {
            //タップした位置に置いた場合の、盤面変化後の状態をrevで返す
            long rev = reverse(Player_Board, Opponent_Board, touch_board);

            //再生時につながり、棋譜がおかしくならないようにするため
            if (touch_board != Touch_Board_For_Undo[Num_Move_Step]) {
                Touch_Board_For_Undo[Num_Move_Step + 1] = 0;
                Rev_For_Undo[Num_Move_Step + 1] = 0;
                Touch_BitFlag[Num_Move_Step + 1] = -1;
                for (int i = Num_Move_Step; i < 64; i++) {
                    IsPass[i] = false;
                }
            }

            //一手戻る時用に保存
            Rev_For_Undo[Num_Move_Step] = rev;
            Touch_Board_For_Undo[Num_Move_Step] = touch_board;
            Touch_BitFlag[Num_Move_Step] = touch_BitFlag;

            //石の反転を反映させて各々のboardに代入
            Player_Board ^= touch_board | rev;
            Opponent_Board ^= rev;

            //ボードの入れ替え及び手番チェンジ
            board_swap_player(Player_Board, Opponent_Board);
            //パスをチェックしオートでパス処理を行う
            pass_check();
            //今何手目であるかを更新
            Num_Move_Step += 1;
            //終局判定を行う。終局の場合はAlertDialogである"Result_Dialog"の表示をMainActivityに依頼する。
            finish_check();
            //再描画
            invalidate();
        }
        return false;
    }

    //左上からnビット右に置けるかどうかのチェック関数
    public boolean can_put(long player_board, long opponent_board, long touch_board) {
        //着手可能マスにのみビットが立っている,legal_boardをLegal_places関数によって作成する
        long legal_board = Legal_places(player_board, opponent_board);
        if ((touch_board & legal_board) == touch_board) {
            return true;
        }
        return false;
    }

    //合法手の位置にのみビットが立っているボードを生成する
    public long Legal_places(long player_board, long opponent_board) {
        long opponent_board_remove_RightLeft = opponent_board & 0x7e7e7e7e7e7e7e7eL; //opponent_boardの左右端のみを取り除いた
        long opponent_board_remove_UpDown = opponent_board & 0x00FFFFFFFFFFFF00L; //opponent_boardの上下端のみを取り除いた
        long opponent_board_remove_AllEdge = opponent_board & 0x007e7e7e7e7e7e00L; //opponent_boardの全辺を取り除いた
        long legal_places; //着手可能マスにのみビットが立っている,返却用long値
        long tmp; //隣に相手の色があるかの検査をした結果を一時的に保存する用
        long blank_board = ~(player_board | opponent_board); //空きマスにビットが立っている,blank_boardを作成

        //左側の処理   *一度に返せる石は6つまでなので「BOARD_SIZE-2」回だけ隣の石を確認する。for外1回 + for内5回の計6回。
        //ToDo forループの展開
        tmp = opponent_board_remove_RightLeft & (player_board << 1);
        for (int i = 1; i < BOARD_SIZE - 2; i++) {
            tmp |= opponent_board_remove_RightLeft & (tmp << 1);
        }
        legal_places = blank_board & (tmp << 1);

        //右側の処理
        tmp = opponent_board_remove_RightLeft & (player_board >>> 1);
        for (int i = 1; i < BOARD_SIZE - 2; i++) {
            tmp |= opponent_board_remove_RightLeft & (tmp >>> 1);
        }
        legal_places |= blank_board & (tmp >>> 1);

        //上側の処理
        tmp = opponent_board_remove_UpDown & (player_board << 8);
        for (int i = 1; i < BOARD_SIZE - 2; i++) {
            tmp |= opponent_board & (tmp << 8);
        }
        legal_places |= blank_board & (tmp << 8);

        //下側の処理
        tmp = opponent_board_remove_UpDown & (player_board >>> 8);
        for (int i = 1; i < BOARD_SIZE - 2; i++) {
            tmp |= opponent_board & (tmp >>> 8);
        }
        legal_places |= blank_board & (tmp >>> 8);

        //右斜め上の処理
        tmp = opponent_board_remove_AllEdge & (player_board << 7);
        for (int i = 1; i < BOARD_SIZE - 2; i++) {
            tmp |= opponent_board_remove_RightLeft & (tmp << 7);
        }
        legal_places |= blank_board & (tmp << 7);

        //左斜め上の処理
        tmp = opponent_board_remove_AllEdge & (player_board << 9);
        for (int i = 1; i < BOARD_SIZE - 2; i++) {
            tmp |= opponent_board_remove_RightLeft & (tmp << 9);
        }
        legal_places |= blank_board & (tmp << 9);

        //右斜め下の処理
        tmp = opponent_board_remove_AllEdge & (player_board >>> 9);
        for (int i = 1; i < BOARD_SIZE - 2; i++) {
            tmp |= opponent_board_remove_RightLeft & (tmp >>> 9);
        }
        legal_places |= blank_board & (tmp >>> 9);

        //左斜め下の処理
        tmp = opponent_board_remove_AllEdge & (player_board >>> 7);
        for (int i = 1; i < BOARD_SIZE - 2; i++) {
            tmp |= opponent_board_remove_RightLeft & (tmp >>> 7);
        }
        legal_places |= blank_board & (tmp >>> 7);

        return legal_places;
    }

    //タップした位置に置いた場合の、盤面変化後の状態をrevで返す
    public long reverse(long player_board, long opponent_board, long touch_board) {
        long rev = 0;
        for (int k = 0; k < 8; k++) {
            long rev_ = 0;
            long mask = transfer(touch_board, k);
            while ((mask != 0 && (mask & opponent_board) != 0)) {
                rev_ |= mask;
                mask = transfer(mask, k);
            }
            if ((mask & player_board) != 0) {
                rev |= rev_;
            }
        }
        return rev;
    }

    //反転箇所を求める関数
    public long transfer(long touch_board, int k) {
        switch (k) {
            case 0: //上
                return (touch_board << 8) & 0xffffffffffffff00L;
            case 1: //右上
                return (touch_board << 7) & 0x7f7f7f7f7f7f7f00L;
            case 2: //右
                return (touch_board >>> 1) & 0x7f7f7f7f7f7f7f7fL;
            case 3: //右下
                return (touch_board >>> 9) & 0x007f7f7f7f7f7f7fL;
            case 4: //下
                return (touch_board >>> 8) & 0x00ffffffffffffffL;
            case 5: //左下
                return (touch_board >>> 7) & 0x00fefefefefefefeL;
            case 6: //左
                return (touch_board << 1) & 0xfefefefefefefefeL;
            case 7: //左上
                return (touch_board << 9) & 0xfefefefefefefe00L;
        }
        return 0;
    }

    //playerとopponentのboardの入れ替え、手番のチェンジ
    public void board_swap_player(long p, long o) {
        final long tmp = p;
        Player_Board = o;
        Opponent_Board = tmp;
        Turn = Turn * -1; //石の描画管理用にturn変数を用意してあるので、それを反転させる。
    }

    //パスをチェックし、自動でパスを行う
    public void pass_check() {
        //自分がおけない、かつ相手がおける時のみパス処理を実行する
        //両者とも置けないときは終了処理をすべきであり、パス処理はすべきでないため。
        long PassCheck_board = Legal_places(Player_Board, Opponent_Board);
        long PassCheck_board2 = Legal_places(Opponent_Board, Player_Board);
        if ((Long.bitCount(PassCheck_board) == 0) && (Long.bitCount(PassCheck_board2) != 0)) {
            board_swap_player(Player_Board, Opponent_Board);
            IsPass[Num_Move_Step] = true;
        }
    }

    //ゲーム終了をチェックし、結果を表示
    public void finish_check() {
        //両手番の打つ箇所がともに0の時に終了なので、以下のようにチェックボードを二つ生成した
        long PassCheck_board = Legal_places(Player_Board, Opponent_Board);
        long PassCheck_board2 = Legal_places(Opponent_Board, Player_Board);
        if ((Long.bitCount(PassCheck_board) == 0) && (Long.bitCount(PassCheck_board2) == 0)) {
            //Board_Viewの呼び出し元であるMainActivityを取得し、MainActivityからResult_Dialogを表示する
            GameMainActivity ma = (GameMainActivity) this.getContext();
            ma.result_Dialog();
            //Num_Move_Step = 0;
        }
    }
}
