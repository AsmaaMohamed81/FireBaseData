package com.asmaa.m.firebasedata.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;

import com.asmaa.m.firebasedata.Adapters.Adapter;
import com.asmaa.m.firebasedata.EventListener.Event;
import com.asmaa.m.firebasedata.Models.DataModel;
import com.asmaa.m.firebasedata.R;
import com.asmaa.m.firebasedata.databinding.ActivityMainBinding;
import com.asmaa.m.firebasedata.databinding.ActivityViewBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Viewr extends AppCompatActivity implements Event{
    private ActivityViewBinding binding;
    private List<DataModel> dataModelList;
    private DatabaseReference dRef;
    private Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view);


        dataModelList = new ArrayList<>();
        adapter = new Adapter(this, dataModelList);
        binding.recycle.setLayoutManager(new LinearLayoutManager(this));
        binding.recycle.setHasFixedSize(true);
        binding.recycle.setAdapter(adapter);

        dRef = FirebaseDatabase.getInstance().getReference();
        DisplayData();
        search();
    }

    private void DisplayData() {


        dataModelList.clear();
        dRef.child("uulmy").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    DataModel dataModel=dataSnapshot1.getValue(DataModel.class);
                    dataModelList.add(dataModel);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(android.view.View view) {
        int id=view.getId();
        switch (id){


        }
    }

    private void search() {
       // dataModelList.clear();

        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                dataModelList.clear();
               dRef.child("uulmy").addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                       if (dataSnapshot!=null)
                       {
                           for(DataSnapshot ds:dataSnapshot.getChildren())
                           {
                               if (ds.child("title").getValue().toString().contains(newText))
                               {
                                   dataModelList.add(ds.getValue(DataModel.class));
                               }
                           }
                           if (dataModelList.size()>0)
                           {
                               adapter.notifyDataSetChanged();
                           }

                       }
                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                   }
               });
                return false;
            }
        });
    }
    public void setPos(int pos)
    {
        Intent intent=new Intent(Viewr.this,ModivyActivity.class);

        DataModel dataModel = dataModelList.get(pos);

        intent.putExtra("ss", (Serializable) dataModel);


        startActivity(intent);


    }
}
