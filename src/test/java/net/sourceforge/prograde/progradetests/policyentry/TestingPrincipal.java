package net.sourceforge.prograde.progradetests.policyentry;

import java.security.Principal;

/**
 *
 * @author Ondrej Lukas
 */
public class TestingPrincipal implements Principal {

    private String name;

    public TestingPrincipal(String name) throws Exception {
        this.name = name;
    }      
    
    @Override
    public String getName() {
        return name;
    }
    
}
