package by.lovata.a2doc.screenRecordDoctor.screenTimetableDoctor;


import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import by.lovata.a2doc.R;

class TimeAdapter extends BaseAdapter {

    static interface RecordTime {
        public void record(String day_selected, String time_selected);
    }

    private Times[] times;
    private Context context;
    private RecordTime recordTime;

    TimeAdapter(Context context, Times[] times) {
        this.times = times;
        this.context = context;
    }

    void setListener(RecordTime recordTime) {
        this.recordTime = recordTime;
    }

    @Override
    public int getCount() {
        return times.length;
    }

    @Override
    public Object getItem(int position) {
        return times[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Times time = (Times) getItem(position);
        View view = LayoutInflater.from(context).inflate(R.layout.timetable, null);

        final TextView day = (TextView) view.findViewById(R.id.timetable_day);
        day.setText(time.day);

        if (time.times[0].equals("null")) {
            TextView textView = new TextView(context);
            textView.setText(context.getResources().getString(R.string.timetable_not_get));
            ((ViewGroup) view.findViewById(R.id.root_timetable)).addView(textView);
        } else {
            ViewGroup root_view = ((ViewGroup) view.findViewById(R.id.root_timetable));

            GridLayout gridLayout = new GridLayout(context);
            gridLayout.setColumnCount(3);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            root_view.addView(gridLayout, layoutParams);

            ViewGroup.LayoutParams layoutParams_button = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            for (final String time_coming : time.times) {
                Button button = new Button(context);
                button.setText(time_coming);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recordTime.record(time.day, time_coming);
                    }
                });
                gridLayout.addView(button, layoutParams_button);
            }
            TextView border = (TextView) view.findViewById(R.id.timetable_border);
            border.setText(time.start + "  " + time.stop);
        }

        return view;
    }

    void setTimes(Times[] times) {
        this.times = times;
        notifyDataSetInvalidated();
    }
}
