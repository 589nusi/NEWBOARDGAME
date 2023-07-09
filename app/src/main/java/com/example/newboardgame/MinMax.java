package com.example.newboardgame;

import java.util.Collection;

public class MinMax {
    private byte player ;//盤面の良さをこのplayerの立場から評価したい

    public MinMax(byte player) {
        this.player = player ;
    }

    //指定盤面から指定深さの先読みを行った結果の
    // （this.playerから見た時の）最善手を返すメソッド.
    //基本的に、stateが終端ではなく、state.player == this.player
    public State minmaxDecision(State state, int depth) {
        double max_value = Double.NEGATIVE_INFINITY ;

        //打てる手をすべて列挙して，それぞれを打った結果の盤面のリストを得る
        Collection<State> states = state.getNextStates();

        State best_s = null ;
        for (State s : states) {
            //s.printState() ;
            double value_s = minValue(s, depth - 1) ;
            //System.out.println(">>" + value_s) ;
            if (value_s > max_value) {
                max_value = value_s ;
                best_s = s ;//よりよい盤面を更新
            }
        }
        //System.out.println("+++++++++++++depth: " + depth + "  Value: " + value) ;
        return best_s ;
    }

    //指定盤面から指定深さの先読みを行った結果の
    // （this.playerから見た時の）最善手の値を返すメソッド
    private double maxValue(State state, int depth) {//depth:残りの先読み手数
        Object v = state.isTerminal(this.player) ;
        if( v != null){ //if state is terminal
            //System.out.println("terminal level: " + (double)v) ;
            return (double)v ;
        }

        if (depth == 0) {
            return state.evaluate(this.player) ;
        }

        double max_value = Double.NEGATIVE_INFINITY ;
        Collection<State> states = state.getNextStates() ;
        for (State s : states) {
            //s.printState() ;
            double value_s = minValue(s, depth - 1) ;
            //System.out.println(">>" + value_s) ;
            if (value_s > max_value) {
                max_value = value_s ;
            }
        }
        //System.out.println("***********depth; " + depth + "  Value: " + value) ;
        return max_value ;
    }

    //指定盤面から指定深さの先読みを行った結果の
    // （this.playerから見た時の）再悪手の値を返すメソッド
    private double minValue(State state, int depth) {
        Object v = state.isTerminal(this.player) ;
        if( v != null){ //if state is terminal
            //System.out.println("terminal level: " + (double)v) ;
            return (double)v ;
        }

        if (depth == 0) {
            return state.evaluate(this.player) ;
        }

        double min_value = Double.POSITIVE_INFINITY ;
        Collection<State> states = state.getNextStates() ;
        for (State s : states) {
            //s.printState() ;
            double value_s = maxValue(s, depth - 1) ;
            //System.out.println(">>" + value_s) ;
            if (value_s < min_value) {
                min_value = value_s ;
            }
        }
        //System.out.println("***********depth; " + depth + "  Value: " + value) ;
        return min_value ;
    }
}
