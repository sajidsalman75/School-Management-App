package co.onsets.school.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.onsets.school.Model.Course;
import co.onsets.school.R;

public class CourseResultAdapter extends RecyclerView.Adapter<CourseResultAdapter.MyViewHolder> {
    private List<Course> coursesList;
    private Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvTotalMarks;
        EditText etNumbers;
        MyViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tvTitle);
            etNumbers = view.findViewById(R.id.etNumbers);
            tvTotalMarks = view.findViewById(R.id.tvTotalMarks);
        }
    }


    public CourseResultAdapter(Context context, List<Course> coursesList) {
        this.coursesList = coursesList;
        this.context = context;
    }

    @Override
    public CourseResultAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.courses_result_list_row, parent, false);

        return new CourseResultAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CourseResultAdapter.MyViewHolder holder, final int position) {
        final Course course = coursesList.get(position);
        holder.tvTitle.setText(course.getTitle());
        holder.tvTotalMarks.setText(Long.toString(course.getTotalMarks()));
        holder.etNumbers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String number = holder.etNumbers.getText().toString();
                if (!number.isEmpty()) {
                    if (Integer.parseInt(number) <= course.getTotalMarks()){
                        int number1 = Integer.parseInt(number);
                        coursesList.get(position).setGainedMarks(number1);
                    }
                    else{
                        Toast.makeText(context, "Gained marks cannot be greater than total marks!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return coursesList.size();
    }
}
