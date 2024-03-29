package com.android.nhom35.luyennghe;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.nhom35.R;
import com.android.nhom35.taikhoan.DatabaseAccess;
import com.android.nhom35.taikhoan.User;
import com.android.nhom35.ui.home.Database;

import java.util.ArrayList;

public class ListeningActivity extends AppCompatActivity {
    final  String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    DatabaseAccess DB;
    TextView txtscore,txtquestcount;
    RadioGroup rdgchoices;
    RadioButton btnop1,btnop2,btnop3,btnop4;
    Button btnconfirm;
    Button btnquit;
    ImageView imHA;
    private ArrayList<CauLuyenNghe> cauLuyenNghes;
    private MediaPlayer mediaPlayer;
    private ImageButton ImgBT;

    User user;

    String URL = "https://github.com/Lap2000/songs/raw/main/Bay-Giua-Ngan-Ha-Nam-Cuong.mp3";
    int questioncurrent = 0;
    int questiontrue = 0;
    int answer=0;
    int score=0;
    int idbo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_listening);
        DB = DatabaseAccess.getInstance(getApplicationContext());
        Anhxa();
        LayUser();
        Intent intent=getIntent();
        idbo=intent.getIntExtra("Bo",0);
        cauLuyenNghes = new ArrayList<>();
        AddArrayCLN();

        shownextquestion(questioncurrent,cauLuyenNghes);

        // Create MediaPlayer.
        mediaPlayer=  new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {mp.start();}
        });
        CountDownTimer countDownTimer=new CountDownTimer(3000,300) {
            @Override
            public void onTick(long millisUntilFinished) {
                showanswer();
            }

            @Override
            public void onFinish() {

                btnconfirm.setEnabled(true);
                shownextquestion(questioncurrent,cauLuyenNghes);
            }
        };
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkans();
                questioncurrent++;
                countDownTimer.start();

            }
        });


        // When the video file ready for playback.
        this.ImgBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // http://example.coom/mysong.mp3
                //String mediaURL = MediaPlayerUtils.URL_MEDIA_SAMPLE;
                String mediaURL = URL;
                MediaPlayerUtils.playURLMedia(ListeningActivity.this, mediaPlayer, mediaURL);
                //doStart();
            }
        });

        btnquit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                doStop();
                Intent intent
                        = new Intent(ListeningActivity.this,
                        LuyenNgheActivity.class);
                startActivity(intent);
            }
        });
    }
    public void Anhxa(){
        txtscore= findViewById(R.id.txtscoreLN);
        txtquestcount= findViewById(R.id.txtquestcountLN);
        rdgchoices= findViewById(R.id.radiochoices);
        btnop1=findViewById(R.id.op1);
        btnop2=findViewById(R.id.op2);
        btnop3=findViewById(R.id.op3);
        btnop4=findViewById(R.id.op4);
        btnconfirm=findViewById(R.id.btnconfirmLN);
        btnquit=findViewById(R.id.btnQuitLN);
        imHA=findViewById(R.id.imgHinh);
        ImgBT=findViewById(R.id.ImgBT);
    }

    private void AddArrayCLN(){
        database = Database.initDatabase(ListeningActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM LuyenNghe WHERE ID_Bo = ?",new String[]{String.valueOf(idbo)});
        cauLuyenNghes.clear();

        for (int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            int idbai = cursor.getInt(0);
            int idbo = cursor.getInt(1);
            String A = cursor.getString(2);
            String B = cursor.getString(3);
            String C = cursor.getString(4);
            String D = cursor.getString(5);
            String True = cursor.getString(6);
            byte[] hinh = cursor.getBlob(7);
            String audio = cursor.getString(8);

            cauLuyenNghes.add(new CauLuyenNghe(idbai,idbo,A,B,C,D,True,hinh,audio));
        }
    }

    public void LayUser()
    {
        database = Database.initDatabase(ListeningActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM User WHERE ID_User = ?",new String[]{String.valueOf(DB.iduser)});
        cursor.moveToNext();
        String Iduser = cursor.getString(0);
        String HoTen = cursor.getString(1);
        int Point = cursor.getInt(2);
        String Email = cursor.getString(3);
        String SDT = cursor.getString(4);
        user = new User(Iduser,HoTen,Point,Email,SDT);
    }

    public void shownextquestion(int pos,  ArrayList<CauLuyenNghe> cauLuyenNghes){

        if(pos > 0 ) doStop();
//        database= Database.initDatabase(ListeningActivity.this,DATABASE_NAME);
//        String a=null;
//        Cursor cursor=database.rawQuery("SELECT * FROM LuyenNghe WHERE ID_Bo=?",new String[]{String.valueOf(idbo)});
//        txtquestcount.setText("Question: "+(questioncurrent+1)+"/"+cursor.getCount()+"");
        txtquestcount.setText("Question: "+(questioncurrent+1)+"/"+cauLuyenNghes.size()+"");
        rdgchoices.clearCheck();
        btnop1.setBackground(this.getResources().getDrawable(R.drawable.bgbtn));
        btnop2.setBackground(this.getResources().getDrawable(R.drawable.bgbtn));
        btnop3.setBackground(this.getResources().getDrawable(R.drawable.bgbtn));
        btnop4.setBackground(this.getResources().getDrawable(R.drawable.bgbtn));
//        if(pos==cursor.getCount()){
        if(pos==cauLuyenNghes.size()){
            DB.capnhatdiem(DB.iduser,user.getPoint(),score);
            Intent intent=new Intent(ListeningActivity.this, FinishQuizLSActivity.class);
            intent.putExtra("score",score);
            intent.putExtra("questiontrue", questiontrue);
            intent.putExtra("qcount", pos);
            startActivity(intent);
        }
        else {
            byte[] anh = cauLuyenNghes.get(pos).getHinhanh();
            Bitmap img= BitmapFactory.decodeByteArray(anh,0,anh.length);
            imHA.setImageBitmap(img);

            String URLaudio = cauLuyenNghes.get(pos).getAudio();
            URL = URLaudio;

            answer=Integer.valueOf(cauLuyenNghes.get(pos).getDapanTrue());
            btnop1.setText(cauLuyenNghes.get(pos).getDapanA());
            btnop2.setText(cauLuyenNghes.get(pos).getDapanB());
            btnop3.setText(cauLuyenNghes.get(pos).getDapanC());
            btnop4.setText(cauLuyenNghes.get(pos).getDapanD());
        }
//        for(int i=pos;i<cursor.getCount();i++){
//            cursor.moveToPosition(i);
//            byte[] anh = cursor.getBlob(7);
//            Bitmap img= BitmapFactory.decodeByteArray(anh,0,anh.length);
//            imHA.setImageBitmap(img);
//            String URLaudio = cursor.getString(8);
//            URL = URLaudio;
//            //String questcontent=cursor.getString(8);
//            String op1=cursor.getString(2);
//            String op2=cursor.getString(3);
//            String op3=cursor.getString(4);
//            String op4=cursor.getString(5);
//            String ans=cursor.getString(6);
//            answer=Integer.valueOf(ans);
//            btnop1.setText(op1);
//            btnop2.setText(op2);
//            btnop3.setText(op3);
//            btnop4.setText(op4);
//
//            break;
//        }



    }
    public void checkans(){
        btnconfirm.setEnabled(false);
        if(btnop1.isChecked()){
            if(1==answer) {

                score+=5;
                questiontrue++;
            }
        }
        if(btnop2.isChecked()){
            if(2==answer) {

                score+=5;
                questiontrue++;
            }
        }
        if(btnop3.isChecked()){
            if(3==answer) {

                score+=5;
                questiontrue++;
            }
        }
        if(btnop4.isChecked()){
            if(4==answer) {

                score+=5;
                questiontrue++;
            }
        }

        txtscore.setText("Score: "+score+"");
    }
    public void showanswer(){
        if(1==answer) {
            btnop1.setBackground(this.getResources().getDrawable(R.drawable.button_2));
            btnop2.setBackground(this.getResources().getDrawable(R.drawable.button_1));
            btnop3.setBackground(this.getResources().getDrawable(R.drawable.button_1));
            btnop4.setBackground(this.getResources().getDrawable(R.drawable.button_1));

        }
        if(2==answer) {
            btnop1.setBackground(this.getResources().getDrawable(R.drawable.button_1));
            btnop2.setBackground(this.getResources().getDrawable(R.drawable.button_2));
            btnop3.setBackground(this.getResources().getDrawable(R.drawable.button_1));
            btnop4.setBackground(this.getResources().getDrawable(R.drawable.button_1));

        }
        if(3==answer) {
            btnop1.setBackground(this.getResources().getDrawable(R.drawable.button_1));
            btnop2.setBackground(this.getResources().getDrawable(R.drawable.button_1));
            btnop3.setBackground(this.getResources().getDrawable(R.drawable.button_2));
            btnop4.setBackground(this.getResources().getDrawable(R.drawable.button_1));

        }
        if(4==answer) {
            btnop1.setBackground(this.getResources().getDrawable(R.drawable.button_1));
            btnop2.setBackground(this.getResources().getDrawable(R.drawable.button_1));
            btnop3.setBackground(this.getResources().getDrawable(R.drawable.button_1));
            btnop4.setBackground(this.getResources().getDrawable(R.drawable.button_2));

        }
    }
    private void doStart( )  {
        if(this.mediaPlayer.isPlaying()) {
            //this.mediaPlayer.stop();
            this.mediaPlayer.pause();
            this.mediaPlayer.reset();
        }
        else {this.mediaPlayer.start();}
    }
    private void doStop()  {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.reset();
    }
}
