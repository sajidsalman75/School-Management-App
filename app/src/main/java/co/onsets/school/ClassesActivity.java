package co.onsets.school;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.onsets.school.Adapter.ClassListAdapter;
import co.onsets.school.Model.ClassModel;

public class ClassesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ClassListAdapter mAdapter;
    private List<ClassModel> classModelList = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference classesReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);
        recyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseDatabase = FirebaseDatabase.getInstance();
        classesReference = firebaseDatabase.getReference("classes");
        classModelList.clear();
        prepareMovieData();
    }

    private void prepareMovieData() {
        Query myTopPostsQuery = classesReference.orderByKey();
        myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    ClassModel classModel = new ClassModel();
                    classModel.setTitle(postSnapshot.child("title").getValue(String.class));
                    classModel.setId(postSnapshot.getKey());
                    classModelList.add(classModel);
                }
                recyclerView = findViewById(R.id.recycler_view);
                mAdapter = new ClassListAdapter(ClassesActivity.this, classModelList);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(ClassesActivity.this));
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                if (classModelList.size() == 0){
                    recyclerView.setVisibility(View.GONE);
                    //tvNoData.setVisibility(View.VISIBLE);
                }
                else{
                    recyclerView.setVisibility(View.VISIBLE);
                    //tvNoData.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        });
    }

    public void addClassClicked(View view) {
        Intent i = new Intent(ClassesActivity.this, AddClassActivity.class);
        startActivity(i);
    }
}
