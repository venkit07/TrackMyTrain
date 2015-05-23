package com.royce.thoughtworks.application;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.SaveCallback;

/**
 * Created by RRaju on 12/9/2014.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "VllO7fW0b79l4aKhs6TQjjxqzrLDRxXbg4PCE9fa",
                "7CJ66l1Vk5ZDtPRyz3PxaA7spl6g245WQBLJnB3i");
        ParseInstallation.getCurrentInstallation().saveInBackground();

    }
}
