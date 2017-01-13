package com.hackingbuzz.loginapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {


    private TextView mTextIdentifier;
    private TextView mTextFullName;
    private TextView mTextEmail;
    private TextView mTextDob;
    private TextView mTextDescription;
    private TextView mTextGender;
    private TextView mTextLocale;
    private ImageView mProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();

        if (intent != null) {


            mTextIdentifier = (TextView) findViewById(R.id.text_identifier);
            mTextFullName = (TextView) findViewById(R.id.text_full_name);
            mTextEmail = (TextView) findViewById(R.id.text_email);
            mTextDob = (TextView) findViewById(R.id.text_dob);
            mTextDescription = (TextView) findViewById(R.id.text_description);
            mTextGender = (TextView) findViewById(R.id.text_gender);
            mTextLocale = (TextView) findViewById(R.id.text_locale);
            mProfilePicture = (ImageView) findViewById(R.id.profile_picture);

            //There! all the user information we downloaded from Facebook, Twitter or any service
            UserAccount account = intent.getParcelableExtra(LoginService.EXTRA_SOCIAL_ACCOUNT); // this is a key n it will give us account as a value  // getParcableExtra use to get object from other activity or service.

            //Just get-set stuff
            mTextIdentifier.setText(account.getIdentifier());
            mTextFullName.setText(account.getFullName());
            mTextEmail.setText(account.getEmail());
            mTextDob.setText(account.getDay() + " " + account.getMonth() + " " + account.getYear());
            mTextDescription.setText(account.getDescription());
            mTextGender.setText(account.getGender());
            mTextLocale.setText(account.getLocale());

            String pictureURL = account.getPictureURL();

            //BAM! I used a library called Picasso that lets you load images directly from a URL
            Picasso.with(this).load(pictureURL).into(mProfilePicture);  //mProfiePicture is a ImageView object
        }
    }
}
