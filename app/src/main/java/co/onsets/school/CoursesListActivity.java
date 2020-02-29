package co.onsets.school;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.onsets.school.Adapter.ClassListAdapter;
import co.onsets.school.Adapter.CourseListAdapter;
import co.onsets.school.Model.ClassModel;
import co.onsets.school.Model.Course;

public class CoursesListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CourseListAdapter mAdapter;
    private List<Course> courseList = new ArrayList<>();
    private DatabaseReference cousesReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_list);
        recyclerView = findViewById(R.id.recycler_view);
    }

    public void addCourseClicked(View view) {
        Intent i = new Intent(CoursesListActivity.this, AddCourseActivity.class);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        cousesReference = firebaseDatabase.getReference("courses");
        courseList.clear();
        prepareData();
    }

    private void prepareData() {
        Query myTopPostsQuery = cousesReference.orderByKey();
        myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Course course = new Course();
                    course.setTitle(postSnapshot.child("title").getValue(String.class));
                    course.setId(postSnapshot.getKey());
                    course.setTotalMarks(postSnapshot.child("total_marks").getValue(Long.class));
                    courseList.add(course);
                }
                recyclerView = findViewById(R.id.recycler_view);
                mAdapter = new CourseListAdapter(CoursesListActivity.this, courseList);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(CoursesListActivity.this));
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                if (courseList.size() == 0){
                    recyclerView.setVisibility(View.GONE);
                    //tvNoData.setVisibility(View.VISIBLE);
                }
                else{
                    recyclerView.setVisibility(View.VISIBLE);
                    //tvNoData.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        });
    }
}
