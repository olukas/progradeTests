package net.sourceforge.prograde.progradetests.policyentry;

import java.security.PrivilegedAction;

/**
 *
 * @author Ondrej Lukas
 */
public class ReadJavaHomeAction implements PrivilegedAction {

    @Override
    public Object run() {
        System.getProperty("java.home");
        return null;
    }
}
