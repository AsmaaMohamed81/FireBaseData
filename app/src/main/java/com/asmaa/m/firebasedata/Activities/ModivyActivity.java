package com.asmaa.m.firebasedata.Activities;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.asmaa.m.firebasedata.EventListener.Event;
import com.asmaa.m.firebasedata.Models.DataModel;
import com.asmaa.m.firebasedata.R;
import com.asmaa.m.firebasedata.databinding.ActivityModivyBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class ModivyActivity extends Activity  implements Event {
private  ActivityModivyBinding activityModivyBinding;
DataModel dataModel;
DatabaseReference dRef;
StorageReference SRef;

private int RECS=200;
    private byte[] Imagebytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dRef = FirebaseDatabase.getInstance().getReference();
        SRef= FirebaseStorage.getInstance().getReference();

        Intent intent=getIntent();

        dataModel = (DataModel) intent.getSerializableExtra("ss");

        activityModivyBinding= DataBindingUtil.setContentView(this,R.layout.activity_modivy);
        activityModivyBinding.setModel(dataModel);


        Picasso.with(this).load(Uri.parse(dataModel.getImage())).into(activityModivyBinding.btnSelectImage);
        activityModivyBinding.setEvent(this);



    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case (R.id.btn_update):
                Updat();
                break;
                
            case (R.id.btn_delete):
                Delete();
                
                break;
            case (R.id.btnSelectImage):

                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent.createChooser(intent,"select"),RECS);

               break;

        }
    }

    private void Delete() {

        Log.e("sssssssss", "sssssssssssssssssss");
        dRef.child("uulmy").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String title = ds.child("title").getValue().toString();
                    String desc = ds.child("description").getValue().toString();
                    String img = ds.child("image").getValue().toString();


                    if (title.equals(dataModel.getTitle()) && desc.equals(dataModel.getDescription()) && img.equals(dataModel.getImage())) {
                        ds.getRef().removeValue();
                        Intent intent = new Intent(ModivyActivity.this, Viewr.class);
                        startActivity(intent);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void Updat() {
        dRef.child("uulmy").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                    String title = ds.child("title").getValue().toString();
                    String desc = ds.child("description").getValue().toString();
                    String img = ds.child("image").getValue().toString();


                    if (title.equals(dataModel.getTitle()) && desc.equals(dataModel.getDescription()) && img.equals(dataModel.getImage())) {


                        SRef.child("images")
                                .child("In")
                                .child(ds.getKey()).putBytes(Imagebytes)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        String linkImage=taskSnapshot.getDownloadUrl().toString();

                                        String path="uulmy/"+ds.getKey()+"/";

                                        Map data=new HashMap();
                                        data.put("title",activityModivyBinding.titleEdittext.getText().toString());
                                        data.put("description",activityModivyBinding.descriptionEdittext.getText().toString());
                                        data.put("image",linkImage);

                                        Map endmap=new HashMap();
                                        endmap.put(path,data);
                                        dRef.updateChildren(endmap);
                        Intent intent = new Intent(ModivyActivity.this, Viewr.class);
                        startActivity(intent);

                                        dRef.updateChildren(endmap).addOnSuccessListener(new OnSuccessListener() {
                                            @Override
                                            public void onSuccess(Object o) {
                                                Toast.makeText(ModivyActivity.this, "sucess", Toast.LENGTH_SHORT).show();

                                            }
                                        });

                                    }
                                });
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==RECS&&resultCode==RESULT_OK&&data!=null){

            Uri uri=data.getData();

            try {
                Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                encodeImage(bitmap);
                activityModivyBinding.btnSelectImage.setImageBitmap(bitmap);
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
