package net.sourceforge.prograde.progradetests.grantanddeny;

import java.io.File;
import net.sourceforge.prograde.sm.ProgradeSecurityManager;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * Test whether conflicts between grant and deny entry have expected results
 *
 * @author olukas
 */
public class GrantAndDenyConflictTestCase {

    /*
     * Test conflict between grant and deny entry when both of them are same and grant priority is set
     */
    @Test
    public void testFullConflictGrantPriotity() throws Exception {
        testFullConflict("fullConflictGrantPriority.policy", true);
    }

    /*
     * Test conflict between grant and deny entry when both of them are same and deny priority is set
     */
    @Test
    public void testFullConflictDenyPriotity() throws Exception {
        testFullConflict("fullConflictDenyPriority.policy", false);
    }

    /*
     * Test conflict between grant and deny entry when deny entry is subset of grant entry and deny priority is set. Test access to specified file and also to
     * any file which has access set due to * in policy file
     */
    @Test
    public void testPartConflictSmallerDenyPriorityDeny() throws Exception {
        testPartConflict("partConflictSmallerDenyPriorityDeny.policy", false, true);
    }

    /*
     * Test conflict between grant and deny entry when deny entry is subset of grant entry and grant priority is set. Test access to specified file and also to
     * any file which has access set due to * in policy file
     */
    @Test
    public void testPartConflictSmallerDenyPriorityGrant() throws Exception {
        testPartConflict("partConflictSmallerDenyPriorityGrant.policy", true, true);
    }

    /*
     * Test conflict between grant and deny entry when grant entry is subset of deny entry and deny priority is set. Test access to specified file and also to
     * any file which has access set due to * in policy file
     */
    @Test
    public void testPartConflictSmallerGrantPriorityDeny() throws Exception {
        testPartConflict("partConflictSmallerGrantPriorityDeny.policy", false, false);
    }

    /*
     * Test conflict between grant and deny entry when grant entry is subset of deny entry and grant priority is set. Test access to specified file and also to
     * any file which has access set due to * in policy file
     */
    @Test
    public void testPartConflictSmallerGrantPriorityGrant() throws Exception {
        testPartConflict("partConflictSmallerGrantPriorityGrant.policy", true, false);
    }

    private void testFullConflict(String policyName, boolean expectedResult) throws Exception {
        System.setProperty("java.security.policy", "=src/test/resources/policyfiles/" + policyName);
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

    private void testPartConflict(String policyName, boolean expectedResult, boolean notSmallerExpectedResult) throws Exception {
        System.setProperty("java.security.policy", "=src/test/resources/policyfiles/" + policyName);
        System.setSecurityManager(new ProgradeSecurityManager());
        try {
            if (expectedResult) {
                try {
                    new File("src/test/resources/policyfiles/" + policyName).exists();
                } catch (Exception e) {
                    throw new Exception("Access was denied, but policy file is set to allow it. Exception: " + e.getMessage());
                }
            } else {
                try {
                    new File("src/test/resources/policyfiles/" + policyName).exists();
                    fail("Policy allowed access, but policy file was set to deny it.");
                } catch (Exception e) {
                }
            }
            if (notSmallerExpectedResult) {
                try {
                    new File("src/test/resources/policyfiles/anyFile.policy").exists();
                } catch (Exception e) {
                    throw new Exception("Wrong set of policy file! Access was denied, but policy file is set to allow it. Exception: " + e.getMessage());
                }
            } else {
                try {
                    new File("src/test/resources/policyfiles/anyFile.policy").exists();
                    fail("Wrong set of policy file! Policy allowed access, but policy file was set to deny it.");
                } catch (Exception e) {
                }
            }
        } finally {
            System.setSecurityManager(null);
        }
    }
}
