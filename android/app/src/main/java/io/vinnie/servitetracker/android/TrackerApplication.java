package io.vinnie.servitetracker.android;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.parse.Parse;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by vinnie on 6/11/14.
 */
public class TrackerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Crashlytics.start(this);

        CalligraphyConfig.initDefault("fonts/Roboto-Light.ttf");

        TrackerSharedPrefs.init(this);

        Parse.initialize(this, "qpeg6z2VCYJfmKbnPmqgp3UOjYFbOETBoKlTDOVN", "TBPBPFtcAXPn1DvPAKzjA6i1UciudXwLe08VC1yS");
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

}
