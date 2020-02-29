package co.onsets.school.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.onsets.school.EditCourseActivity;
import co.onsets.school.Model.Course;
import co.onsets.school.R;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.MyViewHolder> {
    private List<Course> coursesList;
    private Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvTotalMarks;
        RelativeLayout rr;
        MyViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tvTitle);
            rr = view.findViewById(R.id.rr);
            tvTotalMarks = view.findViewById(R.id.tvTotalMarks);
        }
    }


    public CourseListAdapter(Context context, List<Course> coursesList) {
        this.coursesList = coursesList;
        this.context = context;
    }

    @Override
    public CourseListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.courses_list_row, parent, false);

        return new CourseListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CourseListAdapter.MyViewHolder holder, int position) {
        final Course course = coursesList.get(position);
        holder.tvTitle.setText(course.getTitle());
        holder.tvTotalMarks.setText(Long.toString(course.getTotalMarks()));
        holder.rr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, EditCourseActivity.class);
                i.putExtra("id", course.getId());
                i.putExtra("title", holder.tvTitle.getText().toString());
                i.putExtra("totalMarks", course.getTotalMarks());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return coursesList.size();
    }
}
