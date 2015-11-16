package de.iweinzierl.easyprofiles;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.base.Strings;
import com.orm.SugarRecord;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import de.inselhome.android.utils.UiUtils;
import de.iweinzierl.easyprofiles.adapter.ListAdapter;
import de.iweinzierl.easyprofiles.persistence.LogEntity;

public class LogDisplayActivity extends BaseActivity {

    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
            }
             else {
                stacktrace.setText(log.getStacktrace());
            }

            return itemView;
        }
    }

    private ListView logsList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_log_display;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logsList = (ListView) findViewById(R.id.log_list);
    }



    @Override
    protected void onResume() {
        super.onResume();
        List<LogEntity> logs = SugarRecord.listAll(LogEntity.class);

        logsList.setAdapter(new LogAdapter(this, logs));
    }
}
