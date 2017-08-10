package by.lovata.a2doc.screenRecordDoctor.screenTimetableDoctor;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import by.lovata.a2doc.R;

class TimeAdapter extends BaseAdapter {

    interface RecordTime {
        void record(String day_selected, String time_selected);
    }

    private ArrayList<Times> times;
    private Context context;
    private RecordTime recordTime;

    TimeAdapter(Context context, ArrayList<Times> times) {
        this.context = context;
        this.times = times;
    }

    void setListener(RecordTime recordTime) {
        this.recordTime = recordTime;
    }

    @Override
    public int getCount() {
        return times.size();
    }

    @Override
    public Object getItem(int position) {
        return times.get(position);
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

        String date = getDate(time.day);
        final TextView day = (TextView) view.findViewById(R.id.timetable_day);
        day.setText(date);

        if (time.times[0].equals("null")) {
            setEmptyView(view);
        } else {
            setFullView(view, time);
        }

        return view;
    }

    private void setFullView(View view, final Times time) {
        ViewGroup root_view = ((ViewGroup) view.findViewById(R.id.root_timetable));

        GridLayout gridLayout = new GridLayout(context);
        setLayoutParams(gridLayout);
        root_view.addView(gridLayout);

        for (final String timeComing : time.times) {
            Button button = new Button(context);
            button.setText(timeComing);

            setLayoutParamsToButton(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recordTime.record(time.day, timeComing);
                }
            });

            gridLayout.addView(button);
        }

        String string_border = createBorder(time.start, time.stop);
        TextView border = (TextView) view.findViewById(R.id.timetable_border);
        border.setText(string_border);
    }

    private void setLayoutParamsToButton(Button button) {
        button.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        button.setBackgroundResource(R.drawable.time_selector);
        button.setHeight(37);
        button.setTextSize(17);
    }

    private void setLayoutParams(GridLayout gridLayout) {
        int column = Integer.valueOf(context.getResources().getString(R.string.timetable_size));
        gridLayout.setColumnCount(column);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        gridLayout.setLayoutParams(params);
        gridLayout.setUseDefaultMargins(true);
    }

    private String createBorder(String start, String stop) {
        String start_label = context.getResources().getString(R.string.timetable_start);
        String stop_label = context.getResources().getString(R.string.timetable_stop);
        return String.format("%s %s\n%s %s", start_label, start, stop_label, stop);
    }

    private void setEmptyView(View view) {
        TextView textView = new TextView(context);
        textView.setText(context.getResources().getString(R.string.timetable_not_get));
        textView.setTextSize(20);
        textView.setGravity(Gravity.RIGHT);
        textView.setPadding(20, 0, 0, 0);
        textView.setTextColor(ContextCompat.getColor(context, R.color.textColorPrimary));
        ((ViewGroup) view.findViewById(R.id.liner_layout_time_information)).addView(textView);
    }

    void setTimes(ArrayList<Times> times) {
        this.times = times;
        notifyDataSetInvalidated();
    }

    private String getDate(String day_of_year) {
        Calendar calendar = new GregorianCalendar();

        String[] parse_day_of_year = day_of_year.split("-");
        int year = Integer.valueOf(parse_day_of_year[0]);
        int month = Integer.valueOf(parse_day_of_year[1]);
        int date = Integer.valueOf(parse_day_of_year[2]);
        calendar.set(year, month, date);

        String day = null;
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                day = context.getResources().getStringArray(R.array.timetable_day)[0];
                break;
            case Calendar.TUESDAY:
                day = context.getResources().getStringArray(R.array.timetable_day)[1];
                break;
            case Calendar.WEDNESDAY:
                day = context.getResources().getStringArray(R.array.timetable_day)[2];
                break;
            case Calendar.THURSDAY:
                day = context.getResources().getStringArray(R.array.timetable_day)[3];
                break;
            case Calendar.FRIDAY:
                day = context.getResources().getStringArray(R.array.timetable_day)[4];
                break;
            case Calendar.SATURDAY:
                day = context.getResources().getStringArray(R.array.timetable_day)[5];
                break;
            case Calendar.SUNDAY:
                day = context.getResources().getStringArray(R.array.timetable_day)[6];
                break;
        }

        return String.format("%s (%s)", day_of_year, day);
    }
}
