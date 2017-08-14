package by.lovata.a2doc.screenRecordDoctor.screenTimetableDoctor;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import by.lovata.a2doc.R;

class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder>{

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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.timetable, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Times time = times.get(position);
        //holder.setIsRecyclable(false);
        String date = getDate(time.day);
        holder.day.setText(date);

        if (time.times[0].equals("null")) {
            setEmptyView(holder);
        } else {
            setFullView(holder, time);
        }
    }

    @Override
    public int getItemCount() {
        return times.size();
    }

    private void setFullView(ViewHolder holder, final Times time) {

        holder.gridLayout= new GridLayout(context);
        setLayoutParams(holder.gridLayout);
        holder.rootView.addView(holder.gridLayout);

        for (final String timeComing : time.times) {
            holder.buttonTimetable = new Button(context);
            holder.buttonTimetable.setText(timeComing);

            setLayoutParamsToButton(holder.buttonTimetable);
            holder.buttonTimetable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recordTime.record(time.day, timeComing);
                }
            });

            holder.gridLayout.addView(holder.buttonTimetable);
        }

        String string_border = createBorder(time.start, time.stop);
        holder.border.setText(string_border);
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

    private void setEmptyView(ViewHolder holder) {
        holder.noTimetable = new TextView(context);
        holder.noTimetable.setText(context.getResources().getString(R.string.timetable_not_get));
        holder.noTimetable.setTextSize(20);
        holder.noTimetable.setGravity(Gravity.RIGHT);
        holder.noTimetable.setPadding(20, 0, 0, 0);
        holder.noTimetable.setTextColor(ContextCompat.getColor(context, R.color.textColorPrimary));
        holder.viewGroup.addView(holder.noTimetable);
    }

    void setTimes(ArrayList<Times> times) {
        this.times = times;
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

    public class ViewHolder  extends RecyclerView.ViewHolder {
        public TextView day;
        private ViewGroup rootView;
        private TextView border;
        private ViewGroup viewGroup;
        private TextView noTimetable;
        private GridLayout gridLayout;
        private Button buttonTimetable;
        public ViewHolder(View itemView) {
            super(itemView);

            day = (TextView) itemView.findViewById(R.id.timetable_day);
            rootView = ((ViewGroup) itemView.findViewById(R.id.root_timetable));
            border = (TextView) itemView.findViewById(R.id.timetable_border);
            viewGroup=(ViewGroup)itemView.findViewById(R.id.liner_layout_time_information);
        }
    }
}
