package co.onsets.school;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import co.onsets.school.Adapter.CourseListAdapter;
import co.onsets.school.Model.Course;

public class AddCourseActivity extends AppCompatActivity {
    private EditText etTitle, etTotalMarks;
    private DatabaseReference coursesReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        etTitle = findViewById(R.id.etTitle);
        etTotalMarks = findViewById(R.id.etTotalMarks);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        coursesReference = firebaseDatabase.getReference("courses");
    }

    public void addCourseClicked(View view) {
        if (!etTitle.getText().toString().isEmpty() && !etTotalMarks.getText().toString().isEmpty()){
            Query myTopPostsQuery = coursesReference.orderByChild("title").equalTo(etTitle.getText().toString());
            myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() == 0){
                        final String key = coursesReference.push().getKey();
                        coursesReference.child(key).child("total_marks").setValue(Long.parseLong(etTotalMarks.getText().toString()));
                        coursesReference.child(key).child("title").setValue(etTitle.getText().toString()).addOnCompleteListener(AddCourseActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(AddCourseActivity.this, "New course added successfully!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                    else{
                        Toast.makeText(AddCourseActivity.this, "This course is has already been added!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                }
            });
        }
        else{
            Toast.makeText(AddCourseActivity.this, "Kindly fill the fields!", Toast.LENGTH_SHORT).show();
        }
    }
}
