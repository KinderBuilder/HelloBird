package com.example.hellobird;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.hellobird.paintView;

public class board extends AppCompatActivity {
    public Button optionsButton;
    public paintView paint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);

        TextView testMessage = (TextView)findViewById(R.id.textView2);

        Button optionB_BOARD = (Button) findViewById(R.id.optionButtonBoard);
        Button clearB_BOARD = (Button) findViewById(R.id.clearButtonBoard);
        Button saveB_BOARD = (Button) findViewById(R.id.saveButtonBoard);
        Button loadB_BOARD = (Button) findViewById(R.id.loadButtonBoard);
        Button createB_BOARD = (Button) findViewById(R.id.createButtonBoard);

        paint = findViewById(R.id.testPaint);
        paint.invalidate();

        optionB_BOARD.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                startActivity(new Intent(board.this, options.class));
            }
        });
        loadB_BOARD.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                final AlertDialog.Builder builder = new AlertDialog.Builder(board.this);
                View myView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
                final EditText txt_inputText = (EditText)myView.findViewById(R.id.txt_input);
                Button btn_cancel = (Button)myView.findViewById(R.id.btn_cancel);
                Button btn_okay = (Button)myView.findViewById(R.id.btn_okay);
                builder.setView(myView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                btn_okay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        testMessage.setText(txt_inputText.getText().toString());
                        paint.loadCanvas(board.this, txt_inputText.getText().toString());
                        paint.invalidate();
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                builder.setMessage("Hoi").setTitle("This is a title");
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context,"Hoi",Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        clearB_BOARD.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                paint.clearCan();
                paint.invalidate();
            }
        });
        saveB_BOARD.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //Context Way to get information about the app (what it is doing)
                //board = activity (a type of context); .this is how the class refers to itself. without it, then it would be similar to making a new object of board
                paint.saveCanvas(board.this);
            }
        });
        createB_BOARD.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                paint.createCanvas(board.this);
            }
        });
        paint.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent motionEvent){
                //Sets action to what the user did (ex: touch down, move, ect)
                int action = motionEvent.getAction();
                paintView viewP = (paintView)v;
                if(viewP.needsInit()) viewP.init();
                //Uses switch statements to decide what to do based on the action
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        viewP.drawDot((int)motionEvent.getX(),(int)motionEvent.getY());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        viewP.drawDot((int)motionEvent.getX(),(int)motionEvent.getY());
                        break;
                    default:
                        break;
                }
                //Tells android to do it over as it has expired (hence invalidate)
                paint.invalidate();
                return true;
            }
        });
    }
    protected void onStart() {
        super.onStart();
        paint.invalidate();
        paint.invalidate();
    }
}

