package com.example.newboardgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AI extends View {
    private final static String BR = //改行
            System.getProperty("line.separator");
    private Button aiBtn ;
    private TextView tv ;
    private State state ;
    private byte aiPlayer ;
    private volatile boolean gameOver;//値を瞬時反映するために volatileにしてある

    public AI(Context context, State state) {
        super(context, null);
        this.state = state ;
    }

    public void setButtons(Button aiBtn) {
        this.aiBtn = aiBtn ;
        aiBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                aiMove() ;
            }
        });
    }

    public void setAIplayer(byte aiPlayer) {
        this.aiPlayer = aiPlayer ;
    }

    public void setTextView(TextView view) {
        this.tv = view ;
    }

    //次のメソッドは本当は不要
    //aiの手を１つ打つメソッド
    private void aiMove() {
        //Assumption: the player in "state" is the AI player
        List<State> actions = state.getNextStates() ;
        if (actions.isEmpty()) {
            Toast ts = Toast.makeText(getContext(),
                    "このゲームではここに到達するはずはない!", Toast.LENGTH_SHORT);
            ts.setGravity(Gravity.CENTER, 0, 0);
            ts.show();
            return ;
        } else {
            state = new MinMax(aiPlayer).minmaxDecision(state,state.getDepth()) ;
            //new NegaMax(aiPlayer).negamaxDecision(state,state.getDepth()) ;
            //new AlphaBeta(aiPlayer).alphaBetaDecision(state,state.getDepth()) ;
            //new NegaAlphaBeta(aiPlayer).negaAlphaBetaDecision(state,state.getDepth()) ;
            //try { state = MCTStree.mctsDecision(state, "robust", -1, 1000) ;}
            //catch (Exception ex) { ex.printStackTrace() ; }

            Object result2 = state.isTerminal(aiPlayer) ;
            if (result2 != null) { //勝負決定
                String message ;
                double d = (double)result2 ;
                if (d != 0.0 && d != State.lossValue && d != State.winValue ) {
                    tv.setTextColor(Color.RED); // 先手への情報の表示色を赤にする.
                    message = "終局盤面の報酬が期待の値になっていません．" + BR ;
                } else {
                    if (d == State.winValue)
                        message = "あなたの負けです．" + BR ;
                    else if (d == 0.0)
                        message = "引き分けです．" ;
                    else
                        message = "あなたの勝ちです．" + BR ;
                }
                tv.append(message) ;//messageをゲームフレームに表示
                gameOver = true ;
                aiBtn.setEnabled(false) ;
                invalidate() ;
                return ;
            } else {
                //tv.append("あなたの番です．" + BR) ;
                Toast ts = Toast.makeText(getContext(), "あなたの番です!", Toast.LENGTH_SHORT);
                ts.setGravity(Gravity.CENTER, 0, 0);
                ts.show();
                invalidate() ;
            }
        }

        aiBtn.setEnabled(false) ; //next is the human player's turn
    }
}
