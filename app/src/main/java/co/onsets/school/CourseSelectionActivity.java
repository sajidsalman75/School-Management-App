package co.onsets.school;

import androidx.annotation.NonNull;
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

import co.onsets.school.Adapter.CourseListAdapter;
import co.onsets.school.Adapter.CourseSelectionAdapter;
import co.onsets.school.Model.Course;

public class CourseSelectionActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CourseSelectionAdapter mAdapter;
    private List<Course> courseList = new ArrayList<>();
    private List<String> selectedCourses = new ArrayList<>();
    private DatabaseReference cousesReference, classCoursesReference;
    private String classid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_selection);
        recyclerView = findViewById(R.id.recycler_view);
        Intent intent = getIntent();
        if (intent != null){
            classid = intent.getStringExtra("id");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        cousesReference = firebaseDatabase.getReference("courses");
        classCoursesReference = firebaseDatabase.getReference("class_courses");
        courseList.clear();
        prepareData();
    }

    public void doneClicked(View view) {
        for(int i = 0 ; i < courseList.size() ; i++){
            if (courseList.get(i).getSelected()){
                final String key = classCoursesReference.push().getKey();
                classCoursesReference.child(key).child("class_id").setValue(classid);
                classCoursesReference.child(key).child("course_id").setValue(courseList.get(i).getId());
            }
            else{
                Query query = classCoursesReference.orderByChild("course_id").equalTo(courseList.get(i).getId());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                            postSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    private void prepareData() {
        Query query = classCoursesReference.orderByChild("class_id").equalTo(classid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    String courseId = postSnapshot.child("course_id").getValue(String.class);
                    selectedCourses.add(courseId);
                }
                Query myTopPostsQuery = cousesReference.orderByKey();
                myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            Course course = new Course();
                            course.setTitle(postSnapshot.child("title").getValue(String.class));
                            course.setId(postSnapshot.getKey());
                            course.setTotalMarks(postSnapshot.child("total_marks").getValue(Long.class));
                            if (selectedCourses.contains(course.getId())){
                                course.setSelected(true);
                            }
                            courseList.add(course);
                        }
                        recyclerView = findViewById(R.id.recycler_view);
                        mAdapter = new CourseSelectionAdapter(CourseSelectionActivity.this, courseList);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(CourseSelectionActivity.this));
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
