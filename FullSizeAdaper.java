package com.some.studychats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


public  class FullSizeAdaper extends PagerAdapter {

    Context context;
    String[] images;
    LayoutInflater inflater;


    public  FullSizeAdaper(Context context,String[] images){
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.length;
    }

   @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view ==object;

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v= inflater.inflate(R.layout.full_item,null);
        ImageView imageView = (ImageView)v.findViewById(R.id.img);



        ViewPager vp = (ViewPager)container;
        vp.addView(v,0);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {


        ViewPager viewPager = (ViewPager)container;
        View v = (View)object;
        viewPager.removeView(v);

    }
}
