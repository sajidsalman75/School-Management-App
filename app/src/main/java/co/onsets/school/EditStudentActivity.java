package co.onsets.school;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditStudentActivity extends AppCompatActivity {
    private DatabaseReference studentsReference;
    private EditText etName, etGuardianName, etRollNumber, etPhoneNumber, etFee;
    private String studentId, rollNumber, name, guardianName, phoneNumber;
    private long fee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        Intent intent = getIntent();
        if(intent != null){
            studentId = intent.getStringExtra("id");
            rollNumber = intent.getStringExtra("rollNumber");
            name = intent.getStringExtra("name");
            guardianName = intent.getStringExtra("guardianName");
            phoneNumber = intent.getStringExtra("phoneNumber");
            fee = intent.getLongExtra("fee", 0);
        }
        else{
            studentId = rollNumber = name = guardianName = phoneNumber = "";
            fee = 0;
        }

        etName = findViewById(R.id.etName);
        etName.setText(name);
        etGuardianName = findViewById(R.id.etGuardianName);
        etGuardianName.setText(guardianName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etPhoneNumber.setText(phoneNumber);
        etRollNumber = findViewById(R.id.etRollNumber);
        etRollNumber.setText(rollNumber);
        etFee = findViewById(R.id.etFee);
        etFee.setText(String.valueOf(fee));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        studentsReference = firebaseDatabase.getReference("students");
    }

    public void editStudentClicked(View view) {
        if (etName.getText().toString().isEmpty() && etRollNumber.getText().toString().isEmpty() &&
                etPhoneNumber.getText().toString().isEmpty() && etGuardianName.getText().toString().isEmpty() &&
        etFee.getText().toString().isEmpty()){
            Toast.makeText(EditStudentActivity.this, "Kindly fill all the fields!", Toast.LENGTH_SHORT).show();
        }
        else{
            studentsReference.child(studentId).child("name").setValue(etName.getText().toString());
            studentsReference.child(studentId).child("phone_number").setValue(etPhoneNumber.getText().toString());
            studentsReference.child(studentId).child("roll_number").setValue(etRollNumber.getText().toString());
            studentsReference.child(studentId).child("guardian_name").setValue(etGuardianName.getText().toString());
            studentsReference.child(studentId).child("fee").setValue(Long.parseLong(etFee.getText().toString()));
            Toast.makeText(EditStudentActivity.this, "Student updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void deleteStudentClicked(View view) {
        studentsReference.child(studentId).removeValue();
        Toast.makeText(EditStudentActivity.this, "Student deleted successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}
