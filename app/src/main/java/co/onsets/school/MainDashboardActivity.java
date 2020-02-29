package co.onsets.school;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);
    }

    public void coursesClicked(View view) {
        Intent i = new Intent(MainDashboardActivity.this, CoursesListActivity.class);
        startActivity(i);
    }

    public void classesClicked(View view) {
        Intent i = new Intent(MainDashboardActivity.this, ClassesActivity.class);
        startActivity(i);
    }
}
