package projects.etrkk5.employeer.Profiles;

/**
 * Created by EsrefTurkok on 24.04.2018.
 */

public class Item {
    private String name;
    private String location;

    public Item(String name, String location) {
        this.name = name;
        this.location = location;
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
