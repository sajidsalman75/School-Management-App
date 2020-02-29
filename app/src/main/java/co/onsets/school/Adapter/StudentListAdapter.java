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

import co.onsets.school.ClassDashboardActivity;
import co.onsets.school.EditStudentActivity;
import co.onsets.school.Model.Student;
import co.onsets.school.R;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.MyViewHolder> {
    private List<Student> studentList;
    private Context context;
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


    public StudentListAdapter(Context context, List<Student> studentList) {
        this.studentList = studentList;
        this.context = context;
    }

    @Override
    public StudentListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.students_list_row, parent, false);

        return new StudentListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StudentListAdapter.MyViewHolder holder, int position) {
        final Student student = studentList.get(position);
        holder.tvName.setText(student.getName());
        holder.tvRollNumber.setText(student.getRollNumber());
        holder.tvGuardianName.setText(student.getGuardianName());
        holder.rr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, EditStudentActivity.class);
                i.putExtra("id", student.getId());
                i.putExtra("name", student.getName());
                i.putExtra("rollNumber", student.getRollNumber());
                i.putExtra("guardianName", student.getGuardianName());
                i.putExtra("phoneNumber", student.getPhoneNumber());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }
}