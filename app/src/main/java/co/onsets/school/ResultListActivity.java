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

import co.onsets.school.Adapter.ResultAdapter;
import co.onsets.school.Model.Student;

public class ResultListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ResultAdapter mAdapter;
    private List<Student> studentList = new ArrayList<>();
    private DatabaseReference studentsReference;
    private String classId, classTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null){
            classId = intent.getStringExtra("id");
            classTitle = intent.getStringExtra("title");
        }

        setContentView(R.layout.activity_result_list);
        recyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        studentsReference = firebaseDatabase.getReference("students");
        studentList.clear();
        prepareData();
    }

    private void prepareData() {
        Query myTopPostsQuery = studentsReference.orderByKey();
        myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Student student = new Student();
                    student.setName(postSnapshot.child("name").getValue(String.class));
                    student.setGuardianName(postSnapshot.child("guardian_name").getValue(String.class));
                    student.setPhoneNumber(postSnapshot.child("phone_number").getValue(String.class));
                    student.setRollNumber(postSnapshot.child("roll_number").getValue(String.class));
                    student.setClassId(postSnapshot.child("class_id").getValue(String.class));
                    student.setId(postSnapshot.getKey());
                    studentList.add(student);
                }
                recyclerView = findViewById(R.id.recycler_view);
                mAdapter = new ResultAdapter(ResultListActivity.this, studentList, classTitle);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(ResultListActivity.this));
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                if (studentList.size() == 0){
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
