package com.asmaa.m.firebasedata.Adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asmaa.m.firebasedata.Activities.Viewr;
import com.asmaa.m.firebasedata.Models.DataModel;
import com.asmaa.m.firebasedata.R;
import com.asmaa.m.firebasedata.databinding.ItemBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by m on 4/26/2018.
 */

public class Adapter  extends RecyclerView.Adapter<Adapter.myHolder>{

    private Context context;
    List<DataModel> dataModelList;
    Viewr viewr;

    public Adapter(Context context, List<DataModel> dataModelList) {
        this.context = context;
        this.dataModelList = dataModelList;
        this.viewr= (Viewr) context;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item,parent,false);
        return new myHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final myHolder holder, int position) {
        DataModel dataModel = dataModelList.get(position);
        holder.itemBinding.setModel(dataModel);
        Picasso.with(context).load(Uri.parse(dataModel.getImage())).into(holder.itemBinding.img);
        holder.itemBinding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                viewr.setPos(holder.getAdapterPosition());

                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    public class myHolder extends RecyclerView.ViewHolder{
        ItemBinding itemBinding;
        public myHolder(ItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }
}
