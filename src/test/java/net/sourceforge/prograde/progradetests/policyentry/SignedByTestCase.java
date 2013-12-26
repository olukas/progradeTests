package net.sourceforge.prograde.progradetests.policyentry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import net.sourceforge.prograde.sm.ProgradeSecurityManager;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * Test for signedBy entry from policy file.
 *
 * @author Ondrej Lukas
 */
public class SignedByTestCase {

    /*
     * Test whether signed jar with all permission granted can access to java.home property
     */
    @Test
    public void testGrantAllPermissionForSignedJar() throws Exception {
        signedByTest("grantAllPermission.policy", true);
    }

    /*
     * Test whether signed jar with all permission granted and signedBy "duke" denied for java.home can't access to java.home property
     */
    @Test
    public void testOneSignedByAndSignedJar() throws Exception {
        signedByTest("signedByGrantAllPermissionDukeDeny.policy", false);
    }

    /*
     * Test whether signed jar with all permission granted and signedBy "duke,eva" denied for java.home can access to java.home property
     */
    @Test
    public void testTwoSignedByButOnlyOneSignedJar() throws Exception {
        signedByTest("signedByGrantAllPermissionDukeEvaDeny.policy", true);
    }

    /*
     * Test whether signed jar with all permission granted and signedBy "duke,adam" denied for java.home can't access to java.home property
     */
    @Test
    public void testTwoSignedByAndSignedJar() throws Exception {
        signedByTest("signedByGrantAllPermissionDukeAdamDeny.policy", false);
    }

    private void signedByTest(String policyName, boolean expectedResult) throws Exception {
        URLClassLoader urlcl = URLClassLoader.newInstance(new URL[]{
            new URL("file:./src/test/resources/ReadJavaHome.jar")
        });
        Class<?> c = urlcl.loadClass("readjavahome.ReadJavaHome");
        String[] myStringArray = new String[1];
        Method m = c.getMethod("main", new Class[]{myStringArray.getClass()});
        System.setProperty("java.security.policy", "=src/test/resources/policyfiles/" + policyName);
        System.setSecurityManager(new ProgradeSecurityManager());
        try {
            if (expectedResult) {
                try {
                    m.invoke(null, myStringArray);
                } catch (InvocationTargetException e) {
                    throw new Exception("Access was denied, but policy file is set to allow it. Exception: " + e.getTargetException().getMessage());
                }
            } else {
                try {
                    m.invoke(null, myStringArray);
                    fail("Policy allowed access, but policy file was set to deny it.");
                } catch (InvocationTargetException e) {
                }
            }
        } finally {
            System.setSecurityManager(null);
        }
    }
}
