package projects.etrkk5.employeer.Profiles;

/**
 * Created by EsrefTurkok on 24.04.2018.
 */

public class Item {
    private String name;
    private String location;
    private String docsRef;

    public Item(String name, String location, String docsRef) {
        this.name = name;
        this.location = location;
        this.docsRef = docsRef;
    }

    public String getDocsRef() {
        return docsRef;
    }

    public void setDocsRef(String docsRef) {
        this.docsRef = docsRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
