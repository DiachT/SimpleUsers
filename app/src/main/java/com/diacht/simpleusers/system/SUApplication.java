package com.diacht.simpleusers.system;

import android.app.Application;

import com.diacht.simpleusers.R;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

/**
 * SUApplication
 * @author Tetiana Diachuk (diacht@gmail.com)
 */
@ReportsCrashes(formKey = "4802b638-2b25-4af0-8086-aaf74e148ac0", mailTo = "diacht@gmail.com")
public class SUApplication extends Application{
    public final static boolean ACRA_ENABLED = true;
    private SUSettings mSettings;
    @Override
    public void onCreate() {
        if (ACRA_ENABLED) {
            ACRA.init(this);
        }
        super.onCreate();
        mSettings = new SUSettings(this);
    }

    public SUSettings getSettings() {
        return mSettings;
    }


}
