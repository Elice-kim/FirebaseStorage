package com.fortest.elice.firebasestorage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * open camera
 * upload firebase storage
 * and download image
 */

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_CHECK = 100;
    private static final int PERMISSION_OK = 200;

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference mountainImagesRef;

    @BindView(R.id.cameraFabBtn)
    FloatingActionButton cameraFabBtn;
    @BindView(R.id.storageImageView)
    ImageView storageImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setUpforFireBase();


    }

    private void setUpforFireBase() {
        storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        storageRef = storage.getReferenceFromUrl(Constant.STORAGE_URL);
    }


    @OnClick(R.id.cameraFabBtn)
    void cameraFabBtnClicked() {
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            startGalleryPick();
        } //처음에 권한 허가가 되어있지 않으므로 가장 처음에 퍼미션 요청하는 부분
        else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CHECK);
        }
    }

    //퍼미션 요청 후 결과에 해당 되는 부분
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CHECK){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults.length>0){
                startGalleryPick();
            }else return;
        }
    }

    private void startGalleryPick() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        galleryIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PERMISSION_OK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PERMISSION_OK && resultCode==RESULT_OK){
            Uri uri = data.getData();
            Glide.with(this)
                    .load(uri)
                    .into(storageImageView);

            //image uploading 
            StorageReference riversRef = storageRef.child("images/1.jpg");
            UploadTask uploadTask = riversRef.putFile(uri);
            uploadTask.addOnFailureListener(e->{
                    // Handle unsuccessful uploads
                    Toast.makeText(MainActivity.this, "이미지 업로드 실패", Toast.LENGTH_SHORT).show();
            }).addOnSuccessListener(taskSnapshot -> {
                    Toast.makeText(MainActivity.this, "이미지 업로드 성공", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
