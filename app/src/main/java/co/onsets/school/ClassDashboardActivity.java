package co.onsets.school;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ClassDashboardActivity extends AppCompatActivity {
    private String id, title;
    private TextView tvClassTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_dashboard);
        Intent intent = getIntent();

        tvClassTitle = findViewById(R.id.tvClassTitle);

        if (intent != null){
            id = intent.getStringExtra("id");
            title = intent.getStringExtra("title");
            tvClassTitle.setText(title);
        }
    }

    public void coursesClicked(View view) {
        Intent i = new Intent(ClassDashboardActivity.this, CourseSelectionActivity.class);
        i.putExtra("id", id);
        i.putExtra("title", title);
        startActivity(i);
    }

    public void studentsClicked(View view) {
        Intent i = new Intent(ClassDashboardActivity.this, StudentsListActivity.class);
        i.putExtra("id", id);
        startActivity(i);
    }

    public void resultClicked(View view) {
        Intent i = new Intent(ClassDashboardActivity.this, ResultListActivity.class);
        i.putExtra("id", id);
        i.putExtra("title", title);
        startActivity(i);
    }

    public void attendanceClicked(View view) {
        Intent i = new Intent(ClassDashboardActivity.this, AttendanceActivity.class);
        i.putExtra("id", id);
        i.putExtra("title", title);
        startActivity(i);
    }

    public void editClassClicked(View view) {
        Intent i = new Intent(ClassDashboardActivity.this, EditClassActivity.class);
        i.putExtra("id", id);
        i.putExtra("title", title);
        startActivity(i);
    }
}
