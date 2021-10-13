package com.some.studychats;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.some.studychats.FragmentsInstagram.HomeFragment;
import com.some.studychats.FragmentsInstagram.NotificationFragment;
import com.some.studychats.FragmentsInstagram.ProfileFragment;
import com.some.studychats.multi.MultiSelect;

public class MainActivityInstagram extends AppCompatActivity {
    BottomNavigationView bottom_navigation;
    Fragment selectedfragment = null;
    Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_instagram);

        if (Build.VERSION.SDK_INT>=21){
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }

        bottom_navigation = findViewById(R.id.bottom_navigation);
        bottom_navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);


        Bundle intent = getIntent().getExtras();
        if (intent != null){
            String publisher = intent.getString("publisherid");

            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
            editor.putString("profileid", publisher);
            editor.apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ProfileFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.nav_home:
                            selectedfragment = null;
                            selectedfragment = new HomeFragment();
                            break;
                        case R.id.nav_search:
                            selectedfragment = null;
                           selectedfragment = new SearchFragment();
                            break;
                        case R.id.nav_add:
                            selectedfragment = null;
                  //          selectedfragment = new ChatsFragment();
                            startMainActivity();
                   //       startActivity(new Intent(MainActivityInstagram.this,
                     //     MainActivity.class));
                            break;
                        case R.id.nav_heart:
                            selectedfragment = null;
                           selectedfragment = new NotificationFragment();

                            break;
                        case R.id.nav_profile:
                            selectedfragment = null;
                            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                            editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            editor.apply();

                            selectedfragment = new ProfileFragment();
                            break;
                    }
                    if (selectedfragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedfragment).commit();
                    }

                    return true;
                }
            };

    private  void startMainActivity(){
        Intent mainIntent = new Intent(MainActivityInstagram.this,MainActivity.class);
        startActivity(mainIntent);
    }

    private  void startNotification(){
        Intent mainIntent = new Intent(MainActivityInstagram.this, NotificationActivity.class);
        startActivity(mainIntent);

    }

    private  void startHome(){
        Intent mainIntent = new Intent(MainActivityInstagram.this, HomeActivity.class);
        startActivity(mainIntent);

    }
    private  void startSearchActivity(){
        Intent mainIntent = new Intent(MainActivityInstagram.this, SearchActivity.class);
        startActivity(mainIntent);

    }

    private  void startProfileInstagram(){
        Intent mainIntent = new Intent(MainActivityInstagram.this, ProfileActivityInstagram.class);
        startActivity(mainIntent);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(2);
    }
}
