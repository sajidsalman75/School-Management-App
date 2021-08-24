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

import co.onsets.school.Model.ClassModel;

public class EditClassActivity extends AppCompatActivity {
    private EditText etTitle, etFee;
    private String id, title;
    private long fee;
    private DatabaseReference classesReference, classCoursesReference, studentsReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class);

        Intent intent = getIntent();
        if (intent != null){
            id = intent.getStringExtra("id");
            title = intent.getStringExtra("title");
            fee = intent.getLongExtra("fee", 0);
        }
        else{
            id = "";
            title = "";
            fee = 0;
        }

        etTitle = findViewById(R.id.etTitle);
        etFee = findViewById(R.id.etFee);
        etFee.setText(String.valueOf(fee));
        etTitle.setText(title);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        classesReference = firebaseDatabase.getReference("classes");
        classCoursesReference = firebaseDatabase.getReference("class_courses");
        studentsReference = firebaseDatabase.getReference("students");
    }

    public void editClassClicked(View view) {
        if (!etTitle.getText().toString().isEmpty()){
            Query query = classesReference.orderByChild("title").equalTo(etTitle.getText().toString());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String checkId = "";
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        checkId = postSnapshot.getKey();
                    }
                    if (dataSnapshot.getChildrenCount() == 0 || id.contentEquals(checkId)){
                        ClassModel classModel = new ClassModel(null, etTitle.getText().toString(), Long.parseLong(etFee.getText().toString()));
                        classesReference.child(id).setValue(classModel).addOnCompleteListener(EditClassActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(EditClassActivity.this, "Class updated successfully!", Toast.LENGTH_SHORT).show();
                                Query getStudentOfClass = studentsReference.orderByChild("class_id").equalTo(id);
                                getStudentOfClass.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                        for(DataSnapshot childSnapshot : dataSnapshot1.getChildren()){
                                            studentsReference.child(childSnapshot.getKey()).child("fee").setValue(Long.parseLong(etFee.getText().toString()));
                                        }
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                                });
                            }
                        });
                    }
                    else{
                        Toast.makeText(EditClassActivity.this, "This Class has already been added!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else{
            Toast.makeText(EditClassActivity.this, "Kindly fill the field!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteClassClicked(View view) {
        classesReference.child(id).removeValue();
        classCoursesReference.orderByChild("class_id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    postSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        studentsReference.orderByChild("class_id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    postSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Toast.makeText(EditClassActivity.this, "Class deleted successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
