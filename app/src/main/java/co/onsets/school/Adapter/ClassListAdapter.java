package co.onsets.school.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.onsets.school.ClassDashboardActivity;
import co.onsets.school.Model.ClassModel;
import co.onsets.school.R;

public class ClassListAdapter extends RecyclerView.Adapter<ClassListAdapter.MyViewHolder> {
    private List<ClassModel> classesList;
    private Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        LinearLayout llItem;
        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.tvClassTitle);
            llItem = view.findViewById(R.id.llItem);
        }
    }

    public ClassListAdapter(Context context, List<ClassModel> classesList) {
        this.classesList = classesList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.classes_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ClassModel classModel = classesList.get(position);
        holder.title.setText(classModel.getTitle());
        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ClassDashboardActivity.class);
                i.putExtra("id", classModel.getId());
                i.putExtra("title", classModel.getTitle());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return classesList.size();
    }
}
