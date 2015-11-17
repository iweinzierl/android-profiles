package de.iweinzierl.easyprofiles.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.orm.SugarRecord;

import org.joda.time.LocalTime;
import org.slf4j.Logger;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.iweinzierl.easyprofiles.domain.TimeBasedTrigger;
import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;
import de.iweinzierl.easyprofiles.persistence.Profile;
import de.iweinzierl.easyprofiles.persistence.TriggerType;

public class ProfileActivator {

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(ProfileActivator.class.getName());

    public static class NotFoundException extends Exception {
        public NotFoundException(String detailMessage) {
            super(detailMessage);
        }
    }

    public static class NotPermittedException extends Exception {
        public NotPermittedException(String detailMessage) {
            super(detailMessage);
        }
    }

    private static final String SHARED_PREFERENCES = "prefs.profile.activator";
    private static final String PREFS_ACTIVE_PROFILE_ID = "prefs.profile.activator.active.profileid";
    private static final String PREFS_ACTIVE_TRIGGER_ID = "prefs.profile.activator.active.triggerid";

    private final Context context;

    public ProfileActivator(Context context) {
        this.context = context;
    }

    public void activate(PersistentTrigger trigger) throws NotFoundException, NotPermittedException {
        enable(trigger.getOnActivateProfileId(), trigger.getId());
    }

    public void activate(Profile profile) {
        profile.activate(context);

        NotificationHelper helper = new NotificationHelper(context);
        helper.publishProfileNotification(profile);

        SharedPreferences.Editor edit = getSharedPreferences().edit();
        edit.putLong(PREFS_ACTIVE_PROFILE_ID, profile.getId());
        edit.putLong(PREFS_ACTIVE_TRIGGER_ID, 0);
        edit.commit();

        LOG.info("Successfully activated profile: {}", profile);
    }

    public void deactivate(PersistentTrigger trigger) throws NotFoundException, NotPermittedException {
        enable(trigger.getOnDeactivateProfileId(), trigger.getId());
    }

    @Nullable
    public PersistentTrigger getActiveTrigger() {
        SharedPreferences prefs = getSharedPreferences();
        long triggerId = prefs.getLong(PREFS_ACTIVE_TRIGGER_ID, -1);

        if (triggerId <= 0) {
            return null;
        } else {
            return SugarRecord.findById(PersistentTrigger.class, triggerId);
        }
    }

    private void enable(long profileId, long triggerId) throws NotFoundException, NotPermittedException {
        if (isTimeBasedTriggerActive()) {
            throw new NotPermittedException("Time based trigger currently active");
        }

        Profile profile = searchProfileById(profileId);
        profile.activate(context);

        NotificationHelper helper = new NotificationHelper(context);
        helper.publishProfileNotification(profile);

        SharedPreferences.Editor edit = getSharedPreferences().edit();
        edit.putLong(PREFS_ACTIVE_PROFILE_ID, profileId);
        edit.putLong(PREFS_ACTIVE_TRIGGER_ID, triggerId);
        edit.commit();

        LOG.info("Successfully activated profile: {}", profile);
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    private Profile searchProfileById(long profileId) throws NotFoundException {
        Profile profile = SugarRecord.findById(Profile.class, profileId);

        if (profile == null) {
            throw new NotFoundException(String.format("No Profile with ID '%s' found", profileId));
        }

        return profile;
    }

    private boolean isTimeBasedTriggerActive() {
        PersistentTrigger activeTrigger = getActiveTrigger();

        if (activeTrigger != null && activeTrigger.getType() == TriggerType.TIME_BASED) {
            TimeBasedTrigger trigger = new TimeBasedTrigger();
            trigger.apply(activeTrigger);

            return trigger.fulfillsCondition(LocalTime.now());
        }

        return false;
    }
}
