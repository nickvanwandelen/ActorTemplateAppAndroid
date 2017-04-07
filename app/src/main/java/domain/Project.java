package domain;

/**
 * Created by Nick on 3-4-2017.
 */

public class Project {
    private String Description, ID, Name;

    public Project(){
        
    }

    public String getDesrciption() {
        return Description;
    }

    public void setDescription(String desciption) {
        this.Description = desciption;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }
}
