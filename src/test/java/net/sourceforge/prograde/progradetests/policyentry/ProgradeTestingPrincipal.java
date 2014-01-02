package net.sourceforge.prograde.progradetests.policyentry;

import java.security.Principal;

/**
 *
 * @author Ondrej Lukas
 */
public class ProgradeTestingPrincipal implements Principal {

    private String name;

    public ProgradeTestingPrincipal(String name) throws Exception {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
