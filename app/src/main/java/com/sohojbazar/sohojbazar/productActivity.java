package com.sohojbazar.sohojbazar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

public class productActivity extends AppCompatActivity {

    FloatingActionButton fab,edit;
    ImageView menu, productlist;
    TextView title;
    DatabaseReference databaseReference;
    LinearLayout mylayout;
    LayoutInflater inflater,inflater2;
    ProgressBar progressBar;
    LinearLayout addproduct;
    NavigationView nuv;
    DrawerLayout productdrawerlayout;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        fab = findViewById(R.id.additembutton);
        title = findViewById(R.id.nameofbazar);
        mylayout=findViewById(R.id.productlinearlayout);
        progressBar=findViewById(R.id.productloadbar);
        progressBar.setVisibility(View.VISIBLE);
        addproduct=findViewById(R.id.addelement);
        productlist=findViewById(R.id.bazarlist);
        edit=findViewById(R.id.fabbuttonedit);
        menu=findViewById(R.id.menu);
        nuv=findViewById(R.id.navigation_view);
        productdrawerlayout=findViewById(R.id.productdrawerlayout);
        fab.hide();
        edit.hide();
        databaseReference = FirebaseDatabase.getInstance().getReference(getIntent().getStringExtra("bazar-name"));
        inflater=(LayoutInflater)productActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater2=(LayoutInflater)productActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        productlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(productActivity.this,ChartActivity.class));
            }
        });


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!productdrawerlayout.isDrawerOpen(GravityCompat.START)){
                    productdrawerlayout.openDrawer(GravityCompat.START);
                }
            }
        });

        HashMap<String,JSONObject> products=new chart().returnmap();
        if(products!=null){
            Log.e("log",products.toString());
        }
        if(!products.equals("{}")){
            for(String s:products.keySet()){
                JSONObject jsonObject=products.get(s);
                View Vi=inflater2.inflate(R.layout.items,null,false);
                CircleImageView productimg=Vi.findViewById(R.id.circularview);

                Log.e("jsonobject",jsonObject.toString());


                try {
                    Picasso.with(productActivity.this).load(jsonObject.getString("url")).into(productimg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                addproduct.addView(Vi);
            }
        }
        nuv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.chart:
                        Toast.makeText(productActivity.this,"chart selected",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.contractus:
                        Toast.makeText(productActivity.this,"contructs selected",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.share:
                        Toast.makeText(productActivity.this,"share selected",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.addreference:
                        Toast.makeText(productActivity.this,"add reference selected",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.logout:
                        Toast.makeText(productActivity.this,"logout selected",Toast.LENGTH_LONG).show();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(productActivity.this, MainActivity.class));
                        break;

                }
                return true;
            }
        });



        databaseReference
                .addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final HashMap<String, JSONObject> dataobject=new HashMap<>();





                    for(DataSnapshot ds : dataSnapshot.getChildren()) {

                        String s=ds.getKey();
                        Log.e("ds",s);

                        final String pname=dataSnapshot.child(s).child("name").getValue().toString();
                        String pengname=dataSnapshot.child(s).child("englishname").getValue().toString();
                        final String pimgurl=dataSnapshot.child(s).child("imageurl").getValue().toString();
                        final String pprice=dataSnapshot.child(s).child("price").getValue().toString();
                        final String pamounttype=dataSnapshot.child(s).child("amounttype").getValue().toString();
                        final String pdetail=dataSnapshot.child(s).child("detail").getValue().toString();

                        Log.e("product ","  pname   "+pname+"  penglishname   "+pengname+"  pimgurl   "+pimgurl+"   pprice   "+pprice+"   pamounttype   "+pamounttype+"   pdetail   "+pdetail);

                        View vi=inflater.inflate(R.layout.product,null,false);
                        CircleImageView productimage=vi.findViewById(R.id.productimage);
                        LinearLayout layout=vi.findViewById(R.id.productlayout);
                        final TextView nameofproduct=vi.findViewById(R.id.productname);
                        final TextView productprice=vi.findViewById(R.id.productprice);
                        Log.e("executed","done");


                        Picasso.with(productActivity.this).load(pimgurl).into(productimage);

                       // new DownloadImageTask(productimage).execute(pimgurl);
                        nameofproduct.setText(pname);
                        productprice.setText("Price: "+pprice);

                        mylayout.addView(vi);

                        JSONObject jsonObject=new JSONObject();
                        try {
                            jsonObject.put("url",pimgurl);
                            jsonObject.put("detail",pdetail);
                            jsonObject.put("amount",pamounttype);
                            jsonObject.put("price",pprice);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        dataobject.put(pname,jsonObject);

                        productimage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i=new Intent(productActivity.this,productdetail.class);
                                if(getIntent().hasExtra("user-type")){
                                    i.putExtra("user-type","admin");
                                }
                                i.putExtra("name",nameofproduct.getText());
                                i.putExtra("price",productprice.getText());
                                i.putExtra("bazar-name",getIntent().getStringExtra("bazar-name"));
                                try {
                                    i.putExtra("amounttype",dataobject.get(nameofproduct.getText().toString()).get("amount").toString());
                                    i.putExtra("url",dataobject.get(nameofproduct.getText().toString()).get("url").toString());
                                    i.putExtra("detail",dataobject.get(nameofproduct.getText().toString()).get("detail").toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                startActivity(i);
                            }
                        });

                        layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i=new Intent(productActivity.this,productdetail.class);
                                i.putExtra("name",nameofproduct.getText());
                                i.putExtra("price",productprice.getText());
                                i.putExtra("bazar-name",getIntent().getStringExtra("bazar-name"));
                                if(getIntent().hasExtra("user-type")){
                                    i.putExtra("user-type","admin");
                                }
                                try {
                                    i.putExtra("amounttype",dataobject.get(nameofproduct.getText().toString()).get("amount").toString());
                                    i.putExtra("url",dataobject.get(nameofproduct.getText().toString()).get("url").toString());
                                    i.putExtra("detail",dataobject.get(nameofproduct.getText().toString()).get("detail").toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                startActivity(i);
                            }
                        });

                        nameofproduct.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i=new Intent(productActivity.this,productdetail.class);
                                i.putExtra("name",nameofproduct.getText());
                                i.putExtra("price",productprice.getText());
                                i.putExtra("bazar-name",getIntent().getStringExtra("bazar-name"));
                                if(getIntent().hasExtra("user-type")){
                                    i.putExtra("user-type","admin");
                                }
                                try {
                                    i.putExtra("amounttype",dataobject.get(nameofproduct.getText().toString()).get("amount").toString());
                                    i.putExtra("url",dataobject.get(nameofproduct.getText().toString()).get("url").toString());
                                    i.putExtra("detail",dataobject.get(nameofproduct.getText().toString()).get("detail").toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                startActivity(i);
                            }
                        });
                        productprice.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i=new Intent(productActivity.this,productdetail.class);
                                i.putExtra("name",nameofproduct.getText());
                                i.putExtra("price",productprice.getText());
                                i.putExtra("bazar-name",getIntent().getStringExtra("bazar-name"));
                                if(getIntent().hasExtra("user-type")){
                                    i.putExtra("user-type","admin");
                                }
                                try {
                                    i.putExtra("amounttype",dataobject.get(nameofproduct.getText().toString()).get("amount").toString());
                                    i.putExtra("url",dataobject.get(nameofproduct.getText().toString()).get("url").toString());
                                    i.putExtra("detail",dataobject.get(nameofproduct.getText().toString()).get("detail").toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                startActivity(i);
                            }
                        });

                    }
                    progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(productActivity.this,"Something Went Wrong!",Toast.LENGTH_LONG).show();
            }
        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        title.setText(getIntent().getStringExtra("bazar-name"));
        if(getIntent().hasExtra("user-type")){
            if (getIntent().getStringExtra("user-type").equals("admin")) {
                fab.show();
                edit.show();
            }
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(productActivity.this, additem.class);
                intent.putExtra("bazar-name", title.getText());


                if(getIntent().hasExtra("user-type")){
                    intent.putExtra("user-type","admin");
                }
                startActivity(intent);
            }
        });
    }


    @Override
    public void onBackPressed() {
        if(productdrawerlayout.isDrawerOpen(GravityCompat.START)){
            productdrawerlayout.closeDrawer(GravityCompat.START);
        }else {
          startActivity(new Intent(productActivity.this,HomePage.class));
        }

    }
}
