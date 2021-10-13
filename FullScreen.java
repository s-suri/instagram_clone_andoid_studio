package com.some.studychats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class FullScreen extends Activity {

    ViewPager viewPager;
    String[] images;
    int postion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        if (savedInstanceState ==null){


            Intent i = getIntent();
            images = i.getStringArrayExtra("visit_usr_id");
           postion = i.getIntExtra("POSITION",0);

        }
        viewPager = findViewById(R.id.viewpager);

        FullSizeAdaper fullSizeAdaper = new FullSizeAdaper(this,images);
        viewPager.setAdapter(fullSizeAdaper);
        viewPager.setCurrentItem(postion,true);

    }
}
