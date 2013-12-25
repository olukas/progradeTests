/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.prograde.progradetests.miscellaneous;

import net.sourceforge.prograde.sm.ProgradeSecurityManager;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 *
 * @author olukas
 */
public class OwnPermissionTestCase {
    
    @Test
    public void testAccessWithOwnPermission() throws Exception {
        System.setProperty("java.security.policy","=src/test/resources/policyfiles/ownPermission.policy");
        System.setSecurityManager(new ProgradeSecurityManager());
        try {
            assertTrue("Access to granted permission was denied.",testPermission("grant"));
            assertFalse("Access to denied permission was granted.",testPermission("deny"));
        } finally {
            System.setSecurityManager(null);
        }
    }    
    
    private boolean testPermission(String name) {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            try {            
                sm.checkPermission(new MyOwnTestPermission(name));
            } catch(SecurityException ex) {
                return false;
            }
        }
        return true;
    }
}
