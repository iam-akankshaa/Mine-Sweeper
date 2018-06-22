package com.example.akanksha.minesweeper;

import android.content.Intent;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener {

    LinearLayout rootLayout;
    public int size;
    public int currentstatus;

    public MSbutton[][] board;
    public ArrayList<LinearLayout> rows;

    int[] one = {-1, -1, -1, 0, 0, 1, 1, 1};
    int[] two = {-1, 0, 1, -1, 1, -1, 0, 1};
    boolean mineset = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootLayout = findViewById(R.id.root);

        setupBoard();
    }

    public void setupBoard() {

        Intent intent=getIntent();
        String name=intent.getStringExtra("Name");
        int level=intent.getIntExtra("Level",0);

        size=level;

        board = new MSbutton[size][size];
        rows = new ArrayList<>();

        rootLayout.removeAllViews();

        LinearLayout l=new LinearLayout(this);
        LinearLayout.LayoutParams Params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1);

        l.setLayoutParams(Params);
        rootLayout.addView(l);

        TextView text=new TextView(this);
        LinearLayout.LayoutParams Param=new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT,3);
        text.setLayoutParams(Param);
        text.setText(name);
        text.setTextSize(30);
        text.setTextColor(this.getResources().getColor(R.color.colorPrimaryDark));
        l.addView(text);

        //Button b =new Button(this);
        ImageButton img = new ImageButton(this);
        LinearLayout.LayoutParams Parameter=new LinearLayout.LayoutParams(30,ViewGroup.LayoutParams.MATCH_PARENT,1);
        img.setLayoutParams(Parameter);
        l.addView(img);
        img.setImageResource(R.drawable.smile);
        img.setOnClickListener(this);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"Home Button",Toast.LENGTH_LONG).show();
                setupBoard();
            }
        });

        for (int i = 0; i < size; i++) {
            LinearLayout linearLayout = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);

            linearLayout.setLayoutParams(layoutParams);

            rootLayout.addView(linearLayout);
            rows.add(linearLayout);

        }


        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                MSbutton b = new MSbutton(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                b.setLayoutParams(layoutParams);

                b.setOnClickListener(this);
                b.setOnLongClickListener(this);

                LinearLayout row = rows.get(i);

                row.addView(b);
                board[i][j] = b;
                b.setPlayer(i, j);


            }
        }


    }


    public void onClick(View v) {


        Toast.makeText(this,"Button Clicked",Toast.LENGTH_SHORT).show();

        MSbutton button = (MSbutton) v;
        int r = button.getRow();
        int c = button.getCol();

        if (!mineset) {
            mineset = true;
            setmines(r, c);

            //revealall();

        }

        //button.setOnLongClickListener(new View.OnLongClickListener() {



            uncover1(r, c);


    }


        @Override
        public boolean onLongClick(View v) {

           if(currentstatus == INCOMPLETE) {
               Toast.makeText(MainActivity.this, "Long Clicked", Toast.LENGTH_LONG).show();
               MSbutton button = (MSbutton) v;
               button.setText("F");
               button.flag=true;
               button.setEnabled(false);

               return true;
           }
            return false;
        }


    public void setmines(int currrow, int currcol)

    {

        Random rand = new Random();
        int minerow, minecol;
        int noofmines=size+2;
        int count=0;

         //Setting Of Mines
        while(count<noofmines) {

            minerow = rand.nextInt(size - 1);
            minecol = rand.nextInt(size - 1);

            if (minerow != currrow || minecol != currcol) {

                for (int j = 0; j < 8; j++) {
                    int a = currrow + one[j];
                    int b = currcol + two[j];
                    if (a != minerow && b != minecol) {

                        MSbutton button = board[minerow][minecol];
                        if (!button.hasMine()) {

                            count++;
                            button.setValue(-1);
                        }


                    }
                }

            }
        }

        Toast.makeText(this, "Mines are set", Toast.LENGTH_LONG).show();
        //Iterate Over Neighbours

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                MSbutton button = board[i][j];

                if(button.value == -1) {

                    for (int k = 0; k < 8; k++) {

                        int a = i + one[k];
                        int b = j + two[k];

                        if (a >= 0 && a < size && b >= 0 && b < size) {
                            MSbutton but = board[a][b];

                            if (!but.hasMine()) {
                                ++but.value;

                            }

                        }

                    }


                }
            }

        }

    }


      /* public boolean hasmine(int i,int j)
        {
            if (board[i][j]==-1)
            {
                return true;
            }

            else

                return false;
        }*/

    public void revealall()
    {
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                MSbutton button =board[i][j];
                button.setText(String.valueOf(button.value));
            }
        }



    }

      public void uncover1 ( int rowclick, int colclick)
      {
            MSbutton b = board[rowclick][colclick];

            if (b.value == -1)
            {
                b.setText("M");
                b.setBackgroundColor(this.getResources().getColor(R.color.black));
                Toast.makeText(this,"Game over",Toast.LENGTH_LONG).show();
            }
            else if(b.value > 0)
            {
                b.reveal=true;
                if(b.value == 1)
                b.setBackgroundColor(this.getResources().getColor(R.color.colorAccent));
                else if(b.value == 2)
                    b.setBackgroundColor(this.getResources().getColor(R.color.blue));
                else if(b.value == 3)
                    b.setBackgroundColor(this.getResources().getColor(R.color.purple));
                b.setText(String.valueOf(b.value));
            }

            else if(b.value==0){

                b.setBackgroundColor(this.getResources().getColor(R.color.green));
                Toast.makeText(this,"Blank Button Clicked",Toast.LENGTH_SHORT).show();
                for(int i=0;i<8;i++)
                {
                    if(rowclick+one[i]>=0 && rowclick+one[i]<size && colclick+two[i]>=0 && colclick+two[i]<size)
                    {
                        MSbutton button =board[rowclick+one[i]][colclick+two[i]];

                        if(button.value != -1 && button.reveal==false)

                            uncover2(rowclick+one[i],colclick+two[i]);

                    }
                }


            }

      }

        public void uncover2 (int rowclick, int colclick) {

            MSbutton b = board[rowclick][colclick];


                if (b.value > 0 && b.reveal == false) {
                    b.reveal = true;
                    if(b.value == 1)
                        b.setBackgroundColor(this.getResources().getColor(R.color.colorAccent));
                    else if(b.value == 2)
                        b.setBackgroundColor(this.getResources().getColor(R.color.blue));
                    else if(b.value == 3)
                        b.setBackgroundColor(this.getResources().getColor(R.color.purple));
                    b.setText(String.valueOf(b.value));
                    return;

                }
                // when value =0

                else
                {
                    for(int i=0;i<8;i++)
                    {
                        if(rowclick+one[i]>=0 && rowclick+one[i]<size && colclick+two[i]>=0 && colclick+two[i]<size)
                        {
                            MSbutton button =board[rowclick+one[i]][colclick+two[i]];

                            if(button.value != -1 && button.reveal==false)

                                uncover3(rowclick+one[i],colclick+two[i]);

                        }
                    }
                }

        }


        public void uncover3(int rowclick,int colclick)
        {
            MSbutton b = board[rowclick][colclick];


            if (b.value > 0 && b.reveal == false) {
                b.reveal = true;
                if(b.value == 1)
                    b.setBackgroundColor(this.getResources().getColor(R.color.colorAccent));
                else if(b.value == 2)
                    b.setBackgroundColor(this.getResources().getColor(R.color.blue));
                else if(b.value == 3)
                    b.setBackgroundColor(this.getResources().getColor(R.color.purple));
                b.setText(String.valueOf(b.value));
                return;

            }
            // when value =0

            else
            {
                for(int i=0;i<8;i++)
                {
                    if(rowclick+one[i]>=0 && rowclick+one[i]<size && colclick+two[i]>=0 && colclick+two[i]<size)
                    {
                        MSbutton button =board[rowclick+one[i]][colclick+two[i]];

                        if(button.value != -1 && button.reveal==false)

                            uncover4(rowclick+one[i],colclick+two[i]);

                    }
                }
            }

        }

        public void uncover4(int rowclick,int colclick)
        {
            MSbutton b = board[rowclick][colclick];


            if (b.value > 0 && b.reveal == false) {
                b.reveal = true;
                if(b.value == 1)
                    b.setBackgroundColor(this.getResources().getColor(R.color.colorAccent));
                else if(b.value == 2)
                    b.setBackgroundColor(this.getResources().getColor(R.color.blue));
                else if(b.value == 3)
                    b.setBackgroundColor(this.getResources().getColor(R.color.purple));
                b.setText(String.valueOf(b.value));
                return;

            }
        }


}

