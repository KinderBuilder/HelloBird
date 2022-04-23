package com.example.hellobird;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Canvas;

import android.os.Build;

import android.util.AttributeSet;
import android.util.Log;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;


public class paintView extends View{

    private Paint brush;
    private Canvas can;
    private int[][] grid;
    private int cellSize;
    private ArrayList<String> files;
    private int currentFile = 1;
    private String contents[];

    //Static means that it is a part of the class, not the object (aka only one color out of the many paintView objects)
    public static int color = Color.BLACK;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public paintView(Context context, AttributeSet attribute) {
        //Calls the super constructor

        //Purpose
        //Function
        super(context, attribute);
        brush = new Paint();
        cellSize = 10;

        files = loadFileNames(context);

        File directory = new File(context.getFilesDir().toString());
        contents = directory.list();
    }
    //Auto generates the grid
    public void init(){
        int rows = getHeight()/cellSize;
        int collums = getWidth()/cellSize;
        grid = new int[rows][collums];
        for(int i = 0; i < rows; i++){
            for(int x = 0; x < collums; x++){
                grid[i][x] = Color.WHITE;
            }
        }
    }

    public boolean needsInit(){
        return grid == null;
    }

    //Draws the canvas/drawable area on the screen
    public void onDraw(Canvas canvas){
        can = canvas;
        Log.d("whenRun", "onDraw has ran but idk when since this is a preprogramed message. AUTOCORRECT");
        //Access a function in super, not the constructor
        super.onDraw(canvas);
        canvas.drawPaint(brush);
        brush.setColor(Color.WHITE);
        //How it draws the canvas, the first two are the coords of the first dot, the last twoare the coords of the last two dots. The two dots are used to make the rectangle
        if(!needsInit()) {
            for (int i = 0; i < grid.length; i++) {
                for (int x = 0; x < grid[i].length; x++) {
                    brush.setColor(grid[i][x]);
                    canvas.drawRect(x * cellSize, i * cellSize, (x + 1) * cellSize, (i + 1) * cellSize, brush);
                }
            }
        }
        //canvas.drawRect(getLeft()+10.0f, getTop()+10.0f,getRight()-10.0f, getBottom()-10.0f, brush);
    }

    //Draws a dot on the screen
    public void drawDot(int x, int y){
        //Converts the coordinates of the x and y from the screen to the canvas
        //x -= getLeft();
        //y -= getTop();
        int row = y/cellSize;
        int collum = x/cellSize;
        //For out or bounds
        if(row>0&&row<grid.length&&collum>0&&collum<grid[grid.length-1].length){
            Log.d("results", row + ":" + collum);
            Log.d("cords", x + ":" + y);
            grid[row][collum] = color;
        }
    }

    //Draws a line on the screen
    public void drawLine(int startX, int startY, int endX, int endY){
        //The variables for calculating the cells between the start and end of the line, not the pixels
        int startRow = startY/cellSize;
        int startCollum = startX/cellSize;
        int endRow = endY/cellSize;
        int endCollum = endX/cellSize;

        //The variables on where the loop should start & end
        int topRow = Math.min(startRow,endRow);
        int bottomRow = Math.max(startRow,endRow);
        int topCollum = Math.min(startCollum,endCollum);
        int bottomCollum = Math.max(startCollum,endCollum);


    }

    public void clearCan(){
        //Note: For each loop, useful for arrays, Downside: no index access (ex: x, y, hotdogs, ect)
        for (int[] ints : grid) {
            Arrays.fill(ints, Color.WHITE);
        }
    }

