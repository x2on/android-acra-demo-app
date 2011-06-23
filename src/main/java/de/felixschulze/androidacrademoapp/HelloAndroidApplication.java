package de.felixschulze.androidacrademoapp;

import android.app.Application;
import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;


@ReportsCrashes(formKey = "dG4tQWJUcjV2VEl2SlRmSnRyTWUtTGc6MQ")
public class HelloAndroidApplication extends Application {
    @Override
    public void onCreate() {
        // The following line triggers the initialization of ACRA
        ACRA.init(this);
        super.onCreate();
    }
}
