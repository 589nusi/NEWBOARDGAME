package com.example.newboardgame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import android.content.pm.ActivityInfo;
import android.graphics.Color;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private final static String BR = //改行
            System.getProperty("line.separator");
    private  volatile  boolean gameover;

    private Board_state state ;

    private byte aiPlayer ;

    private TextView tv;


    private TextView[] T_gettile=new TextView[2];
    private int[] id_get={R.id.get_p1,R.id.get_p2};

    private TextView[] T_avapoint=new TextView[2];
    private int[] id_ava={R.id.ava_p1,R.id.ava_p2};

    private int[] p_gettile={1,1};

    private int getpoint=0;
    private byte x=1;
    private byte y=-1;
    Random rand =  new Random() ;
    int n_turn;
    private Button aiBtn ;





    private TextView[] gameboard=new TextView[45];//textview接続する
    private int[] tile={R.id.cell0,R.id.cell1,R.id.cell2,R.id.cell3,R.id.cell4,R.id.cell5,R.id.cell6,R.id.cell7,R.id.cell8,
            R.id.cell9,R.id.cell10,R.id.cell11,R.id.cell12,R.id.cell13,R.id.cell14,R.id.cell15,R.id.cell16,R.id.cell17,
            R.id.cell18,R.id.cell19,R.id.cell20,R.id.cell21,R.id.cell22,R.id.cell23,R.id.cell24,R.id.cell25,R.id.cell26,
            R.id.cell27,R.id.cell28,R.id.cell29,R.id.cell30,R.id.cell31,R.id.cell32,R.id.cell33,R.id.cell34,R.id.cell35,
            R.id.cell36,R.id.cell37,R.id.cell38,R.id.cell39,R.id.cell40,R.id.cell41,R.id.cell42,R.id.cell43,R.id.cell44,
    };//textviewxcellとの接続

    public Board_state getInitialState(byte player) {
        Board_state s = null ;
        try {
            byte[][] b = new byte[5][9] ;
            s = new Board_state(b, player) ;
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Failed to create the initial state.",
                    Toast.LENGTH_LONG).show();
        }
        return s ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        tv=findViewById(R.id.Turn_name);

        for (int i = 0; i < 45; i++) {
            gameboard[i] = findViewById(tile[i]);
        }//textview接続
        for (int i=0; i<2;i++) {
            T_avapoint[i] = findViewById(id_ava[i]);
            T_gettile[i] = findViewById(id_get[i]);
            T_gettile[i].setText(String.valueOf(p_gettile[i]));
        }
        aiBtn = findViewById(R.id.AI_btn) ;//ボタンへの参照を得る


        boolean isFirst = rand.nextBoolean() ;

        getpoint = (rand.nextInt(2)+1)*2 ;//使えるポイント

        if (isFirst) {
            tv.setText("貴方が先手（緑色）です．\n") ;
            state = getInitialState(State.Player1);
            //aiBtn.setEnabled(false) ;
            gameboard[18].setBackgroundColor(Color.GREEN);
            gameboard[26].setBackgroundColor(Color.YELLOW);
        } else {
            tv.setText("貴方が後手（黄色）です．\n");
            state = getInitialState(State.Player1);
            //aiBtn.setEnabled(true) ;
            gameboard[18].setBackgroundColor(Color.GREEN);
            gameboard[26].setBackgroundColor(Color.YELLOW);
        }
        if(state.getPlayer()==1){
            T_avapoint[0].setText(String.valueOf(getpoint));
            T_avapoint[1].setText(String.valueOf(0));

        }else{
            T_avapoint[0].setText(String.valueOf(0));
            T_avapoint[1].setText(String.valueOf(getpoint));
        }

        state.setBoard(2,0,x);
        state.setBoard(2,8,y);

    }
    @SuppressLint("ResourceType")
    public boolean onClickEvent(View view){
        if(gameover) return false;



        String str = (String)view.getTag();
        int tag = Integer.parseInt(str);
        int r=tag/9;
        int c=tag%9;
        byte player= state.getPlayer();
        byte[][] board = state.getBoard() ;//更新前のboard
        byte board_n=board[r][c];//打ちたい場所にどのプレイヤーがいるか
        String toastMessage = "トースト";




        if(board_n!=0&&getpoint-2<0) return false;


        //このコードの位置を変える
        if(state.makeAmove(new Move(r,c))==false){
            return false;
        }//移動できるか判定
        Toast toast = Toast.makeText(this, String.valueOf(-board_n), Toast.LENGTH_SHORT);
        toast.show();



        if(board_n!=0){

                if(player==1) {
                    gameboard[tag].setBackgroundColor(Color.GREEN);
                    p_gettile[0]=p_gettile[0]+1;
                    T_gettile[0].setText(String.valueOf(p_gettile[0]));
                }
                if(player==-1){
                    gameboard[tag].setBackgroundColor(Color.YELLOW);
                    p_gettile[1]=p_gettile[1]+1;
                    T_gettile[1].setText(String.valueOf(p_gettile[1]));
                }
                getpoint=getpoint-2;

        }
        else{
            if(player==1) {
                gameboard[tag].setBackgroundColor(Color.GREEN);
                p_gettile[0]=p_gettile[0]+1;
                T_gettile[0].setText(String.valueOf(p_gettile[0]));
            }
            if(player==-1){
                gameboard[tag].setBackgroundColor(Color.YELLOW);
                p_gettile[1]=p_gettile[1]+1;
                T_gettile[1].setText(String.valueOf(p_gettile[1]));
            }
            getpoint=getpoint-1;
        }




        aiPlayer=player;//player引き渡し
        Object result = state.isTerminal(aiPlayer) ; // 今の手を，ボード記憶用の配列に記憶し，
        if (result != null) { //勝負決定
            double d = (double)result ;
            String message ;
            if (d != 0.0 && d != State.lossValue && d != State.winValue ) {
                tv.setTextColor(Color.RED); // 先手への情報の表示色を赤にする.
                message = "終局盤面の報酬が期待の値になっていません．" + BR ;
            } else {

                if (d == State.winValue)
                    message = "あなたの負けです．" + BR ;
                else if (d == 0.0)
                    message = "引き分けです．" + BR ;
                else
                    message = "あなたの勝ちです．" + BR ;
            }
            gameover = true ;
            tv.setText(message); ;//messageをゲームフレームに表示
            aiBtn.setEnabled(false) ;//aibtnを押せるようにする

        }


        if(getpoint<=0) {
            state.setPlayer(state.getOpponent());
            getpoint = (rand.nextInt(2)+1)*2 ;//使えるポイント
            n_turn=state.getTurn();
            tv.setText("turn is"+String.valueOf(n_turn));
        }


        if(state.getPlayer()==1){
            T_avapoint[0].setText(String.valueOf(getpoint));
            T_avapoint[1].setText(String.valueOf(0));

        }else{
            T_avapoint[0].setText(String.valueOf(0));
            T_avapoint[1].setText(String.valueOf(getpoint));
        }

        return false;
    }


    @Override
    public void onBackPressed() {
        ActivityCompat.finishAffinity(this) ;//TTTActivityを終了
        android.os.Process.killProcess(android.os.Process.myPid());
    }

/*
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.yourxmlinlayout-land);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.yourxmlinlayoutfolder);
        }
    }
    */


}