    public void saveCanvas(Context context){
        try {
            //Creates the writer
            //Located in data/com.example.hellobird/files
            File directory = new File(context.getFilesDir().toString());
            contents = directory.list();
            //Prints out all the contents in files
            for(int i = 0; i < contents.length; i++){
                Log.d("Contents Of File", contents[i]);
            }
            //Saves the current file
            OutputStreamWriter writer = new OutputStreamWriter(context.openFileOutput("DrawingTest"+currentFile+".txt",Context.MODE_PRIVATE));
            Log.d("FileInfo","Directory of DrawingTest" + directory.toString());
            for(int y = 0; y < grid.length; y++){
                for(int x = 0; x <grid[y].length; x++){
                    writer.write(""+grid[y][x]);
                    if(x<grid[y].length-1){
                        writer.write(",");
                    }
                }
                writer.write("\n");
            }
            Log.d("File","Saving Now... Look for errors...idk");
            Log.d("FileInfo",context.getFilesDir().getAbsolutePath());
            //Signals that it's job is finished & allows other things to manipulate the file
            writer.close();
        }catch (IOException e){
            Log.e("Grid Saving Error",e.toString());
        }
    }
    public void createCanvas(Context context){
        //Saves the current state of the current canvas
        saveCanvas(context);
        //Creates a blank canvas
        clearCan();
        //Saves it as a new canvas
        try {
            //For explanation, look at saveCanvas
            OutputStreamWriter writer = new OutputStreamWriter(context.openFileOutput("DrawingTest" + contents.length + ".txt", Context.MODE_PRIVATE));
            for (int y = 0; y < grid.length; y++) {
                for (int x = 0; x < grid[y].length; x++) {
                    writer.write("" + grid[y][x]);
                    if (x < grid[y].length - 1) {
                        writer.write(",");
                    }
                }
                writer.write("\n");
            }
            writer.close();
            currentFile = contents.length;
            invalidate();
        }catch (IOException e){
            Log.e("Grid Saving Error",e.toString());
        }
    }
    public void loadCanvas(Context context, String fileName){
        try{
            /*
            Previous old code, original purpose was to allow the user to type in what save file they wanted
            int startIndex;
            int endIndex;

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Choose A Drawing To Load");
            builder.setTitle("Load Drawing");
            //Pops up with options to choose, requires an Array of Strings
            builder.setItems(files.toArray(new String[0]), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //To Do: Add here
                }
            });*/
            //Raw string of the file location
            for(int i = 0; i < contents.length; i++) {
                if ((fileName + ".txt").equals(contents[i])) {
                    InputStream inputStream = context.openFileInput(fileName + ".txt");
                    if (inputStream == null) {
                        Log.e("Grid Loading Error", "Cannot find file");
                        return;
                    }
                    //Reads the raw data
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    //Refines/Parces the data
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    //
                    for (int y = 0; y < grid.length; y++) {
                        String storage = reader.readLine();
                        String data[] = storage.split(",");
                        for (int x = 0; x < grid[y].length; x++) {
                            grid[y][x] = Integer.parseInt(data[x]);
                        }
                    }
                    break;
                } else if (i + 1 == contents.length) {
                    Toast toast = Toast.makeText(context, "Error: File Not Found", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }catch(IOException e){
            Log.e("Grid Loading Error", e.toString());
        }
    }

    public ArrayList<String> loadFileNames(Context context){
        Log.d("PaintView", "Now running");
        try{
            ArrayList<String> fileNames = new ArrayList<>();
            InputStream inputStream = context.openFileInput("storageFile.txt");
            Log.d("PaintView", "Half");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            while(true){
                String currentLine = reader.readLine();
                if(currentLine==null)
                {
                    break;
                }
                fileNames.add(currentLine);
            }
            Log.d("PaintView", "Done");
            return fileNames;
        }catch(FileNotFoundException o){
            createStoargeFile(context);
            Log.e("File Load Error", o.toString());
            return new ArrayList<>();
            //return loadFileNames(context);
        }catch(IOException e){
            Log.e("File Load Error", e.toString());
            return null;
        }
    }

    private void createStoargeFile(Context context){
        try{
            OutputStreamWriter writer = new OutputStreamWriter(context.openFileOutput("storageFile.txt",Context.MODE_PRIVATE));
            writer.write("\n");
            writer.close();
            writer.flush();
        }catch (IOException e){
            Log.e("File Load Error", e.toString());
        }
    }

    //Fix a warning that isn't really helpful (I think)
    @Override
    public boolean performClick(){return true;}
}
