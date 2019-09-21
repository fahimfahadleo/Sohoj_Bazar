package com.sohojbazar.sohojbazar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.INotificationSideChannel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.InputStream;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class productdetail extends AppCompatActivity {

    ImageView image,add,remove;
    TextView name,price,detail,amount;
    int i=0;
    CircleImageView additemclick;
    String bazartype;
    String usertype,typeofbazar,productname,rpoduct,url,amounttype,productdetail1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetail);
        image=findViewById(R.id.productdetailimage);
        add=findViewById(R.id.productdetailaddimg);
        remove=findViewById(R.id.productdetailremoveimg);
        name=findViewById(R.id.productdetailname);
        price=findViewById(R.id.productdetailprice);
        detail=findViewById(R.id.productdetaildetail);
        amount=findViewById(R.id.productdetailamount);
        additemclick=findViewById(R.id.additemtochart);



        usertype=getIntent().getStringExtra("user-type");
        typeofbazar=getIntent().getStringExtra("bazar-name");
        productname=getIntent().getStringExtra("name");
        rpoduct=getIntent().getStringExtra("price");
        url=getIntent().getStringExtra("url");
        amounttype=getIntent().getStringExtra("amounttype");
        productdetail1=getIntent().getStringExtra("detail");



       final String tag="balsal";
       Log.e(tag,usertype);
        Log.e(tag,typeofbazar);
        Log.e(tag,productname);
        Log.e(tag,rpoduct);
        Log.e(tag,url);
        Log.e(tag,amounttype);
        Log.e(tag,productdetail1);


        Picasso.with(productdetail.this).load(url).into(image);

       // new DownloadImageTask(image).execute(getIntent().getStringExtra("url"));
        name.setText(getIntent().getStringExtra("name"));
        price.setText(getIntent().getStringExtra("price"));
        String amounttwxt = getIntent().getStringExtra("amounttype");
        detail.setText(getIntent().getStringExtra("detail"));
        Log.e("amount",amounttwxt);
        final String [] amounts=amounttwxt.split(Pattern.quote("."));

        amount.setText(amounts[0]);


         String totalprice=Integer.toString(Integer.valueOf(getIntent().getStringExtra("price").split(" ")[1])*Integer.valueOf(amounttwxt.split(" ")[0]));

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(amounts[i+1]!=null){
                        amount.setText(amounts[i+1]);
                        i++;
                    }
                }catch (Exception e){
                    Log.e("array index out of bond",e.toString());
                }

            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(amounts[i-1]!=null){
                        amount.setText(amounts[i-1]);
                        i--;
                    }
                }catch (Exception e){
                    Log.e("array index out of bond",e.toString());
                }

            }
        });

        additemclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    chart chart=new chart(name.getText().toString(),amount.getText().toString(),price.getText().toString(),getIntent().getStringExtra("url"));
                    Log.e("chartitem",chart.returnmap().toString());
                    Toast.makeText(productdetail.this,"আপনার পন্যটি ব্যাগে যোগ হয়েছে।",Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(productdetail.this,productActivity.class);
        intent.putExtra("bazar-name",typeofbazar);
        if(usertype!=null){
            intent.putExtra("user-type","admin");
        }
        startActivity(intent);
    }
}
