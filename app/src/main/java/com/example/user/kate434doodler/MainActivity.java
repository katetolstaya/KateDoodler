package com.example.user.kate434doodler;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends ActionBarActivity {
    private DrawingView drawView;
    private ImageButton currPaint;
    private int currOpacity;
    private int currSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawView = (DrawingView)findViewById(R.id.drawing);

        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        currSize = 2;
        final int max_size = 3;
        final int  min_size = 1;
        final int step_size = 1;

        SeekBar seekbar = (SeekBar) findViewById(R.id.seekBar2);
        seekbar.setMax( (max_size - min_size) / step_size );
        seekbar.setProgress((currSize-min_size)/step_size);

        seekbar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener()
                {
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) { }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress,
                                                  boolean fromUser)
                    {
                        double value = min_size + (progress * step_size);
                        currSize = (int) value;
                        drawView.setBrushSize(currSize);
                    }
                }
        );

        currOpacity = 3;

        final int max_op = 3;
        final int  min_op = 1;
        final int step_op = 1;

        seekbar = (SeekBar) findViewById(R.id.seekBar3);
        seekbar.setMax( (max_op - min_op) / step_op);
        seekbar.setProgress((currOpacity-min_op)/step_op);

        seekbar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener()
                {
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) { }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress,
                                                  boolean fromUser)
                    {
                        double value = min_op + (progress * step_op);
                        currOpacity = (int) value;
                        drawView.setOpacity(currOpacity);
                    }
                }
        );


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void paintClicked(View view){
        //use chosen color
        if(view!=currPaint){
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
//update color
            drawView.setColor(color);

            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint=(ImageButton)view;

        }

    }

    public void saveClicked(View view){
        Context context = getApplicationContext();
        final Bitmap bitmap = drawView.getCanvasBitmap();
        final File folder;
        File file;

            folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            final Date now = new Date();
            file = new File(folder, (now.getTime() / 1000) + ".png");
        try {
            file.createNewFile();

            final FileOutputStream ostream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
            ostream.close();

            // Tell the media scanner about the new file so that it is immediately available to the user.
            MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null, null);
        } catch (IOException e) {
            // Insert error handling here :)
        }

        // show toast/brief pop-up  that shows file name/location
        CharSequence text = file.toString() + " saved!";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void newClicked(View view){
        drawView.resetBitmap();
    }

    public void eraseClicked(View view){

       // ImageButton imgView = (ImageButton)view;
       // String color = view.getTag().toString();
//update color
        drawView.eraser();

        //imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
        currPaint=(ImageButton)view;
    }
}
