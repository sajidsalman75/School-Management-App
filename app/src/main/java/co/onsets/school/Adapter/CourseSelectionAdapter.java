package co.onsets.school.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.onsets.school.Model.Course;
import co.onsets.school.R;

public class CourseSelectionAdapter extends RecyclerView.Adapter<CourseSelectionAdapter.MyViewHolder> {
    private List<Course> coursesList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvTotalMarks;
        ImageView ivDone;
        RelativeLayout rr;
        MyViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvTotalMarks = view.findViewById(R.id.tvTotalMarks);
            ivDone = view.findViewById(R.id.ivDone);
            rr = view.findViewById(R.id.rr);
        }
    }


    public CourseSelectionAdapter(Context context, List<Course> coursesList) {
        this.coursesList = coursesList;
    }

    @Override
    public CourseSelectionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.courses_list_row, parent, false);

        return new CourseSelectionAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CourseSelectionAdapter.MyViewHolder holder, int position) {
        final Course course = coursesList.get(position);
        holder.tvTitle.setText(course.getTitle());
        holder.tvTotalMarks.setText(Long.toString(course.getTotalMarks()));
        if (course.getSelected()){
            holder.ivDone.setVisibility(View.VISIBLE);
        }
        else{
            holder.ivDone.setVisibility(View.GONE);
        }
        holder.rr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                course.setSelected(!course.getSelected());
                if (course.getSelected()){
                    holder.ivDone.setVisibility(View.VISIBLE);
                }
                else{
                    holder.ivDone.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return coursesList.size();
    }
}
