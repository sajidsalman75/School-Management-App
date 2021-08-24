package co.onsets.school.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.onsets.school.EditStudentActivity;
import co.onsets.school.Model.Student;
import co.onsets.school.R;

public class FeeAdapter extends RecyclerView.Adapter<FeeAdapter.MyViewHolder> {
    private List<Student> studentList;
    private Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvGuardianName, tvRollNumber, tvFee;
        public RelativeLayout rr;
        ImageView ivDone;
        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvGuardianName = view.findViewById(R.id.tvGuardianName);
            tvRollNumber = view.findViewById(R.id.tvRollNumber);
            tvFee = view.findViewById(R.id.tvFee);
            ivDone = view.findViewById(R.id.ivDone);
            rr = view.findViewById(R.id.rr);
        }
    }


    public FeeAdapter(Context context, List<Student> studentList) {
        this.studentList = studentList;
        this.context = context;
    }

    @Override
    public FeeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.students_list_row, parent, false);

        return new FeeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FeeAdapter.MyViewHolder holder, int position) {
        final Student student = studentList.get(position);
        holder.tvName.setText(student.getName());
        holder.tvRollNumber.setText(student.getRoll_number());
        holder.tvGuardianName.setText(student.getGuardian_name());
        holder.tvFee.setText(String.valueOf(student.getDue_fee()));
        holder.tvFee.setVisibility(View.VISIBLE);
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
