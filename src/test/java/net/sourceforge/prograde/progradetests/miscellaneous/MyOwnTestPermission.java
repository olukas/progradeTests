package net.sourceforge.prograde.progradetests.miscellaneous;

import java.security.Permission;

/**
 *
 * @author olukas
 */
public class MyOwnTestPermission extends Permission {

    public MyOwnTestPermission(String name) {
        super(name);
    }

    @Override
    public boolean implies(Permission permission) {
        if (permission instanceof MyOwnTestPermission) {
            return ((MyOwnTestPermission) permission).getName().equals(this.getName());
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return ((MyOwnTestPermission) obj).getName().equals(this.getName());
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public String getActions() {
        return null;
    }
}
