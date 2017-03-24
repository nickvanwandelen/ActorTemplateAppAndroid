package domain;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Nick on 24-Mar-17.
 */

public class Actor {
    private String role, roleDescription;
    private ArrayList<Roleplayer> roleplayers;

    public Actor(String r, String rd){
        roleplayers = new ArrayList<Roleplayer>();
        this.role = r;
        this.roleDescription = rd;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public ArrayList<Roleplayer> getRoleplayers() {
        return roleplayers;
    }

    public void setRoleplayers(ArrayList<Roleplayer> roleplayers) {
        this.roleplayers = roleplayers;
    }
}
