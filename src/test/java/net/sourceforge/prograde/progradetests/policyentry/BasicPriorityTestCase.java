package net.sourceforge.prograde.progradetests.policyentry;

import net.sourceforge.prograde.sm.ProgradeSecurityManager;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * Test for priority entry from policy file. It also test basic priority grant and deny access to property.
 * 
 * @author Ondrej Lukas
 */
public class BasicPriorityTestCase {
    
    /*
     * Test whether grant priority and grant java.home permission permit access java.home property
     */
    @Test
    public void testPriorityGrantAndPermissionGrant() throws Exception {        
        priorityTest("priorityGrantPermissionGrant.policy",true);
    }
    
    /*
     * Test whether grant priority and deny java.home permission denying access java.home property
     */
    @Test
    public void testPriorityGrantAndPermissionDeny() throws Exception {        
        priorityTest("priorityGrantPermissionDeny.policy",false);
    }
    
    /*
     * Test whether deny priority and grant java.home permission permit access java.home property
     */
    @Test
    public void testPriorityDenyAndPermissionGrant() throws Exception {        
        priorityTest("priorityDenyPermissionGrant.policy",true);
    }
    
    /*
     * Test whether deny priority and deny java.home permission denying access java.home property
     */
    @Test
    public void testPriorityDenyAndPermissionDeny() throws Exception {        
        priorityTest("priorityDenyPermissionDeny.policy",false);
    }
    
    private void priorityTest(String policyName,boolean expectedResult) throws Exception {
        System.setProperty("java.security.policy","=src/test/resources/policyfiles/"+policyName);
        System.setSecurityManager(new ProgradeSecurityManager());
        try {
            if (expectedResult) {
                try {
                    System.getProperty("java.home");
                } catch (Exception e) {
                    throw new Exception("Access was denied, but policy file is set to allow it. Exception: " + e.getMessage());
                }
            } else {
                try {
                    System.getProperty("java.home");
                    fail("Policy allowed access, but policy file was set to deny it.");
                } catch (Exception e) {
                }
            }        
        } finally {
            System.setSecurityManager(null);
        }
    }
}
