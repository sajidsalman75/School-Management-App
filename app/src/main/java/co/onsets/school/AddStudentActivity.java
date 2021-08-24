package co.onsets.school;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddStudentActivity extends AppCompatActivity {
    private DatabaseReference studentsReference;
    private EditText etName, etGuardianName, etRollNumber, etPhoneNumber;
    private String classId;
    private long fee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        Intent intent = getIntent();
        if(intent != null){
            classId = intent.getStringExtra("id");
            fee = intent.getLongExtra("fee", 0);
        }

        etName = findViewById(R.id.etName);
        etGuardianName = findViewById(R.id.etGuardianName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etRollNumber = findViewById(R.id.etRollNumber);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        studentsReference = firebaseDatabase.getReference("students");
    }

    public void addStudentClicked(View view) {
        if (etName.getText().toString().isEmpty() && etRollNumber.getText().toString().isEmpty() &&
                etPhoneNumber.getText().toString().isEmpty() && etGuardianName.getText().toString().isEmpty()){
            Toast.makeText(AddStudentActivity.this, "Kindly fill all the fields!", Toast.LENGTH_SHORT).show();
        }
        else{
            final String key = studentsReference.push().getKey();
            studentsReference.child(key).child("name").setValue(etName.getText().toString());
            studentsReference.child(key).child("phone_number").setValue(etPhoneNumber.getText().toString());
            studentsReference.child(key).child("roll_number").setValue(etRollNumber.getText().toString());
            studentsReference.child(key).child("guardian_name").setValue(etGuardianName.getText().toString());
            studentsReference.child(key).child("class_id").setValue(classId);
            studentsReference.child(key).child("fee").setValue(fee);
            Toast.makeText(AddStudentActivity.this, "New student added successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
