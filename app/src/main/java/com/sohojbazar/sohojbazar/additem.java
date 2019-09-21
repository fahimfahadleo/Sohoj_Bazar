package com.sohojbazar.sohojbazar;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class additem extends AppCompatActivity {
    ImageView newproduct, addproduct;
    EditText name, englishname, detail, price, amounttype;
    Uri imageuri;
    StorageReference sr;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);
        newproduct = findViewById(R.id.addimageofnewproduct);
        addproduct = findViewById(R.id.addelementtodb);
        name = findViewById(R.id.nameofnewproduct);
        englishname = findViewById(R.id.nameofnewproducteng);
        detail = findViewById(R.id.detailofnewproduct);
        price = findViewById(R.id.priceofnewproduct);
        amounttype = findViewById(R.id.amounttypeofnewproduct);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        sr = FirebaseStorage.getInstance().getReference("Product_Images");


        newproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i, 1);
            }
        });

        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadfile();
            }
        });
    }

    String getExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    void uploadfile() {


        final StorageReference ref = sr.child(name.getText() + "." + getExtension(imageuri));


        ref.putFile(imageuri)
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setProgress(((int) progress));
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.e("url", uri.toString());


                                String url = uri.toString();
                                String productname = name.getText().toString();
                                String productenglishname = englishname.getText().toString();
                                String productdetails = detail.getText().toString();
                                String productprice = price.getText().toString();
                                String productamountty = amounttype.getText().toString();

                                dataupload dataupload = new dataupload(url, productname, productenglishname, productdetails, productprice, productamountty);

                                FirebaseDatabase.getInstance().getReference().child(getIntent().getStringExtra("bazar-type")).child(productname).setValue(dataupload);
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(additem.this, "New Product Has Been Added.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageuri = data.getData();
            newproduct.setImageURI(imageuri);
        }
    }

    class dataupload {
        String imageurl, name, englishname, detail, price, amounttype;

        public dataupload() {

        }

        public dataupload(String imageurl, String name, String englishname, String detail, String price, String amounttype) {
            this.imageurl = imageurl;
            this.name = name;
            this.englishname = englishname;
            this.detail = detail;
            this.price = price;
            this.amounttype = amounttype;
        }

        public String getImageurl() {
            return imageurl;
        }

        public String getName() {
            return name;
        }

        public String getEnglishname() {
            return englishname;
        }

        public String getDetail() {
            return detail;
        }

        public String getPrice() {
            return price;
        }

        public String getAmounttype() {
            return amounttype;
        }
    }

    @Override
    public void onBackPressed() {

        Intent i=new Intent(additem.this,productActivity.class);
        i.putExtra("bazar-name",getIntent().getStringExtra("bazar-name"));
        if(getIntent().hasExtra("user-type")){
            i.putExtra("user-type","admin");
        }
        startActivity(i);
        super.onBackPressed();
    }
}
