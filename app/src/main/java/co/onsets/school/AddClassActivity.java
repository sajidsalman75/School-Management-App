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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddClassActivity extends AppCompatActivity {
    private EditText etTitle;
    private DatabaseReference classesReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        etTitle = findViewById(R.id.etTitle);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        classesReference = firebaseDatabase.getReference("classes");
    }

    public void addClassClicked(View view) {
        if (!etTitle.getText().toString().isEmpty()){
            final String key = classesReference.push().getKey();
            classesReference.child(key).child("title").setValue(etTitle.getText().toString()).addOnCompleteListener(AddClassActivity.this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(AddClassActivity.this, "New Class added successfully!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(AddClassActivity.this, ClassesActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        }
        else{
            Toast.makeText(AddClassActivity.this, "Kindly fill the field!", Toast.LENGTH_SHORT).show();
        }
    }
}
