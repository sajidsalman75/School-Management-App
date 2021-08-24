package co.onsets.school.Model;

import androidx.annotation.Keep;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
@Keep
public class Course {
    private String id, title;
    private Boolean isSelected, isGainedValidated;
    private long gainedMarks, totalMarks;

    public Course(String id, String title) {
        this.id = id;
        this.title = title;
        this.isSelected = false;
        this.gainedMarks = -1;
        this.totalMarks = -1;
        this.isGainedValidated = false;
    }

    public Course() {
        this.isSelected = false;
        this.gainedMarks = -1;
        this.totalMarks = -1;
        this.isGainedValidated = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public long getGainedMarks() {
        return gainedMarks;
    }

    public void setGainedMarks(long gainedMarks) {
        this.gainedMarks = gainedMarks;
    }

    public long getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(long totalMarks) {
        this.totalMarks = totalMarks;
    }

    public Boolean getGainedValidated() {
        return isGainedValidated;
    }

    public void setGainedValidated(Boolean gainedValidated) {
        isGainedValidated = gainedValidated;
    }
}
