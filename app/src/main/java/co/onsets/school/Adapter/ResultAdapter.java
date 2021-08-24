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

import co.onsets.school.Model.Student;
import co.onsets.school.R;
import co.onsets.school.ResultActivity;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.MyViewHolder> {
    private List<Student> studentList;
    private Context context;
    private String classTitle;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvGuardianName, tvRollNumber;
        public RelativeLayout rr;
        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvGuardianName = view.findViewById(R.id.tvGuardianName);
            tvRollNumber = view.findViewById(R.id.tvRollNumber);
            rr = view.findViewById(R.id.rr);
        }
    }


    public ResultAdapter(Context context, List<Student> studentList, String classTitle) {
        this.studentList = studentList;
        this.context = context;
        this.classTitle = classTitle;
    }

    @Override
    public ResultAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.students_list_row, parent, false);

        return new ResultAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ResultAdapter.MyViewHolder holder, int position) {
        final Student student = studentList.get(position);
        holder.tvName.setText(student.getName());
        holder.tvRollNumber.setText(student.getRoll_number());
        holder.tvGuardianName.setText(student.getGuardian_name());
        holder.rr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ResultActivity.class);
                i.putExtra("id", student.getId());
                i.putExtra("name", student.getName());
                i.putExtra("rollNumber", student.getRoll_number());
                i.putExtra("guardianName", student.getGuardian_name());
                i.putExtra("classTitle", classTitle);
                i.putExtra("phoneNumber", student.getPhone_number());
                i.putExtra("classId", student.getClass_id());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }
}
