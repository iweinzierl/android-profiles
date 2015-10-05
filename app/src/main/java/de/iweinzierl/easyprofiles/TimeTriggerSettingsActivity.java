package de.iweinzierl.easyprofiles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.common.collect.Sets;
import com.google.gson.Gson;

import org.joda.time.LocalTime;

import de.iweinzierl.easyprofiles.domain.Day;
import de.iweinzierl.easyprofiles.domain.TimeBasedTrigger;

public class TimeTriggerSettingsActivity extends Activity {

    public static final String EXTRA_TIME_TRIGGER_DATA = "extra.timebasedtrigger.data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO implement and remove this mock
        TimeBasedTrigger.Data triggerData = TimeBasedTrigger.Data.from(
                LocalTime.now().plusMinutes(2),
                LocalTime.now().plusMinutes(3),
                Sets.newHashSet(Day.FRIDAY, Day.SATURDAY)
        );

        Intent data = new Intent();
        data.putExtra(EXTRA_TIME_TRIGGER_DATA, new Gson().toJson(triggerData));

        setResult(RESULT_OK, data);
        finish();
    }
}
