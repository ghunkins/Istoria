package com.example.ghunk.istoria;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.File;

public class TakePhoto extends AppCompatActivity {

    ImageView result;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dispatchTakePictureIntent();    // launches the camera

        result = (ImageView)findViewById(R.id.takeImageView);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //String path = "sdcard/Istoria/test.jpg";
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            result.setImageBitmap(imageBitmap);
        }
    }

    /* Pulled from official Android site:
       https://developer.android.com/training/camera/photobasics.html
     */
    public void dispatchTakePictureIntent() {
        File file = getFile();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);   // get intent to take photo
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));  // add saving photo to file
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    private File getFile() {
        File folder = new File("sdcard/Istoria");
        if (!folder.exists()) {
            folder.mkdir();
        }
        File image_file = new File(folder, "data.jpg");
        return image_file;
    }


}
