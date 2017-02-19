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
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class DisplayGoodReads extends AppCompatActivity {

    public static final String EXTRA_NAME = "title";
    public static final String EXTRA_AUTHOR = "author";
    public static final String EXTRA_RATING = "rating";
    public static final String EXTRA_SYNOPSIS = "synopsis";
    public static final String EXTRA_IMAGE = "image";

    String bookTitle;
    String author;
    String ratingText;
    String synopsis;
    String image;
    List<List<String>> reviews;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);       // set the xml to activity_detail

        Intent intent = getIntent();

        // parse the values from intent
        final String cheeseName = intent.getStringExtra(EXTRA_NAME);    //gets the title name from intent
        bookTitle = intent.getStringExtra(EXTRA_NAME);
        author = intent.getStringExtra(EXTRA_AUTHOR);
        ratingText = intent.getStringExtra(EXTRA_RATING);
        synopsis = intent.getStringExtra(EXTRA_SYNOPSIS);
        synopsis = android.text.Html.fromHtml(synopsis).toString();
        image = intent.getStringExtra(EXTRA_IMAGE);

        String test = "<strong>Harry</strong><strong> Potter's life is miserable. His parents are dead and he's stuck with his heartless relatives, who force him to live in a tiny closet under the stairs. But his fortune changes when he receives a letter that tells him the truth about himself: he's a wizard. A mysterious visitor rescues him from his relatives and takes him to his new home, Hogwarts School of Witchcraft and Wizardry.<br /><br />After a lifetime of bottling up his magical powers, Harry finally feels like a normal kid. But even within the Wizarding community, he is special. He is the boy who lived: the only person to have ever survived a killing curse inflicted by the evil Lord Voldemort, who launched a brutal takeover of the Wizarding world, only to vanish after failing to kill Harry.<br /><br />Though Harry's first year at Hogwarts is the best of his life, not everything is perfect. There is a dangerous secret object hidden within the castle walls, and Harry believes it's his responsibility to prevent it from falling into evil hands. But doing so will bring him into contact with forces more terrifying than he ever could have imagined.<br /><br />Full of sympathetic characters, wildly imaginative situations, and countless exciting details, the first installment in the series assembles an unforgettable magical world and sets the stage for many high-stakes adventures to come.";
        Log.i("test", android.text.Html.fromHtml(test).toString());

        // load the toolbars
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);   //finds the toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(cheeseName);                         // sets the title name

        loadBackdrop();                                                 //loads the image

        /* use specific objects in order to access */
        TextView bookTitle = (TextView)findViewById(R.id.book_title);
        TextView author = (TextView)findViewById(R.id.author);
        TextView ratingText = (TextView)findViewById(R.id.ratingText);
        TextView synopsis = (TextView)findViewById(R.id.synopsis);
        TextView review = (TextView)findViewById(R.id.review);
        RatingBar ratingBar = (RatingBar)findViewById(R.id.ratingBar);

        ratingBar.setRating(Float.parseFloat(this.ratingText));

        bookTitle.setText(this.bookTitle);
        author.setText(this.author);
        ratingText.setText(this.ratingText+"/5.00");
        synopsis.setText(this.synopsis);
    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(image.replace("\\", "")).centerCrop().into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("onOptionsItem", item.toString());
        if (item.getItemId() == android.R.id.home) {       // if it's the back button
            Intent myIntent = new Intent(getApplicationContext(), TakePhoto.class);
            startActivityForResult(myIntent, 0);
        }
        return true;

    }
}
