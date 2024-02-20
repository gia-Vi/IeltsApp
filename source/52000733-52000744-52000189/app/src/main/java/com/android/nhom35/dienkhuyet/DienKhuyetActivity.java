package com.android.nhom35.dienkhuyet;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.nhom35.MainActivity;
import com.android.nhom35.R;
import com.android.nhom35.bohoctap.BoHocTap;
import com.android.nhom35.bohoctap.BoHocTapAdapter;
import com.android.nhom35.ui.home.Database;

import java.util.ArrayList;

public class DienKhuyetActivity extends AppCompatActivity {

    ListView listView;
    ImageView imgback;
    ArrayList<BoHocTap> boHocTapArrayList;
    BoHocTapAdapter boHocTapAdapter;
    final  String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    int idbocauhoi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dien_khuyet);

        listView= findViewById(R.id.lvdienkhuyet);
        imgback = findViewById(R.id.imgVBackDK);
        boHocTapArrayList =new ArrayList<>();
        AddArrayBTN();
        boHocTapAdapter =new BoHocTapAdapter(DienKhuyetActivity.this,R.layout.row_dienkhuyet, boHocTapArrayList);
        listView.setAdapter(boHocTapAdapter);
        boHocTapAdapter.notifyDataSetChanged();
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent
                        = new Intent(DienKhuyetActivity.this,
                        MainActivity.class);
                startActivity(intent);
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                database= Database.initDatabase(DienKhuyetActivity.this,DATABASE_NAME);
                String a=null;
                Cursor cursor=database.rawQuery("SELECT * FROM BoCauHoi",null);
                for(int i=position;i<cursor.getCount();i++){
                    cursor.moveToPosition(i);
                    int idbo=cursor.getInt(0);
                    int stt=cursor.getInt(1);
                    String tenbo=cursor.getString(2);
                    a=tenbo;
                    idbocauhoi=idbo;
                    break;
                }
                Intent quiz= new Intent(DienKhuyetActivity.this, FillBlanksActivity.class);
                quiz.putExtra("BoDK",idbocauhoi);

                startActivity(quiz);
            }
        });
    }
    private void AddArrayBTN(){
        database= Database.initDatabase(DienKhuyetActivity.this,DATABASE_NAME);
        Cursor cursor=database.rawQuery("SELECT * FROM BoCauHoi",null);
        boHocTapArrayList.clear();
        for (int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            int idbo = cursor.getInt(0);
            int  stt = cursor.getInt(1);
            String tenbo = cursor.getString(2);
            boHocTapArrayList.add(new BoHocTap(idbo,stt,tenbo));

        }
    }
}