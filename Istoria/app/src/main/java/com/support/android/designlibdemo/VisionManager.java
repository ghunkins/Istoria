package com.support.android.designlibdemo;

/**
 * Created by brand on 2/18/2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class VisionManager {

    private static final String CLOUD_VISION_API_KEY = "AIzaSyC15tABjgjnGRO4pshXCrH7ONlgTgGXWHk";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";

    private Context c;
    //public String toReturn = null;

    /* method called by TakePhoto.java */
    public void uploadImage(Uri uri, Context c) {
        this.c = c;
        if (uri != null) {
            try {
                // from Stack
                // http://stackoverflow.com/questions/3879992/how-to-get-bitmap-from-an-uri
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(c.getContentResolver(), uri);
                if (bitmap == null) Log.e("o","FUCK IT'S NOT BITMAPPING");
                callCloudVision(bitmap);
                //mMainImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                Log.d(TAG, "Image picking failed because " + e.getMessage());
                //Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, "Image picker gave us a null image.");
            //Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
        }
    }

    public void callCloudVision(final Bitmap bitmap) throws IOException {
        // Switch text to loading
        //mImageDetails.setText(R.string.loading_message);


        // Dialog for onPreExecute
        final ProgressDialog pd = new ProgressDialog(c);

        // Do the real work in an async task, because we need to use the network anyway
        new AsyncTask<Object, Void, String>() {

            @Override
            protected void onPreExecute() {
                pd.setTitle("Querying");
                pd.setMessage("Our magicians are conjuring...");
                pd.setIcon(R.mipmap.ic_launcher2);
                pd.setCancelable(true);
                pd.setCanceledOnTouchOutside(false);
                pd.show();
            }
            @Override
            protected String doInBackground(Object... params) {
                Log.d(TAG, "beginning do in background method");
                try {
                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

                    Log.d(TAG, "making initializer");
                    VisionRequestInitializer requestInitializer =
                            new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
                                /**
                                 * We override this so we can inject important identifying fields into the HTTP
                                 * headers. This enables use of a restricted cloud platform API key.
                                 */
                                @Override
                                protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                                        throws IOException {
                                    super.initializeVisionRequest(visionRequest);

                                    String packageName = c.getPackageName();
                                    visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                                    String sig = PackageManagerUtils.getSignature(c.getPackageManager(), packageName);

                                    visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                                }
                            };

                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                    builder.setVisionRequestInitializer(requestInitializer);
                    builder.setApplicationName("Istoria");

                    Log.d(TAG, "building vision");
                    Vision vision = builder.build();

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                            new BatchAnnotateImagesRequest();

                    Log.d(TAG, "setting requests");
                    batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
                        AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

                        // Add the image
                        Image base64EncodedImage = new Image();
                        // Convert the bitmap to a JPEG
                        // Just in case it's a format that Android understands but Cloud Vision
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                        byte[] imageBytes = byteArrayOutputStream.toByteArray();

                        // Base64 encode the JPEG
                        base64EncodedImage.encodeContent(imageBytes);
                        annotateImageRequest.setImage(base64EncodedImage);

                        // add the features we want
                        annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                            Feature labelDetection = new Feature();
                            labelDetection.setType("TEXT_DETECTION");  // changed from LABEL_DETECTION to TEXT_DETECTION
                            labelDetection.setMaxResults(1);           // changed from 10 to 1
                            add(labelDetection);
                        }});

                        // Add the list of one thing to the request
                        add(annotateImageRequest);
                    }});

                    Vision.Images.Annotate annotateRequest =
                            vision.images().annotate(batchAnnotateImagesRequest);
                    // Due to a bug: requests to Vision API containing large images fail when GZipped.
                    annotateRequest.setDisableGZipContent(true);
                    Log.d(TAG, "created Cloud Vision request object, sending request");

                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    return convertResponseToString(response);

                } catch (GoogleJsonResponseException e) {
                    Log.d(TAG, "failed to make API request because " + e.getContent());
                } catch (IOException e) {
                    Log.d(TAG, "failed to make API request because of other IOException " +
                            e.getMessage());
                }
                return "Cloud Vision API request failed. Check logs for details.";
            }

            protected void onPostExecute(String result) {
                Log.i("WE DID IT REDDIT", result);

                if (result.equals("Cloud Vision API request failed. Check logs for details.") || result.equals("nothing")) {  // do something if "nothing"
                    result = "harry potter and the prisoner";
                }

                Log.d(TAG, result);


                String API_BASE_URL = "http://ocr.simg.us";
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

                Retrofit.Builder builder = new Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create());

                Retrofit retrofit = builder.client(httpClient.build()).build();

                OCRClient client =  retrofit.create(OCRClient.class);

                Call<Book> call = client.doQuery(result);

                // Execute the call asynchronously. Get a positive or negative callback.
                call.enqueue(new Callback<Book>() {
                    @Override
                    public void onResponse(Call<Book> call, Response<Book> response) {

                        Log.d(TAG, "response");

                        // The network call was a success and we got a response
                        // TODO: use the repository list and display it
                        pd.dismiss();

                        Intent intent = new Intent(c, DisplayGoodReads.class);

                        intent.putExtra(DisplayGoodReads.EXTRA_NAME, response.body().getTitle());
                        intent.putExtra(DisplayGoodReads.EXTRA_RATING, response.body().getAverageRating());
                        intent.putExtra(DisplayGoodReads.EXTRA_IMAGE, response.body().getImageUrl());
                        intent.putExtra(DisplayGoodReads.EXTRA_AUTHOR, response.body().getAuthorName());
                        //final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);

                        c.startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<Book> call, Throwable t) {

                        Log.d(TAG, "fail");

                        // the network call was a failure
                        // TODO: handle error
                        pd.dismiss();
                    }
                });


                //toReturn = result;
                //delegate.processFinish(result);
                //mImageDetails.setText(result);
            }
        }.execute();
    }

    private String convertResponseToString(BatchAnnotateImagesResponse response) {
        try {
            String toReturn = null;
            Log.i("o", response.toString());
            List<EntityAnnotation> coolStuff = response.getResponses().get(0).getTextAnnotations();
            toReturn = coolStuff.get(0).getDescription();   // full text is the first argument
            toReturn = toReturn.replace("\n", " ");         // replace newlines with spaces
            return toReturn;                                // return
        } catch (Exception e) {
            return "nothing";
        }
    }

    public Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }
}
