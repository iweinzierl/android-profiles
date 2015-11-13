package de.iweinzierl.easyprofiles.util;

import android.os.Build;

public final class AndroidUtils {

    public static int getBuildVersion() {
        return Build.VERSION.SDK_INT;
    }
}
