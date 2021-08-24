package co.onsets.school;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.onsets.school.Adapter.FeeAdapter;
import co.onsets.school.Adapter.StudentListAdapter;
import co.onsets.school.Model.Student;

public class FeeListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FeeAdapter mAdapter;
    private List<Student> studentList = new ArrayList<>();
    private DatabaseReference studentsReference;
    private String classid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_list);

        Intent intent = getIntent();
        if (intent != null){
            classid = intent.getStringExtra("id");
        }
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
        Query myTopPostsQuery = studentsReference.orderByChild("class_id").equalTo(classid);
        myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Student student = postSnapshot.getValue(Student.class);
                    student.setId(postSnapshot.getKey());
                    studentList.add(student);
                }
                recyclerView = findViewById(R.id.recycler_view);
                mAdapter = new FeeAdapter(FeeListActivity.this, studentList);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(FeeListActivity.this));
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

    public void doneClicked(View view){
        for(int i = 0 ; i < studentList.size() ; i++){
            if(studentList.get(i).getSelected()){
                studentsReference.child(studentList.get(i).getId()).child("due_fee").setValue(0);
            }
        }
        Toast.makeText(FeeListActivity.this, "Fee submitted successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
