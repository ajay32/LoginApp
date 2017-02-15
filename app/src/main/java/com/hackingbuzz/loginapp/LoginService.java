package com.hackingbuzz.loginapp;

import android.app.IntentService;
import android.content.IntentSender;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cloudrail.si.exceptions.AuthenticationException;
import com.cloudrail.si.interfaces.Profile;
import com.cloudrail.si.services.Facebook;
import com.cloudrail.si.services.GitHub;
import com.cloudrail.si.services.GooglePlus;
import com.cloudrail.si.services.Instagram;
import com.cloudrail.si.services.LinkedIn;
import com.cloudrail.si.services.MicrosoftLive;
import com.cloudrail.si.services.Twitter;
import com.cloudrail.si.services.Yahoo;
import com.cloudrail.si.types.DateOfBirth;




public class LoginService extends IntentService {

    public static final String EXTRA_PROFILE = "profile";
    public static final String EXTRA_SOCIAL_ACCOUNT = "account";
    private static final String TAG = "AJ";

    public LoginService() {
        super("Social Login Service");
    }

    private Profile init(SocialMediaProvider provider) throws AuthenticationException {
        //Superclass reference variable = subclass object
        Profile profile = null;   // polymorphism
        switch (provider) {
            case FACEBOOK:
                profile = new Facebook(this, getString(R.string.facebook_app_key), getString(R.string.facebook_app_secret));
                break;
            case TWITTER:
                profile = new Twitter(this, getString(R.string.twitter_app_key), getString(R.string.twitter_app_secret));
                break;
            case GOOGLE_PLUS:
                profile = new GooglePlus(this, getString(R.string.google_plus_app_key), getString(R.string.google_plus_app_secret));
                break;
            case INSTAGRAM:
                profile = new Instagram(this, getString(R.string.instagram_app_key), getString(R.string.instagram_app_secret));
                break;
            case LINKED_IN:
                profile = new LinkedIn(this, getString(R.string.linked_in_app_key), getString(R.string.linked_in_app_secret));
                break;
            case YAHOO:
                profile = new Yahoo(this, getString(R.string.yahoo_app_key), getString(R.string.yahoo_app_secret));
                break;
            case WINDOWS_LIVE:
                profile = new MicrosoftLive(this, getString(R.string.windows_live_app_key), getString(R.string.windows_live_app_secret));
                break;
            case GITHUB:
                profile = new GitHub(this, getString(R.string.github_app_key), getString(R.string.github_app_secret));
                break;
        }
        //At run time, profile will contain one of the 8 objects above and will fetch details from that particular provider
        return profile;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            try {
                Bundle extras = intent.getExtras();     // it will get value of our enum in Bundle datastructure (n to get value from it we need its getSerializable method n pass the key the value we want..     // getSerializable is a method of Bundle see below
                //Guess what I sent from the Activity which calls this Service! Our Enumeration variable!
                //It contains FACEBOOK or TWITTER or ... and is Serializable!
                SocialMediaProvider provider = (SocialMediaProvider) extras.getSerializable(EXTRA_PROFILE);  // extras contain the key(data) of a profile that we want to login see extras = mProfile see MainActivity.java

                //You already know which social account the user will use, just create an object of it.
                Profile profile = init(provider);

                //Create a User Account object with all the details
                UserAccount account = init(profile);

                //Start another Activity to show the user details
                Intent detailsIntent = new Intent(getApplicationContext(), DetailsActivity.class);

                //To directly start activities from an IntentService, we need to add this flag
                detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   // its not working before but putExtra method

                //Send the User Account object with all details
                // to pass objects in android we can use parcable technique or java serilization (through intents)
                detailsIntent.putExtra(EXTRA_SOCIAL_ACCOUNT, account);  // key value pair (key is constact we taken above ) ..u may use it when u need to use it like this when u need to use it in more than one place
               
                detailsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  // required if you are calling an activity from outside activity(means other than any other activity)..here i am calling through service  // facing instagram run time error

                startActivity(detailsIntent);
            } catch (AuthenticationException e) {

                //When you cancel in the middle of a login, cloud rail was throwing this exception, hence!
                Log.e(TAG, "onHandleIntent: You cancelled", e);

            }
        }
    }

    private UserAccount init(Profile profile) {
        //The usual get-set stuff
        UserAccount account = new UserAccount();
        account.setIdentifier(profile.getIdentifier());
        account.setFullName(profile.getFullName());
        account.setEmail(profile.getEmail());
        account.setDescription(profile.getDescription());
        DateOfBirth dob = profile.getDateOfBirth();
        if (dob != null) {
            Long day = dob.getDay();
            Long month = dob.getMonth();
            Long year = dob.getYear();
            if (day != null) {
                account.setDay(day);
            }
            if (month != null) {
                account.setMonth(month);
            }
            if (year != null) {
                account.setYear(year);
            }
        }
        account.setGender(profile.getGender());
        account.setPictureURL(profile.getPictureURL());
        account.setLocale(profile.getLocale());
        return account;
    }
}
