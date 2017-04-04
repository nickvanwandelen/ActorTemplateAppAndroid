package domain;

/**
 * Created by Nick on 3-4-2017.
 */

public class Actor {
    private String Description, ID, Name, State;

    public Actor(){

    }


    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
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

    public String getState() {
        return State;
    }

    public void setState(String state) {
        this.State = state;
    }
}
