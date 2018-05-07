package com.asmaa.m.firebasedata.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.asmaa.m.firebasedata.EventListener.Event;
import com.asmaa.m.firebasedata.R;
import com.asmaa.m.firebasedata.databinding.MainMainBinding;

public class Main extends AppCompatActivity implements Event {
        private MainMainBinding mainBinding;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        mainBinding= DataBindingUtil.setContentView(this,R.layout.main_main);

        mainBinding.setEvent(this);





    }

    @Override
    public void onClick(View view) {
            int id =view.getId();

            switch (id){

                case R.id.btn_add:

                    Intent intent=new Intent(Main.this,MainActivity.class);
                    startActivity(intent);

                    break;
                case R.id.btn_view:

                    Intent intent1=new Intent(Main.this, Viewr.class);
                    startActivity(intent1);

                    break;
            }

    }
}
