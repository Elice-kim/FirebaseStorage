package com.fortest.elice.firebasestorage;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**open camera
 * upload firebase storage
 * and download image */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.cameraFabBtn)
    FloatingActionButton cameraFabBtn;
    @BindView(R.id.storageImageView)
    ImageView storageImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


}
