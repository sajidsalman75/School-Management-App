package co.onsets.school;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import co.onsets.school.Model.Course;

public class EditCourseActivity extends AppCompatActivity {
    private EditText etTitle, etTotalMarks;
    private String id;
    private DatabaseReference coursesReference, classCourseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        Intent intent = getIntent();
        Long totalMarks;
        String title;
        if(intent != null){
            title = intent.getStringExtra("title");
            totalMarks = intent.getLongExtra("totalMarks", 0);
            id = intent.getStringExtra("id");
        }
        else{
            title = "";
            totalMarks = null;
            id = "";
        }

        etTitle = findViewById(R.id.etTitle);
        etTotalMarks = findViewById(R.id.etTotalMarks);

        etTitle.setText(title);
        etTotalMarks.setText(String.valueOf(totalMarks));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        coursesReference = firebaseDatabase.getReference("courses");
        classCourseReference = firebaseDatabase.getReference("class_courses");
    }

    public void editCourseClicked(View view) {
        if (!etTitle.getText().toString().isEmpty() && !etTotalMarks.getText().toString().isEmpty()){
            Query myTopPostsQuery = coursesReference.orderByChild("title").equalTo(etTitle.getText().toString());
            myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0 || dataSnapshot.getValue() == null){
                        coursesReference.child(id).child("total_marks").setValue(Long.parseLong(etTotalMarks.getText().toString()));
                        coursesReference.child(id).child("title").setValue(etTitle.getText().toString()).addOnCompleteListener(EditCourseActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(EditCourseActivity.this, "Course updated successfully!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                    else{
                        Toast.makeText(EditCourseActivity.this, "This course is has already been added!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                }
            });
        }
        else{
            Toast.makeText(EditCourseActivity.this, "Kindly fill the fields!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteCourseClicked(View view) {
        coursesReference.child(id).removeValue();
        Query query = classCourseReference.orderByChild("course_id").equalTo(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    postSnapshot.getRef().removeValue().addOnCompleteListener(EditCourseActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(EditCourseActivity.this, "Course deleted successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
