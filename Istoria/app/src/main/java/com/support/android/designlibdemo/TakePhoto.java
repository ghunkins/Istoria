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

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TakePhoto extends AppCompatActivity {

    ImageView result;                               // imageview to display the photo
    static final int REQUEST_IMAGE_CAPTURE = 1;     //
    String mCurrentPhotoPath;
    String GoogleResult = null;
    Uri masterUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        countDownToPhoto();


        //dispatchTakePictureIntent();    // launches the camera

        //result = (ImageView)findViewById(R.id.takeImageView);
        //result.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));

    }

    private void countDownToPhoto() {
        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {}
            public void onFinish() {
                Log.e("o", "the countdownToPhoto is finished");
                dispatchTakePictureIntent();
            }
        }.start();
    }

    /* Pulled from official Android site:
       https://developer.android.com/training/camera/photobasics.html
     */
    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);     // get intent to take photo
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (Exception e) {
                Log.e("o","photofile not created");
                e.printStackTrace();
            }
            if (photoFile != null) {
                //Log.e("1", Uri.parse(mCurrentPhotoPath).toString());
                //Uri photoUri = FileProvider.getUriForFile(this, "com.support.android.fileprovider", photoFile);
                Uri photoUri = Uri.fromFile(photoFile);
                masterUri = Uri.fromFile(photoFile);
                //Uri photoUri = Uri.parse(mCurrentPhotoPath);
                //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse(mCurrentPhotoPath));  // add saving photo to file
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);  // add saving photo to file
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                Log.e("o", "it's starting the picture intent");
            }
        }
    }

    /* Returns the File object of the created photo file
     * Also updates mCurrentPhotoPath with the current path of the file */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        File externalStorage = Environment.getExternalStorageDirectory();
        File storageDir = new File(externalStorage.getAbsolutePath() + "/Istoria");
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        //File image = new File(this.getFilesDir(), imageFileName);
        //File image = new File(storageDir, imageFileName);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        //Log.e("0", Uri.parse(mCurrentPhotoPath).toString());
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.e("o", "IMAGE SUCCESSFUL");
            //Log.e("2", Uri.parse(mCurrentPhotoPath).toString());
        }
        // display the background as the photo taken
        //result = (ImageView)findViewById(R.id.takeImageView);
        //result.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));


        // SEND THE .JPG FILE TO THE SERVER
        VisionManager uploader = new VisionManager();
        //uploader.uploadImage(Uri.parse(mCurrentPhotoPath), this);
        //uploader.uploadImage(Uri.fromFile(new File(mCurrentPhotoPath)), this);      // LOOK AT THIS
        uploader.uploadImage(masterUri, this);

        // WAIT FOR THE DAMN ASYNC TO FUCKING FINISH AND FUCKING RETURN
        // ???

        // display the loading ProgressDialog
        /*final ProgressDialog pd = new ProgressDialog(TakePhoto.this);
        pd.setTitle("Black Magic");
        pd.setMessage("suck my black magic peepee");
        pd.setIcon(R.drawable.ic_discuss);
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);
        pd.show();*/

        /* begin Timer to keep track of how long server is taking */
        /*new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);

            }
            public void onFinish() {
                //mTextField.setText("done!");
                pd.dismiss();

                Intent intent = new Intent(TakePhoto.this, DisplayGoodReads.class);
                //intent.putExtra(DisplayGoodReads.EXTRA_NAME, holder.mBoundString);
                //final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);

                startActivity(intent);
            }
        }.start();*/
    }
}

