package co.onsets.school;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import co.onsets.school.Model.ClassModel;
import co.onsets.school.Model.Student;

public class MainDashboardActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference classesReference, studentsReference;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseDatabase = FirebaseDatabase.getInstance();
        classesReference = firebaseDatabase.getReference("classes");
        studentsReference = firebaseDatabase.getReference("students");
    }

    public void coursesClicked(View view) {
        Intent i = new Intent(MainDashboardActivity.this, CoursesListActivity.class);
        startActivity(i);
    }

    public void classesClicked(View view) {
        Intent i = new Intent(MainDashboardActivity.this, ClassesActivity.class);
        startActivity(i);
    }

    public void addFeeClicked(View view) {
        Query getAllClasses = classesReference.orderByKey();
        getAllClasses.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    ClassModel classModel = postSnapshot.getValue(ClassModel.class);
                    classModel.setId(postSnapshot.getKey());
                    Query getUsersOfClass = studentsReference.orderByChild("class_id").equalTo(classModel.getId());
                    getUsersOfClass.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                            for(DataSnapshot childSnapshot : dataSnapshot1.getChildren()){
                                Student student = childSnapshot.getValue(Student.class);
                                student.setId(childSnapshot.getKey());
                                long dueFee = student.getFee() + student.getDue_fee();
                                studentsReference.child(childSnapshot.getKey()).child("due_fee").setValue(dueFee);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });
                }
                Toast.makeText(MainDashboardActivity.this, "Fee added successfully!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void feeSmsClicked(View view) {
        sendSMSMessage();
    }

    private void sendSMS() {
        Query getAllClasses = classesReference.orderByKey();
        getAllClasses.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    final ClassModel classModel = postSnapshot.getValue(ClassModel.class);
                    classModel.setId(postSnapshot.getKey());
                    Query getUsersOfClass = studentsReference.orderByChild("class_id").equalTo(classModel.getId());
                    getUsersOfClass.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                            for(DataSnapshot childSnapshot : dataSnapshot1.getChildren()){
                                Student student = childSnapshot.getValue(Student.class);
                                student.setId(childSnapshot.getKey());
                                if(student.getDue_fee() > 0){
                                    SmsManager smsManager = SmsManager.getDefault();
                                    String message = "الھادی فاؤنڈیشن اینڈ اکیڈمی\n";
                                    //message = message + "رول نمبر: " + student.getRoll_number() + "\n";
                                    message = message + student.getName() + "-" + classModel.getTitle() + "\n";
                                    //message = message + "والد کا نام: " + student.getGuardian_name() + "\n";
                                    message = message + "اپنی فیس ادا کریں.";
                                    smsManager.sendTextMessage(student.getPhone_number(), null, message, null, null);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });
                }
                Toast.makeText(MainDashboardActivity.this, "Messages sent successfully!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    protected void sendSMSMessage() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        } else {
            sendSMS();
        }
    }
}
