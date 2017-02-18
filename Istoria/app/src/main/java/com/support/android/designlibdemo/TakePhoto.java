/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.support.android.designlibdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TakePhoto extends AppCompatActivity {

    ImageView result;                               // imageview to display the photo
    static final int REQUEST_IMAGE_CAPTURE = 1;     //
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        dispatchTakePictureIntent();    // launches the camera

        result = (ImageView)findViewById(R.id.takeImageView);
        result.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // display the background as the photo taken
        result = (ImageView)findViewById(R.id.takeImageView);
        result.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));

        // SEND THE .JPG FILE TO THE SERVER
        VisionManager uploader = new VisionManager();
        uploader.uploadImage(Uri.fromFile(new File(mCurrentPhotoPath)), this);

        // display the loading ProgressDialog
        final ProgressDialog pd = new ProgressDialog(TakePhoto.this);
        pd.setTitle("Black Magic");
        pd.setMessage("Wait while black magic is performed...");
        pd.setIcon(R.drawable.ic_discuss);
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        /* begin Timer to keep track of how long server is taking */
        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                /* update the pd message */
            }
            public void onFinish() {
                //mTextField.setText("done!");
                /* return to taking a photo, display "sorry" message */
                pd.dismiss();

                Intent intent = new Intent(TakePhoto.this, DisplayGoodReads.class);
                //intent.putExtra(DisplayGoodReads.EXTRA_NAME, holder.mBoundString);
                //final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);

                startActivity(intent);
            }
        }.start();
    }

    /* Pulled from official Android site:
       https://developer.android.com/training/camera/photobasics.html
     */
    public void dispatchTakePictureIntent() {
        File photoFile = createImageFile();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);     // get intent to take photo
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));  // add saving photo to file
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /* Returns the File object of the created photo file
     * Also updates mCurrentPhotoPath with the current path of the file */
    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        File externalStorage = Environment.getExternalStorageDirectory();
        File storageDir = new File(externalStorage.getAbsolutePath() + "/Istoria");
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File image = new File(storageDir, imageFileName);
        mCurrentPhotoPath = image.getAbsolutePath();



        return image;
    }
}

