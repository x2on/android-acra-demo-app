package de.felixschulze.androidacrademoapp;

import android.app.Application;
import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.acra.ErrorReporter;

@ReportsCrashes(formKey = "", // will not be used
                formUri = "http://10.0.2.2:8888/reportcrash")
                //formUriBasicAuthLogin = "yourlogin", // optional
                //formUriBasicAuthPassword = "y0uRpa$$w0rd", // optional)
public class HelloAndroidApplication extends Application {

    private static ReportsCrashes mReportsCrashes;

    @Override
    public void onCreate() {
        // The following line triggers the initialization of ACRA
        ACRA.init(this);
        mReportsCrashes = this.getClass().getAnnotation(ReportsCrashes.class);
        JsonSender jsonSender = new JsonSender(mReportsCrashes.formUri(), null);
        ErrorReporter.getInstance().setReportSender(jsonSender);

        super.onCreate();
    }
}
