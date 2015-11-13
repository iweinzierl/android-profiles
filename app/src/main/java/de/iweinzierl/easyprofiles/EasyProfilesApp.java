package de.iweinzierl.easyprofiles;

import com.orm.SugarApp;

import org.slf4j.Logger;

import de.inselhome.android.logging.AndroidLoggerFactory;

public class EasyProfilesApp extends SugarApp {

    private static final String LOG_TAG = "easyprofiles";

    private static final Logger LOG = AndroidLoggerFactory.getInstance(LOG_TAG).getLogger(EasyProfilesApp.class.getName());

    @Override
    public void onCreate() {
        super.onCreate();

        LOG.info("Application startup");
    }
}
