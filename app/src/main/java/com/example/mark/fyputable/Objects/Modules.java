package com.example.mark.fyputable.Objects;

import java.util.List;

public class Modules {

   private String ModuleCode;
   private String ModuleName;
   private List<String> RegisteredStaff;
   private List<String> RegisteredStudents;

    public Modules(String moduleCode, String moduleName, List<String> registeredStaff, List<String> registeredStudents) {
        ModuleCode = moduleCode;
        ModuleName = moduleName;
        RegisteredStaff = registeredStaff;
        RegisteredStudents = registeredStudents;
    }

    public Modules() {
    }

    public String getModuleCode() {
        return ModuleCode;
    }

    public void setModuleCode(String moduleCode) {
        ModuleCode = moduleCode;
    }

    public String getModuleName() {
        return ModuleName;
    }

    public void setModuleName(String moduleName) {
        ModuleName = moduleName;
    }

    public List<String> getRegisteredStaff() {
        return RegisteredStaff;
    }

    public void setRegisteredStaff(List<String> registeredStaff) {
        RegisteredStaff = registeredStaff;
    }

    public List<String> getRegisteredStudents() {
        return RegisteredStudents;
    }

    public void setRegisteredStudents(List<String> registeredStudents) {
        RegisteredStudents = registeredStudents;
    }
}