package co.onsets.school.Model;

import androidx.annotation.Keep;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
@Keep
public class Student {
    private String id, name, roll_number, phone_number, guardian_name, class_id;
    private Boolean isSelected;
    private long fee, due_fee;

    public Student(String id, String name, String rollNumber, String phoneNumber, String guardianName, String classId, long fee) {
        this.id = id;
        this.name = name;
        this.roll_number = rollNumber;
        this.phone_number = phoneNumber;
        this.guardian_name = guardianName;
        this.isSelected = false;
        this.class_id = classId;
        this.fee = fee;
    }

    public Student() {
        this.isSelected = false;
        this.fee = 0;
        this.due_fee = 0;
    }

    public long getDue_fee() {
        return due_fee;
    }

    public void setDue_fee(long due_fee) {
        this.due_fee = due_fee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoll_number() {
        return roll_number;
    }

    public void setRoll_number(String roll_number) {
        this.roll_number = roll_number;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getGuardian_name() {
        return guardian_name;
    }

    public void setGuardian_name(String guardian_name) {
        this.guardian_name = guardian_name;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public long getFee() {
        return fee;
    }

    public void setFee(long fee) {
        this.fee = fee;
    }
}
