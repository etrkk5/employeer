package projects.etrkk5.employeer.Profiles;

public class employeeAd {
    private String title;
    private String location;
    private String companyName;
    private String docsRef;

    public employeeAd(String docsRef, String title, String location, String companyName){
        this.docsRef = docsRef;
        this.title = title;
        this.location = location;
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDocsRef() {
        return docsRef;
    }

    public void setDocsRef(String docsRef) {
        this.docsRef = docsRef;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
