package de.iweinzierl.easyprofiles.persistence.listener;

import android.content.Context;
import android.util.Log;

import com.orm.SugarRecord;
import com.orm.entity.annotation.PostRemove;
import com.orm.entity.annotation.PrePersist;

import java.util.List;

import de.iweinzierl.easyprofiles.persistence.PersistentTrigger;
import de.iweinzierl.easyprofiles.persistence.Profile;

public class ProfileEntityListener {

    private final Context context;

    public ProfileEntityListener(Context context) {
        this.context = context;
    }

    @PrePersist
    public void prePersist(Profile profile) {
        Log.d("easyprofiles", "Store children of Profile first");

        if (profile.getWifiSettings() != null) {
            SugarRecord.save(profile.getWifiSettings());
        }

        if (profile.getVolumeSettings() != null) {
            SugarRecord.save(profile.getVolumeSettings());
        }

        if (profile.getDataSettings() != null) {
            SugarRecord.save(profile.getDataSettings());
        }

        if (profile.getExtraSettings() != null) {
            SugarRecord.save(profile.getExtraSettings());
        }

    }

    @PostRemove
    public void postRemove(Profile profile) {
        List<PersistentTrigger> triggers = SugarRecord.find(
                PersistentTrigger.class,
                "on_activate_profile_id = ? or on_deactivate_profile_id = ?",
                String.valueOf(profile.getId()), String.valueOf(profile.getId()));

        for (PersistentTrigger trigger : triggers) {
            if (SugarRecord.delete(trigger)) {
                Log.d("easyprofiles", "Deleted depending trigger after profile deletion: " + trigger);
            }
        }
    }
}
