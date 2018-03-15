package projects.etrkk5.employeer.Profiles;

/**
 * Created by EsrefTurkok on 16.02.2018.
 */

public class employee {
    private String employeeName;
    private String employeeSurname;
    private String employeeEmail;
    private String employeePhone;
    private String employeeProfession;
    private String employeeLocation;
    private String employeeId;
    private String employeeExperience;
    private String employeeAge;
    private String userType;

    public employee(){}


    public employee(String employeeName, String employeeSurname, String employeeEmail){
        this.employeeName = employeeName;
        this.employeeSurname = employeeSurname;
        this.employeeEmail = employeeEmail;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeSurname() {
        return employeeSurname;
    }

    public void setEmployeeSurname(String employeeSurname) { this.employeeSurname = employeeSurname; }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String getEmployeePhone() {
        return employeePhone;
    }

    public void setEmployeePhone(String employeePhone) {
        this.employeePhone = employeePhone;
    }

    public String getEmployeeProfession() {
        return employeeProfession;
    }

    public void setEmployeeProfession(String employeeProfession) { this.employeeProfession = employeeProfession; }

    public String getEmployeeLocation() {
        return employeeLocation;
    }

    public void setEmployeeLocation(String employeeLocation) { this.employeeLocation = employeeLocation; }

    public String getEmployeeId() { return employeeId; }

    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getUserType() { return userType; }

    public void setUserType(String userType) { this.userType = userType; }

    public String getEmployeeExperience() { return employeeExperience; }

    public void setEmployeeExperience(String employeeExperience) { this.employeeExperience = employeeExperience; }

    public String getEmployeeAge() { return employeeAge; }

    public void setEmployeeAge(String employeeAge) { this.employeeAge = employeeAge; }
}
