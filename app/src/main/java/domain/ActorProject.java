package domain;

import java.util.ArrayList;

/**
 * Created by Nick on 24-Mar-17.
 */

public class ActorProject {
    private String projectName;
    private ArrayList<Actor> actors;
    private boolean archived = false;

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public ActorProject(String n){
        this.projectName = n;
        this.actors = new ArrayList<Actor>();
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public ArrayList<Actor> getActors() {
        return actors;
    }

    public void setActors(ArrayList<Actor> actors) {
        this.actors = actors;
    }
}
