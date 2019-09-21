package com.sohojbazar.sohojbazar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePage extends AppCompatActivity {

    NavigationView nav;
    DrawerLayout drawerLayout;
    ImageView menu,bazarlist;
    FirebaseAuth firebase;

    FirebaseUser firebaseUser;
    ImageView munch,mangsho,kacha,cosmetics,fol,mudi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        nav=findViewById(R.id.navigation_view);
        menu=findViewById(R.id.menu);
        drawerLayout=findViewById(R.id.drawerlayout);
        bazarlist=findViewById(R.id.bazarlist);

        firebase= FirebaseAuth.getInstance();
        firebaseUser=firebase.getCurrentUser();
        munch=findViewById(R.id.munchbazar);
        mangsho=findViewById(R.id.mangshobazar);
        kacha=findViewById(R.id.kachabazar);
        cosmetics=findViewById(R.id.cosmeticsbazar);
        fol=findViewById(R.id.folbazar);
        mudi=findViewById(R.id.mudibazar);
        bazarlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this,ChartActivity.class));
            }
        });
        final Intent intent=new Intent(this,productActivity.class);
        String userid=firebaseUser.getUid();
        if(userid.equals("ZEB1QaaFsmclx9cvN3HykIdtxM73")){
            intent.putExtra("user-type","admin");
        }

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        munch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("bazar-name","মাছ বাজার");
                startActivity(intent);
            }
        });
        mangsho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("bazar-name","মাংস বাজার");
                startActivity(intent);
            }
        });
        fol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("bazar-name","ফল বাজার");
                startActivity(intent);
            }
        });
        mudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("bazar-name","মুদি বাজার");
                startActivity(intent);
            }
        });
        cosmetics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("bazar-name","কসমেটিক্স বাজার");
                startActivity(intent);
            }
        });
        kacha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("bazar-name","কাচা বাজার");
                startActivity(intent);
            }
        });



        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.chart:
                        Toast.makeText(HomePage.this,"chart selected",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.contractus:
                        Toast.makeText(HomePage.this,"contructs selected",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.share:
                        Toast.makeText(HomePage.this,"share selected",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.addreference:
                        Toast.makeText(HomePage.this,"add reference selected",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.logout:
                        Toast.makeText(HomePage.this,"logout selected",Toast.LENGTH_LONG).show();
                        firebase.signOut();
                        startActivity(new Intent(HomePage.this, MainActivity.class));
                        break;

                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
}
