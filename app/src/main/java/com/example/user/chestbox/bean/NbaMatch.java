package com.example.user.chestbox.bean;

import android.graphics.Bitmap;

/**
 * Created by User on 2017/11/24.
 */

public class NbaMatch {
    private String player1;
    private Bitmap player1logobig;
    private String score;
    private Bitmap player2logobig;
    private String player2;
    private String status;
    private String time;

    public NbaMatch(){

    }

    public NbaMatch(String player1,Bitmap player1logobig,String score,Bitmap player2logobig,String player2,String status){
        this.player1 = player1;
        this.player1logobig = player1logobig;
        this.score = score;
        this.player2logobig = player2logobig;
        this.player2 = player2;
        this.status = status;
        this.time = time;

    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public Bitmap getPlayer1logobig() {
        return player1logobig;
    }

    public void setPlayer1logobig(Bitmap player1logobig) {
        this.player1logobig = player1logobig;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public Bitmap getPlayer2logobig() {
        return player2logobig;
    }

    public void setPlayer2logobig(Bitmap player2logobig) {
        this.player2logobig = player2logobig;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.status = time;
    }
}
