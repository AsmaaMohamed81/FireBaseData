package com.asmaa.m.firebasedata.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.asmaa.m.firebasedata.EventListener.Event;
import com.asmaa.m.firebasedata.R;
import com.asmaa.m.firebasedata.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Event{
private ActivityMainBinding binding;
private final int IMG_REQ= 1;
private Bitmap  bitmap;
private byte [] Imagebytes;
private DatabaseReference dref ;
private StorageReference sRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        binding.setEvent(this);
        dref = FirebaseDatabase.getInstance().getReference();
        sRef = FirebaseStorage.getInstance().getReference();


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.add_item:
                Add();
                break;
            case R.id.btnSelectImage:
                selectImage();
                break;
            case R.id.view_item:
                Intent intent = new Intent(MainActivity.this,
                        Viewr.class);
                startActivity(intent);
                break;
        }
    }

    private void selectImage() {



        Intent intent1=new Intent(Intent.ACTION_GET_CONTENT);
        intent1.setType("image/*");
        startActivityForResult(intent1.createChooser(intent1,"selsct"),IMG_REQ);

    }

    private void Add() {
        final String title = binding.titleEdittext.getText().toString();
        final String content = binding.descriptionEdittext.getText().toString();

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content) && Imagebytes.length!=0)
        {


            final String push1 = dref.push().getKey();
            sRef.child("images")
                    .child("In")
                    .child(push1).putBytes(Imagebytes)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String linkImage=taskSnapshot.getDownloadUrl().toString();

                            String path="uulmy/"+push1+"/";

                            Map data=new HashMap();
                            data.put("title",title);
                            data.put("description",content);
                            data.put("image",linkImage);

                            Map endmap=new HashMap();
                            endmap.put(path,data);

                            dref.updateChildren(endmap).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    Toast.makeText(MainActivity.this, "sucess", Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                    });

        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == IMG_REQ && resultCode == RESULT_OK && data!=null)
//        {
//            Uri uri = data.getData();
//            try {
//                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
//                encodeImage(bitmap);
//                binding.btnSelectImage.setImageBitmap(bitmap);
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode==IMG_REQ && resultCode==RESULT_OK && data!=null)){

            Uri uri = data.getData();

            try {
                bitmap=BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                encodeImage(bitmap);

                binding.btnSelectImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }




    }

    private void encodeImage(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,90,outputStream);
        Imagebytes = outputStream.toByteArray();
    }
}
