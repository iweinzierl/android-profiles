package de.iweinzierl.easyprofiles.util;

import android.content.Context;
import android.content.res.Resources;
import android.media.AudioManager;

import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.persistence.RingtoneMode;

public class RingtoneModeHelper {

    public static RingtoneMode translateToRingtoneMode(int mode) {
        switch (mode) {
            case AudioManager.RINGER_MODE_NORMAL:
                return RingtoneMode.NORMAL;
            case AudioManager.RINGER_MODE_SILENT:
                return RingtoneMode.SILENT;
            case AudioManager.RINGER_MODE_VIBRATE:
                return RingtoneMode.VIBRATE;
            default:
                return RingtoneMode.NORMAL;
        }
    }

    public static int translateToAudioManagerRingerMode(RingtoneMode ringtoneMode) {
        switch (ringtoneMode) {
            case NORMAL:
                return AudioManager.RINGER_MODE_NORMAL;
            case SILENT:
                return AudioManager.RINGER_MODE_SILENT;
            case VIBRATE:
                return AudioManager.RINGER_MODE_VIBRATE;
            default:
                return AudioManager.RINGER_MODE_NORMAL;
        }
    }

    public static String getRingtoneModeLabel(Context context, RingtoneMode ringtoneMode) {
        Resources resources = context.getResources();

        switch (ringtoneMode) {
            case NORMAL:
                return resources.getString(R.string.ringtone_mode_normal);
            case VIBRATE:
                return resources.getString(R.string.ringtone_mode_vibrate);
            case SILENT:
                return resources.getString(R.string.ringtone_mode_silent);
            default:
                return "unknown ringtone mode";
        }
    }
}
