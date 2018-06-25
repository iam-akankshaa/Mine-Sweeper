package com.example.akanksha.minesweeper;

import android.content.Intent;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
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


    public static final int INCOMPLETE = 0;
    public static final int WON = 1;
    public  static final int LOST=-1;
    ImageButton img;
    public int tsize;
    boolean mineset;
    long timeleftinmillisec = 600000;
    CountDownTimer timer;
    boolean timerunning;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootLayout = findViewById(R.id.root);

        setupBoard();
    }

    public void setupBoard() {

        mineset=false;
        currentstatus=INCOMPLETE;
        Intent intent = getIntent();
        String name = intent.getStringExtra("Name");
        int level = intent.getIntExtra("Level", 0);

        if (level == 8)
            tsize = 30;
        else if (level == 10)
            tsize = 20;
        else if (level == 13)
            tsize = 11;

        size = level;

        board = new MSbutton[size][size];
        rows = new ArrayList<>();

        rootLayout.removeAllViews();

        LinearLayout l = new LinearLayout(this);
        LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);

        l.setLayoutParams(Params);
        rootLayout.addView(l);

        TextView text = new TextView(this);
        LinearLayout.LayoutParams Param = new LinearLayout.LayoutParams(0, 120,1);
        text.setLayoutParams(Param);
        text.setText(name);
        text.setTextSize(20);
        text.setTextColor(this.getResources().getColor(R.color.colorPrimaryDark));
        l.addView(text);

        // btn =new Button(this);
        img = new ImageButton(this);
        LinearLayout.LayoutParams Parameter = new LinearLayout.LayoutParams(0, 150,1);
        img.setLayoutParams(Parameter);
        l.addView(img);
        img.setBackground(this.getResources().getDrawable(R.drawable.smilee));
        //img.setImageResource(R.drawable.smile);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Do something
                stoptimer();
                setupBoard();
            }
        });


        textView = new TextView(this);
        LinearLayout.LayoutParams Par = new LinearLayout.LayoutParams(0, 120,1);

        textView.setLayoutParams(Par);
        textView.setGravity(Gravity.END);
        l.addView(textView);
        textView.setTextSize(30);


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
                layoutParams.rightMargin = -8;
                layoutParams.leftMargin = -8;
                layoutParams.topMargin = -12;
                layoutParams.bottomMargin = -12;
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




    public void startstop() {

        if (timerunning)
            stoptimer();
        else
            starttimer();
    }

    public void starttimer() {

        timer = new CountDownTimer(timeleftinmillisec, 1000) {

            @Override
            public void onTick(long l) {

                timeleftinmillisec = l;
                updateTimer();
            }

            public void onFinish() {


            }


        }.start();


        timerunning = true;
    }




   public  void stoptimer() {

        timer.cancel();
        timeleftinmillisec=600000;
        timerunning=false;


   }

   public void updateTimer()
   {
       int min= (int) timeleftinmillisec/60000;
       int sec=(int) timeleftinmillisec%60000/1000;

       String timeleft;
       timeleft=""+min;
       timeleft+=":";
       if(sec<10)
           timeleft+="O";
       timeleft+=sec;
       textView.setText(timeleft);


   }
    public void onClick(View v) {

         if(currentstatus==INCOMPLETE) {

             MSbutton button = (MSbutton) v;
             int r = button.getRow();
             int c = button.getCol();

             if (!mineset) {
                 mineset = true;
                 setmines(r, c);
                 startstop();

                 //revealall();

             }

             //button.setOnLongClickListener(new View.OnLongClickListener();
             button.reveal=true;
             button.setEnabled(false);
             uncover1(r, c);
             checkGameStatus();
         }

    }


        @Override
        public boolean onLongClick(View v) {

           if(currentstatus == INCOMPLETE) {
               Toast.makeText(MainActivity.this, "Long Clicked", Toast.LENGTH_LONG).show();
               MSbutton button = (MSbutton) v;
               //button.setText("F");
               button.setBackground(this.getResources().getDrawable(R.drawable.flag));
               button.flag=true;

               return true;
           }
            return false;
        }


    public void setmines(int currrow, int currcol)
    {
        Random rand = new Random();
        int minerow, minecol;
        int noofmines=size+4;
        int count=0;

         //Setting Of Mines
        for (int j = 0; j < 8; j++) {
            int a = currrow + one[j];
            int b = currcol + two[j];
            if (a >= 0 && a < size && b >= 0 && b < size) {
                MSbutton butn = board[a][b];
                butn.can_mine = false;

            }

        }

          while(count<noofmines) {

            minerow = rand.nextInt(size - 1);
            minecol = rand.nextInt(size - 1);

            if (minerow != currrow || minecol != currcol) {

                        MSbutton button = board[minerow][minecol];
                        if (!button.hasMine() && button.can_mine == true) {

                            count++;
                            button.setValue(-1);
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
                //b.setText("*");
                b.setBackground(this.getResources().getDrawable(R.drawable.mine3));
                // b.setTextColor(this.getResources().getColor(R.color.black));
                allMines();
                currentstatus=LOST;
                Toast.makeText(this,"OOps!!! Game over",Toast.LENGTH_LONG).show();

            }
            else if(b.value > 0)
            {
                //b.reveal=true;
                if(b.value == 1)
                b.setTextColor(this.getResources().getColor(R.color.blue));
                else if(b.value == 2)
                    b.setTextColor(this.getResources().getColor(R.color.green));
                else if(b.value == 3)
                    b.setTextColor(this.getResources().getColor(R.color.red));
                else if(b.value == 4)
                    b.setTextColor(this.getResources().getColor(R.color.purple));
                else if(b.value == 5)
                    b.setTextColor(this.getResources().getColor(R.color.orange));

                b.setBackgroundColor(this.getResources().getColor(R.color.light));
                b.setText(String.valueOf(b.value));
                b.setTextSize(tsize);
                //b.setEnabled(false);
            }

            else if(b.value==0){
                //b.reveal=true;
                //b.setEnabled(false);
                b.setText("");
                b.setBackgroundColor(this.getResources().getColor(R.color.light));

                Toast.makeText(this,"Blank Button Clicked",Toast.LENGTH_SHORT).show();
                for(int i=0;i<8;i++)
                {
                    if(rowclick+one[i]>=0 && rowclick+one[i]<size && colclick+two[i]>=0 && colclick+two[i]<size)
                    {
                        MSbutton button =board[rowclick+one[i]][colclick+two[i]];

                        if(button.value != -1 && button.reveal==false && button.flag==false)

                            uncover2(rowclick+one[i],colclick+two[i]);

                    }
                }


            }

      }

        public void uncover2 (int rowclick, int colclick) {

            MSbutton b = board[rowclick][colclick];

               if(b.reveal == false && b.flag == false) {
                   if (b.value > 0) {
                       b.reveal = true;
                       b.setEnabled(false);
                       b.setText(String.valueOf(b.value));
                       b.setTextSize(tsize);
                       b.setBackgroundColor(this.getResources().getColor(R.color.light));

                      if (b.value == 1)
                           b.setTextColor(this.getResources().getColor(R.color.blue));
                       else if (b.value == 2)
                           b.setTextColor(this.getResources().getColor(R.color.green));
                      else if(b.value == 3)
                          b.setTextColor(this.getResources().getColor(R.color.red));
                      else if(b.value == 4)
                          b.setTextColor(this.getResources().getColor(R.color.purple));
                      else if(b.value == 5)
                          b.setTextColor(this.getResources().getColor(R.color.orange));


                       return;

                   }
                   // when value =0

                   else {
                       b.reveal=true;
                       b.setEnabled(false);
                       b.setBackgroundColor(this.getResources().getColor(R.color.light));
                       b.setText("");

                       for (int i = 0; i < 8; i++) {
                           if (rowclick + one[i] >= 0 && rowclick + one[i] < size && colclick + two[i] >= 0 && colclick + two[i] < size) {
                               MSbutton button = board[rowclick + one[i]][colclick + two[i]];

                               if (button.value != -1 && button.reveal == false && button.flag == false)

                                   uncover2(rowclick + one[i], colclick + two[i]);

                           }
                       }
                   }

               }

        }

        public void checkGameStatus()
        {
            if(textView.getText().toString().equals("0:00")){

                Toast.makeText(this,"Time Over!!!",Toast.LENGTH_LONG).show();
                stoptimer();
                disableall();
                return;

            }

            else if(currentstatus == LOST)
            {
                stoptimer();
                return;

            }


            MSbutton b;
            for(int i=0;i<size;i++) {
                for (int j = 0; j < size; j++) {
                    b = board[i][j];
                    if (b.value != -1 || b.flag == false) {
                        if (b.reveal == false)
                            return;

                    }

                }
            }

            currentstatus=WON;
            stoptimer();
            Toast.makeText(this, "Yahoo U Won!!", Toast.LENGTH_LONG).show();
            return;

        }

        public void disableall() {

            MSbutton b;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {

                    b = board[i][j];
                    if(b.isEnabled())
                        b.setEnabled(false);


                }
            }
        }


        public void allMines()
            {
                img.setBackground(this.getResources().getDrawable(R.drawable.sad));
                MSbutton butn;
                for(int i=0;i<size;i++)
                {
                    for(int j=0;j<size;j++)
                    {
                        butn=board[i][j];
                        if(butn.isEnabled())
                        butn.setEnabled(false);
                        if(butn.value == -1)
                        {
                            butn.setBackground(this.getResources().getDrawable(R.drawable.mine3));
                        }

                    }


                }

            }

  /*  private Handler timer = new Handler();
    private int secondsPassed = 0;
    public void startTimer()
    {
        if (secondsPassed == 0)
        {
            timer.removeCallbacks(updateTimeElasped);
            // tell timer to run call back after 1 second
            timer.postDelayed(updateTimeElasped, 1000);
        }
    }

    public void stopTimer()
    {
        // disable call backs
        timer.removeCallbacks(updateTimeElasped);
    }

    // timer call back when timer is ticked
    private Runnable updateTimeElasped = new Runnable()
    {
        public void run()
        {

            long currentMilliseconds = System.currentTimeMillis();
            ++secondsPassed;
            //TextView et = (TextView) l.findViewById(R.id.10);
            textView.setText(Integer.toString(secondsPassed));

            // add notification
            timer.postAtTime(this, currentMilliseconds);
            // notify to call back after 1 seconds
            // basically to remain in the timer loop
            timer.postDelayed(updateTimeElasped, 1000);
        }
    }; */






}

