package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.FragmentManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    //WIDGETS
TextView timelefttext,songtitle;
Button play,pause,forward,backward;
SeekBar seekBar;
ImageButton imageButton;

//MEDIA PLAYER
    MediaPlayer mediaPlayer;

    //Handler
    Handler handler = new Handler();

    //Variables
    double starttime =0;
    double endtime = 0;
    double forwardtime = 10000;
    double Backwardtime = 10000;
    static int onetimeonly =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timelefttext = findViewById(R.id.timeLeft_text);
        songtitle = findViewById(R.id.song_title_text);

        play = findViewById(R.id.play_btn);
        pause = findViewById(R.id.pause_btn);
        forward = findViewById(R.id.forward_btn);
        backward = findViewById(R.id.backward_btn);

        seekBar = findViewById(R.id.seekbar);

        imageButton = (ImageButton) findViewById(R.id.imageButton2);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageButton.setVisibility(View.GONE);
                play.setVisibility(View.GONE);
                pause.setVisibility(View.GONE);
                forward.setVisibility(View.GONE);
                backward.setVisibility(View.GONE);
                Fragment fragment = new NotificationFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.container,fragment).commit();
            }
        });

        //Media player

        mediaPlayer = MediaPlayer.create(this,R.raw.swami_vivekanand_biography);
        songtitle.setText(getResources().getIdentifier(
                "swami_vivekanand_biography",
                "raw",
                getPackageName()
        ) );
        seekBar.setClickable(false);

        //ADDING FUNCTIONALITIES FOR THE BUTTONS
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayMusic();

            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp= (int)starttime;
                if ((temp+forwardtime) <= endtime){
                    starttime= starttime+forwardtime;
                    mediaPlayer.seekTo((int) starttime);
                }else {
                    Toast.makeText(MainActivity.this, "Can't Jump forward!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int) starttime;
                if ((temp-Backwardtime) > 0){
                    starttime= starttime-forwardtime;
                    mediaPlayer.seekTo((int) starttime);
                }else {
                    Toast.makeText(MainActivity.this, "Can't Jump Backward!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }






    private void PlayMusic() {
        mediaPlayer.start();

        endtime = mediaPlayer.getDuration();
        starttime= mediaPlayer.getCurrentPosition();

        if (onetimeonly==0) {
            seekBar.setMax((int) endtime);
            onetimeonly = 1;
        }
        timelefttext.setText(String.format(
                "%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) endtime),
                TimeUnit.MILLISECONDS.toSeconds((long) endtime)-
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes
                                ((long) endtime))
        ));
            seekBar.setProgress((int) starttime);
            handler.postDelayed(UpdateSongTime,100);

    }

    //Creating the runnable
    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            starttime = mediaPlayer.getCurrentPosition();
            timelefttext.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) starttime),
                    TimeUnit.MILLISECONDS.toSeconds((long) starttime)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) starttime))
            ));


            seekBar.setProgress((int)starttime);
            handler.postDelayed(this, 100);
        }
    };
}