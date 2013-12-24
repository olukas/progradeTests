package net.sourceforge.prograde.progradetests.grantanddeny;

import net.sourceforge.prograde.sm.ProgradeSecurityManager;
import org.junit.Test;
import static org.junit.Assert.fail;

/**
 * Test whether more grant/deny entries and more permission in one entry are successfully used.
 * 
 * @author olukas
 */
public class MoreGrantOrDenyEntriesInPolicyFileTestCase {
    
    /*
     * Test whether more grant entries and more permission in one grant entry are successfully used.
     */
    @Test
    public void testMoreGrantEntriesInPolicyFile() throws Exception {
        testMoreGrantOrDenyEntries("moreGrantEntries.policy",true);
    }
    
    /*
     * Test whether more deny entries and more permission in one deny entry are successfully used.
     */
    @Test
    public void testMoreDenyEntriesInPolicyFile() throws Exception {
        testMoreGrantOrDenyEntries("moreDenyEntries.policy",false);
    }
    
    private void testMoreGrantOrDenyEntries(String policyName,boolean expectedResult) throws Exception {
        System.setProperty("java.security.policy","=src/test/resources/policyfiles/"+policyName);
        System.setSecurityManager(new ProgradeSecurityManager());
        try {
            if (expectedResult) {
                try {
                    System.getProperty("java.home");
                    System.getProperty("user.home");
                    System.getProperty("java.version");
                } catch (Exception e) {
                    throw new Exception("Access was denied, but policy file is set to allow it. Exception: " + e.getMessage());
                }
            } else {
                try {
                    System.getProperty("java.home");
                    fail("Policy allowed access, but policy file was set to deny it.");
                } catch (Exception e) {
                }
                try {
                    System.getProperty("user.home");
                    fail("Policy allowed access, but policy file was set to deny it.");
                } catch (Exception e) {
                }
                try {
                    System.getProperty("java.version");
                    fail("Policy allowed access, but policy file was set to deny it.");
                } catch (Exception e) {
                }
            }        
        } finally {
            System.setSecurityManager(null);
        }
    }
}
