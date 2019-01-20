package com.example.mark.fyputable;

public class ModuleReg {
    private String ModuleID;
    private Long RegistrationID;
    private String StudentID;


    public ModuleReg() {
    }

    public ModuleReg(String mID, Long registrationID, String studentID) {
        ModuleID = mID;
        RegistrationID = registrationID;
        StudentID = studentID;
    }

    public String getModuleID() {
        return ModuleID;
    }

    public void setModuleID(String mID) {
        ModuleID = mID;
    }

    public Long getRegistrationID() {
        return RegistrationID;
    }

    public void setRegistrationID(Long registrationID) {
        RegistrationID = registrationID;
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
    }
}
