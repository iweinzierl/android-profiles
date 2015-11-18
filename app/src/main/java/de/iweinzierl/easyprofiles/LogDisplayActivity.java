package de.iweinzierl.easyprofiles;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.orm.SugarRecord;

import org.joda.time.DateTime;
import org.slf4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import de.inselhome.android.logging.AndroidLoggerFactory;
import de.inselhome.android.utils.UiUtils;
import de.iweinzierl.easyprofiles.adapter.ListAdapter;
import de.iweinzierl.easyprofiles.adapter.TimeRangeAdapter;
import de.iweinzierl.easyprofiles.domain.TimePeriod;
import de.iweinzierl.easyprofiles.domain.TimeUnitExt;
import de.iweinzierl.easyprofiles.persistence.LogEntity;

public class LogDisplayActivity extends BaseActivity {

    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final Logger LOG = AndroidLoggerFactory.getInstance().getLogger(LogDisplayActivity.class.getName());

    public class LogAdapter extends ListAdapter<LogEntity> {

        public LogAdapter(Context context, List<LogEntity> items) {
            super(context, items);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LogEntity log = (LogEntity) getItem(i);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.list_item_log, viewGroup, false);

            TextView date = UiUtils.getGeneric(TextView.class, itemView, R.id.date);
            TextView level = UiUtils.getGeneric(TextView.class, itemView, R.id.level);
            TextView message = UiUtils.getGeneric(TextView.class, itemView, R.id.message);
            TextView stacktrace = UiUtils.getGeneric(TextView.class, itemView, R.id.stacktrace);

            date.setText(DATE_FORMATTER.format(log.getTimestamp()));
            level.setText(log.getLevel());
            message.setText(log.getMessage());

            if (Strings.isNullOrEmpty(log.getStacktrace())) {
                stacktrace.getLayoutParams().height = 0;
            } else {
                stacktrace.setText(log.getStacktrace());
            }

            int color = 0;
            switch (log.getLevel()) {
                case "DEBUG":
                    color = getResources().getColor(R.color.LogDebug);
                    break;
                case "INFO":
                    color = getResources().getColor(R.color.LogInfo);
                    break;
                case "WARN":
                    color = getResources().getColor(R.color.LogWarn);
                    break;
                case "ERROR":
                    color = getResources().getColor(R.color.LogError);
                    break;
            }

            date.setTextColor(color);
            level.setTextColor(color);
            message.setTextColor(color);
            stacktrace.setTextColor(color);

            return itemView;
        }
    }

    private ListView logsList;
    private Spinner timeRangeSpinner;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_log_display;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logsList = (ListView) findViewById(R.id.log_list);
        timeRangeSpinner = (Spinner) findViewById(R.id.time_range);

        timeRangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateLogs();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        timeRangeSpinner.setAdapter(new TimeRangeAdapter(this, Lists.newArrayList(
                new TimePeriod(1, TimeUnitExt.HOUR),
                new TimePeriod(4, TimeUnitExt.HOUR),
                new TimePeriod(12, TimeUnitExt.HOUR),
                new TimePeriod(1, TimeUnitExt.DAY),
                new TimePeriod(1, TimeUnitExt.WEEK)
        )));

        timeRangeSpinner.setSelection(0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateLogs();
    }

    private void updateLogs() {
        TimePeriod timePeriod = (TimePeriod) timeRangeSpinner.getSelectedItem();

        DateTime now = DateTime.now();
        DateTime lowerRange;

        switch (timePeriod.getUnit()) {
            case HOUR:
                lowerRange = now.minusHours(timePeriod.getPeriod());
                break;
            case DAY:
                lowerRange = now.minusDays(timePeriod.getPeriod());
                break;
            case WEEK:
                lowerRange = now.minusWeeks(timePeriod.getPeriod());
                break;
            default:
                throw new IllegalArgumentException("Unsupported time unit: " + timePeriod.getUnit());
        }

        LOG.debug("Search logs starting of {}", lowerRange.toString("yyyy-MM-dd HH:mm:ss"));

        List<LogEntity> logs = SugarRecord.find(
                LogEntity.class,
                "timestamp >= ?",
                new String[]{String.valueOf(lowerRange.toDate().getTime())},
                null,
                "timestamp",
                null);

        LOG.debug("Found {} logs", logs.size());

        logsList.setAdapter(new LogAdapter(this, logs));
    }
}
