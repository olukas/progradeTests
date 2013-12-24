package net.sourceforge.prograde.progradetests.miscellaneous;

import net.sourceforge.prograde.sm.ProgradeSecurityManager;
import org.junit.Test;

/**
 * Test whether expand properties works right in policy files. 
 * Test works right only if property expandProperties in ${java.home}/lib/security/java.security 
 * is set to true (it is default setting in java).
 * 
 * @author olukas
 */
public class ExpandPropertiesTestCase {
    
    /*
     * Test whether test.java.home.property and test.read.property were right expanded
     */
    @Test
    public void testPriorityGrantAndPermissionGrant() throws Exception {
        System.setProperty("test.java.home.property","java.home");
        System.setProperty("test.read.property","read");
        System.setProperty("java.security.policy","=src/test/resources/policyfiles/expandProperties.policy");
        System.setSecurityManager(new ProgradeSecurityManager());
        try {            
            System.getProperty("java.home");
        } catch (Exception e) {
            throw new Exception("Access was denied, but policy file is set to allow it. Exception: " + e.getMessage());
        } finally {
            System.setSecurityManager(null);
        }
    }     
}
