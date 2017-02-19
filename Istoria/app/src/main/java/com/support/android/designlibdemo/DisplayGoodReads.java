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
import android.view.Menu;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DisplayGoodReads extends AppCompatActivity {

    public static final String EXTRA_NAME = "googlequery";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);       // set the xml to activity_detail

        Intent intent = getIntent();

        final String cheeseName = intent.getStringExtra(EXTRA_NAME);    //gets the title name from intent


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);   //finds the toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(cheeseName);                         // sets the title name

        loadBackdrop();                                                 //loads the image

        /* LOAD THE DATA */
        TextView bookTitle = (TextView)findViewById(R.id.book_title);
        TextView author = (TextView)findViewById(R.id.author);
        TextView ratingText = (TextView)findViewById(R.id.ratingText);
        TextView synopsis = (TextView)findViewById(R.id.synopsis);
        TextView review = (TextView)findViewById(R.id.review);
        RatingBar ratingBar = (RatingBar)findViewById(R.id.ratingBar);


        /*
        bookTitle.setText("sup suckers");
        author.setText();
        ratingText.setText();
        synopsis.setText();
        review.setText();
        */

    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(Cheeses.getRandomCheeseDrawable()).centerCrop().into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }
}
