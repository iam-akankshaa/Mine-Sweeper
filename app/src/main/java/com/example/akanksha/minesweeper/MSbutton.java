package com.example.akanksha.minesweeper;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;

public class MSbutton extends AppCompatButton{


    public MSbutton(Context context) {
        super(context);

    }

    private int row;
    private int col;
    public int value=0;
    public boolean reveal=false;
    public boolean flag=false;
    public boolean can_mine=true;



    public void setPlayer(int i,int j)
    {
        this.row=i;
        this.col=j;

       /* if(MainActivity.Player_0 == player)
        {
            setText("0");
        }
        if(MainActivity.Player_x == player)
        {
            setText("X");
        }*/


    }

    public void setValue(int val)
    {
        this.value=val;
    }

    public int  getRow()
    {
        return this.row;
    }


    public int getCol()
    {
        return this.col;
    }

    public boolean hasMine()
    {
        if(this.value == -1)
            return true;
        else
            return false;
    }




}
