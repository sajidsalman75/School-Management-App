package co.onsets.school;

import androidx.annotation.NonNull;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

import co.onsets.school.Adapter.CourseListAdapter;
import co.onsets.school.Adapter.CourseResultAdapter;
import co.onsets.school.Adapter.ResultAdapter;
import co.onsets.school.Model.Course;
import co.onsets.school.Model.Student;

public class ResultActivity extends AppCompatActivity {
    private String id, name, guardianName, phoneNumber, rollNumber, classId, classTitle;
    private TextView tvName, tvRollNumber, tvGuardianName, tvClassTitle, tvNoteLimit;
    private EditText etNote;
    RecyclerView rvCourses;
    private CourseResultAdapter mAdapter;
    private List<Course> courseList = new ArrayList<>();
    private DatabaseReference coursesReference, classCoursesReference;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();

        tvName = findViewById(R.id.tvTitle);
        tvGuardianName = findViewById(R.id.tvGuardianName);
        tvRollNumber = findViewById(R.id.tvRollNumber);
        rvCourses = findViewById(R.id.rv_courses);
        tvClassTitle = findViewById(R.id.tvClassTitle);
        tvNoteLimit = findViewById(R.id.tvNoteLimit);
        etNote = findViewById(R.id.etNote);
        tvNoteLimit.setText("0/145");

        etNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvNoteLimit.setText(String.valueOf(etNote.length()) + "/145");
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
        if (intent != null){
            id = intent.getStringExtra("id");
            name = intent.getStringExtra("name");
            tvName.setText(name);

            phoneNumber = intent.getStringExtra("phoneNumber");
            rollNumber = intent.getStringExtra("rollNumber");
            tvRollNumber.setText(rollNumber);

            guardianName = intent.getStringExtra("guardianName");
            tvGuardianName.setText(guardianName);

            classId = intent.getStringExtra("classId");
            classTitle = intent.getStringExtra("classTitle");
            tvClassTitle.setText(classTitle);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        coursesReference = firebaseDatabase.getReference("courses");
        classCoursesReference = firebaseDatabase.getReference("class_courses");
        courseList.clear();
        prepareData();
    }

    private void prepareData() {
        Query myTopPostsQuery = classCoursesReference.orderByChild("class_id").equalTo(classId);
        myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Query coursesQuery = coursesReference.orderByKey().equalTo(postSnapshot.child("course_id").getValue(String.class));
                    coursesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                Course course = new Course();
                                course.setTitle(postSnapshot.child("title").getValue(String.class));
                                course.setId(postSnapshot.getKey());
                                course.setTotalMarks(postSnapshot.child("total_marks").getValue(Long.class));
                                courseList.add(course);
                            }
                            mAdapter = new CourseResultAdapter(ResultActivity.this, courseList);
                            //rvCourses.setHasFixedSize(true);
                            rvCourses.setLayoutManager(new LinearLayoutManager(ResultActivity.this));
                            rvCourses.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                            if (courseList.size() == 0){
                                rvCourses.setVisibility(View.GONE);
                                //tvNoData.setVisibility(View.VISIBLE);
                            }
                            else{
                                rvCourses.setVisibility(View.VISIBLE);
                                //tvNoData.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        });
    }

    public void sendSMS() {
        try {
            if (etNote.length() <= 145){
                if(courseList.size() > 0){
                    boolean isValidMarks = true;
                    String message = "Al Hadi Academy\n";
                    String message1 = "";
                    message = message + "Roll Number: " + rollNumber + "\n";
                    message = message + "Name: " + name + "\n";
                    message = message + "S/D/O: " + guardianName + "\n";
                    message = message + "Class: " + classTitle + "\n";
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    Date date = new Date();
                    long totalGainedMarks = 0, totalMarks = 0;
                    for (int i = 0 ; i < courseList.size() ; i++){
                        if (courseList.get(i).getGainedMarks()!= -1){
                            if (message.length() <= 120){
                                message = message + courseList.get(i).getTitle() + " " + courseList.get(i).getGainedMarks()
                                        + "/" + courseList.get(i).getTotalMarks() + "\n";
                            }
                            else{
                                message1 = message1 + courseList.get(i).getTitle() + " " + courseList.get(i).getGainedMarks()
                                        + "/" + courseList.get(i).getTotalMarks() + "\n";
                            }
                            totalGainedMarks = totalGainedMarks + courseList.get(i).getGainedMarks();
                            totalMarks = totalMarks + courseList.get(i).getTotalMarks();
                        }
                        if (courseList.get(i).getGainedMarks() > courseList.get(i).getTotalMarks()){
                            isValidMarks = false;
                        }
                    }
                    if (isValidMarks && courseList.size() > 0){
                        if (message.length() <= 120 && message1.isEmpty()){
                            double percentage = ((double) totalGainedMarks/totalMarks) * 100.0;
                            message = message + "Total Marks: " + totalGainedMarks + "/" + totalMarks + "(" +
                                     percentage + ")";
                        }
                        else{
                            double percentage = (Double.longBitsToDouble(totalGainedMarks)/Double.longBitsToDouble(totalMarks)) * 100.0;
                            long percentage1 = Math.round(percentage);
                            message1 = message1 + "Total Marks: " + totalGainedMarks + "/" + totalMarks + "(" +
                                    percentage1 + ")";
                        }
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                        if (!message1.isEmpty()){
                            smsManager.sendTextMessage(phoneNumber, null, message1, null, null);
                        }
                        if(!etNote.getText().toString().isEmpty()){
                            smsManager.sendTextMessage(phoneNumber, null, "Note:\n" + etNote.getText().toString(), null, null);
                        }
                        Toast.makeText(getApplicationContext(), "Messages Sent",
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Marks are not valid!",
                                Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "No courses found!", Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), getString(R.string.note_length_warning), Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage(),
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
        }
        else{
            sendSMS();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMS();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }

    public void doneClicked(View view) {
        sendSMSMessage();
    }
}
