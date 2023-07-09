package com.example.newboardgame;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Board_state extends State{
    public Board_state()throws  Exception{
        this.board=new byte[5][9];
    }

    public Board_state(byte[][] board, byte player) throws Exception {
        this.board = board ;
        this.player = player ;
    }
    @Override
    public boolean oneMove(byte[][] b, int r, int c, byte aPlayer) {
        if (r < 0 || r >= b.length || c < 0 || c >= b[0].length || b[r][c] != 0)//エラーチェック
            return false ;
        else {
            b[r][c] = aPlayer ;
            return true ;
        }
    }

    //指定のboardとplayerからstateを作成して返す。
    //This is a factory method.
    @Override
    public State getState(byte[][] board, byte player) {
        State s = null ;
        try {
            s = new Board_state(board, player) ;
        } catch (Exception ex) {
            ex.printStackTrace() ;
        }
        return s ;
    }



    //指定の場所で指定のplayerが手を打てるかを判定する。
    @Override
    public boolean isValidMove(Move move, byte player) {
        if ( board[move.getRow()][move.getCol()] !=player )
            return true ;
        else return false ;
    }



    //ここから修正
    @Override
    public boolean makeAmove(Move move) {
        int r = move.getRow(), c = move.getCol() ;//row gyou max 5 columm retu max 9


        byte value=player;

        if(board[r][c]==player) return false;//すでにそのマスに自分が売っていた場合



        // 上方向のチェック
        if (r > 0 &&  board[r - 1][c] == value) {
            this.board[r][c]=value;
            return true;
        }

        // 下方向のチェック
        if (r < board.length - 1 && board[r + 1][c] == value) {
            this.board[r][c]=value;
            return true;
        }

        // 左方向のチェック
        if (c > 0 && board[r][c - 1] == value) {
            this.board[r][c]=value;
            return true;
        }

        // 右方向のチェック
        if (c < board[r].length - 1 && board[r][c + 1] == value) {
            this.board[r][c]=value;
            return true;
        }


        return false;

    }

    @Override
    public Object isTerminal(byte player) {
        int i, j ;
        int count1=0,count2=0;
        byte value=player;



        if(player==1){
            if(board[2][8]==value)return winValue;

            if(board[2][0]==-value)return lossValue;
        }
        else{
            if(board[2][0]==value)return winValue;

            if(board[2][8]==-value)return lossValue;

        }
        if(n_turn>=15){
        for(i=0;i< board.length;i++){
            for(j=0;j< board[0].length;j++){

                if(board[i][j]==value) count1=count1+1;//タイルの計算
                if(board[i][j]==-value) count2=count2+1;//タイルの計算
            }

        }
        if(count1>count2) return winValue;
        if(count1<count2) return lossValue;
        }

        //ここ修正
        //check if no more move is possible
        /*for (i = 0 ; i < board.length ; i++) {
            for (j = 0; j < board[0].length ; j++)
                if (board[i][j] == 0) break ;
            if (j < board[0].length) break ;
        }
        if (i == board.length) return 0.0 ;*/

        return null ;
    }
    //


    @Override
    public double evaluate(byte player) {
        //終局の場合，その報酬を返す
        Object result = isTerminal(player) ;
        if (result != null)
            return (double)result ;

        //終局ではない場合，近似値を返す
        byte r = player ;
        int[] counter = new int[2] ;
        for (int k = 0 ; k < 2 ; k++) {
            int i , j , numberOfzero ;

            //check hopeful rows for the opponent of r
            for (i = 0 ; i < board.length ; i++) {
                numberOfzero = 0 ;
                for (j = 0; j < board[0].length ; j++) {
                    if (board[i][j] == r) break;
                    else if (board[i][j] == 0) numberOfzero++ ;
                }
                if (j == board[0].length) {
                    if (numberOfzero == 1)
                        counter[k] += 9 ;
                    else
                        counter[k]++ ;
                }
            }

            //check hopeful columns for the opponent of r
            for (i = 0 ; i < board[0].length ; i++) {
                numberOfzero = 0 ;
                for (j = 0; j < board.length ; j++) {
                    if ( board[j][i] == r) break ;
                    else if (board[j][i] == 0) numberOfzero++ ;
                }
                if (j == board.length) {
                    if (numberOfzero == 1)
                        counter[k] += 9 ;
                    else
                        counter[k]++ ;
                }
            }

            //check if the main diagonal is hopeful for the opponent of r
            numberOfzero = 0 ;
            for (j = 0 ; j < board.length ; j++) {
                if (board[j][j] == r) break;
                else if (board[j][j] == 0) numberOfzero++ ;
            }
            if (j == board.length) {
                if (numberOfzero == 1)
                    counter[k] += 9 ;
                else
                    counter[k]++ ;
            }

            //check if the secondary diagonal is hopeful for the opponent of r
            numberOfzero = 0 ;
            for (j = 0 ; j < board.length ; j++) {
                if (board[j][board.length - 1 - j] == r) break;
                else if (board[j][board.length - 1 - j] == 0) numberOfzero++ ;
            }
            if (j == board.length) {
                if (numberOfzero == 1)
                    counter[k] += 9 ;
                else
                    counter[k]++ ;
            }

            r = (byte)-player ;
        }

        return (counter[1] - counter[0]) / 10.0 ;
    }

    @Override
    public int getDepth() {
        return 2 ;
    }


}
