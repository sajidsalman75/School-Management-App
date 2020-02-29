package co.onsets.school.Model;

public class Student {
    private String id, name, rollNumber, phoneNumber, guardianName, classId;
    private Boolean isSelected;

    public Student(String id, String name, String rollNumber, String phoneNumber, String guardianName, String classId) {
        this.id = id;
        this.name = name;
        this.rollNumber = rollNumber;
        this.phoneNumber = phoneNumber;
        this.guardianName = guardianName;
        this.isSelected = false;
        this.classId = classId;
    }

    public Student() {
        this.isSelected = false;
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

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }
}
