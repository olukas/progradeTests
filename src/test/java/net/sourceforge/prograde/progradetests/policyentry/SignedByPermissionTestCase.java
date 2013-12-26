package net.sourceforge.prograde.progradetests.policyentry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import net.sourceforge.prograde.sm.ProgradeSecurityManager;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * Test for signedBy part of permission from grant/deny entry of policy file.
 *
 * @author olukas
 */
public class SignedByPermissionTestCase {

    /*
     * Test whether permission from jar signed by same certificate as is set in signedBy in policy file allow access.
     */
    @Test
    public void testAccessGrantToSignedPermission() throws Exception {
        signedByTest("signedByPermission.policy", true);
    }

    /*
     * Test whether permission from jar signed by different certificate as is set in signedBy in policy file not allow access.
     */
    @Test
    public void testAccessDenyToWrongSignedPermission() throws Exception {
        signedByTest("wrongSignedByPermission.policy", false);
    }

    private void signedByTest(String policyName, boolean expectedResult) throws Exception {
        URLClassLoader urlcl = URLClassLoader.newInstance(new URL[]{
            new URL("file:./src/test/resources/SignedByPermission.jar")
        });
        Class<?> c = urlcl.loadClass("signedbypermission.SignedByPermission");
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
