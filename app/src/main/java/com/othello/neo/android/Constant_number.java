package com.othello.neo.android;

/**
 * Created by Tomoyuki on 2017/04/23.
 */

//変数クラス
//定数(final)→全て大文字
//グローバル変数→単語の頭のみ大文字
public class Constant_number {
    // privateコンストラクタでインスタンス生成を抑止
    private Constant_number() {
    }

    //描画関連の定数
    public static final int BOARD_LINE_STORKE_WIDTH = 1; //盤面の線太さ
    public static final int CIRCLE_RADIUS_PARAM = 2; //石の半径の調整用パラメーター

    //その他定数
    public static final int BOARD_SIZE = 8;
    public static final int BLACK_TURN = 1; // Turn変数用の定数  White = -1 とみなして処理を行う
    public static final int REGISTERATION_MODE = 1; //Mode用の定数
    public static final int CHALLENGE_MODE = -1;

    public static float Square_Length = 0; //一マス(正方形)の辺の長さ
    public static int Turn = BLACK_TURN; //このturn変数を基準に石の描画を管理
    public static int Mode = REGISTERATION_MODE; //今が棋譜登録モードなのか、あるいは暗記チャレンジモードなのかを保存する変数

    //戻る用の配列
    //ToDo 高速化やメモリの観点から改良が必要か?
    public static long Rev_For_Undo[] = new long[65]; //戻る用のrevをここに保存する //ToDo ここの「65」は適切か？ 65にした意図は両端を0で挟んで処理をしやすくするため
    public static long Touch_Board_For_Undo[] = new long[65]; //戻る用のtouch_boardをここに保存する
    public static int Touch_BitFlag[] = new int [65]; //棋譜出力用のタッチした位置を格納した配列
    public static boolean IsPass[] = new boolean[65]; //パスしたら、その手数のインデックスにtrueを代入

    public static int Num_Move_Step = 1; //今何手目かをここに保存する

    public static long Player_Board = 0x0000000000000000L;   //黒
    public static long Opponent_Board = 0x0000000000000000L;   //白
}