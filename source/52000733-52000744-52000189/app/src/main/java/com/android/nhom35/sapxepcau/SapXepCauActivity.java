package com.android.nhom35.sapxepcau;

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

public class SapXepCauActivity extends AppCompatActivity {

    final  String DATABASE_NAME = "HocNgonNgu.db";
    SQLiteDatabase database;
    ImageView imgback;


    ArrayList<BoHocTap> boTuVungs;
    BoHocTapAdapter adapter;
    ListView botuvungs;

    int idbo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sapxepcau);
        botuvungs = findViewById(R.id.listviewSXC);
        imgback = findViewById(R.id.imgVBackSXC);
        boTuVungs = new ArrayList<>();
        AddArrayBTV();
        adapter = new BoHocTapAdapter(SapXepCauActivity.this,R.layout.row_sapxepcau,boTuVungs);
        botuvungs.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        botuvungs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idbo = boTuVungs.get(position).getIdBo();
                Intent sxc = new Intent(SapXepCauActivity.this, ArrangeSentencesActivity.class);
                sxc.putExtra("idbo", idbo);
                startActivity(sxc);
            }
        });

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent
                        = new Intent(SapXepCauActivity.this,
                        MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void AddArrayBTV(){
        database = Database.initDatabase(SapXepCauActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM BoCauHoi",null);
        boTuVungs.clear();

        for (int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            int idbo = cursor.getInt(0);
            int  stt = cursor.getInt(1);
            String tenbo = cursor.getString(2);
            boTuVungs.add(new BoHocTap(idbo,stt,tenbo));
        }

    }
}