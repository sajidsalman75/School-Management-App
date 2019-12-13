package co.onsets.school.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.onsets.school.Model.ClassModel;
import co.onsets.school.R;

public class ClassListAdapter extends RecyclerView.Adapter<ClassListAdapter.MyViewHolder> {
    private List<ClassModel> classesList;
    private Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public LinearLayout llItem;
        public MyViewHolder(View view) {
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
                Toast.makeText(context, classModel.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return classesList.size();
    }
}
