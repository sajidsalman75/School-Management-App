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
            Query query = classesReference.orderByChild("title").equalTo(etTitle.getText().toString());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() == 0){
                        classesReference.child(key).child("title").setValue(etTitle.getText().toString()).addOnCompleteListener(AddClassActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(AddClassActivity.this, "New Class added successfully!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                    else{
                        Toast.makeText(AddClassActivity.this, "This Class has already been added!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else{
            Toast.makeText(AddClassActivity.this, "Kindly fill the field!", Toast.LENGTH_SHORT).show();
        }
    }
}
