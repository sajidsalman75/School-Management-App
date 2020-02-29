package co.onsets.school.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.onsets.school.Model.Student;
import co.onsets.school.R;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.MyViewHolder> {
    private List<Student> studentList;
    private Context context;
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvGuardianName, tvRollNumber;
        ImageView ivDone;
        RelativeLayout rr;
        MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvGuardianName = view.findViewById(R.id.tvGuardianName);
            tvRollNumber = view.findViewById(R.id.tvRollNumber);
            ivDone = view.findViewById(R.id.ivDone);
            rr = view.findViewById(R.id.rr);
        }
    }


    public AttendanceAdapter(Context context, List<Student> studentList) {
        this.studentList = studentList;
        this.context = context;
    }

    @Override
    public AttendanceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.students_list_row, parent, false);

        return new AttendanceAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AttendanceAdapter.MyViewHolder holder, int position) {
        final Student student = studentList.get(position);
        holder.tvName.setText(student.getName());
        holder.tvRollNumber.setText(student.getRollNumber());
        holder.tvGuardianName.setText(student.getGuardianName());
        holder.rr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                student.setSelected(!student.getSelected());
                if (student.getSelected()){
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
        return studentList.size();
    }
}
