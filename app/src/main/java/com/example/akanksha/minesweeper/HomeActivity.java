package com.example.akanksha.minesweeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    EditText editText;
    RadioGroup group;
    RadioButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        editText=findViewById(R.id.name);
    }

    public void submit(View v)
    {

        String name=editText.getText().toString();

        Intent intent=new Intent(this,MainActivity.class);
        intent.putExtra("Name",name);

        group=findViewById(R.id.level);

        // get selected radio button from radioGroup
        int selectedId = group.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        button = (RadioButton) findViewById(selectedId);

        //Toast.makeText(MyAndroidAppActivity.this,radioSexButton.getText(), Toast.LENGTH_SHORT).show();
        String level=button.getText().toString();

        if(level.equals("Easy"))
        {
            //int size=5;
            intent.putExtra("Level",8);
        }
        else if(level.equals("Medium"))
        {
            //int size=5;
            intent.putExtra("Level",10);
        }
        else if(level.equals("Difficult"))
        {
            //int size=5;
            intent.putExtra("Level",13);
        }

        Toast.makeText(this,"button",Toast.LENGTH_LONG).show();

         startActivity(intent);

    }


}
