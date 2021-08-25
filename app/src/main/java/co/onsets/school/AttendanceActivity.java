package co.onsets.school;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import co.onsets.school.Adapter.AttendanceAdapter;
import co.onsets.school.Adapter.StudentListAdapter;
import co.onsets.school.Model.Student;

public class AttendanceActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AttendanceAdapter mAdapter;
    private List<Student> studentList = new ArrayList<>();
    private DatabaseReference studentsReference;
    private String classid;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        Intent intent = getIntent();
        if (intent != null) {
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
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Student student = new Student();
                    student = postSnapshot.getValue(Student.class);
                    student.setId(postSnapshot.getKey());
                    studentList.add(student);
                }
                recyclerView = findViewById(R.id.recycler_view);
                mAdapter = new AttendanceAdapter(AttendanceActivity.this, studentList);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(AttendanceActivity.this));
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                if (studentList.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        });
    }

    public void doneClicked(View view) {
        sendSMSMessage();
    }

    public void sendSMS() {
        try {
            for (int i = 0; i < studentList.size(); i++) {
                if (studentList.get(i).getSelected()) {
//                    message = studentList.get(i).getName() + " is present in Al Hadi Academy today Dated: "
//                    + dateFormat.format(date);
                } else {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Date date = new Date();
                    String message = "Al-Hadi Foundation and academy" +  "\n"  + studentList.get(i).getName() + " is absent.\n" + "Dated: "
                            + dateFormat.format(date);
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(studentList.get(i).getPhone_number(), null, message, null, null);
                }
                if(i == studentList.size()-1){
                    Toast.makeText(getApplicationContext(), "Messages Sent",
                            Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMS();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }
}
