package net.sourceforge.prograde.progradetests.policyentry;

import java.security.PrivilegedAction;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import net.sourceforge.prograde.sm.ProgradeSecurityManager;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * Test for principal entry from policy file.
 * 
 * @author Ondrej Lukas
 */
public class PrincipalTestCase {
    
    /*
     * Test whether subject with TestingPrincipal principal can read java.home which has grant for TestingPrincipal
     */
    @Test
    public void testOnePrincipalWithRightSubjectPrincipal() throws Exception {        
        principalTest("onePrincipal.policy",true,true);
    }
    
    /*
     * Test whether subject without any principal can't read java.home which has grant for TestingPrincipal
     */
    @Test
    public void testOnePrincipalWithoutSubjectPrincipal() throws Exception {        
        principalTest("onePrincipal.policy",false,false);
    }
    
    /*
     * Test whether subject with TestingPrincipal principal can't read java.home which has grant for WrongTestingPrincipal
     */
    @Test
    public void testOnePrincipalWithWrongSubjectPrincipal() throws Exception {        
        principalTest("oneWrongPrincipal.policy",false,true);
    }
    
    /*
     * Test whether subject with two right TestingPrincipal principals can read java.home which has grant for that two TestingPrincipal
     */
    @Test
    public void testTwoPrincipalsWithRightSubjectPrincipal() throws Exception {        
        principalTest("twoPrincipal.policy",true,true);
    }
    
    /*
     * Test whether subject with two TestingPrincipal principals, but only one of them is right can't read java.home 
     * which has grant for one of that TestingPrincipal and one different TestingPrincipal
     */
    @Test
    public void testTwoPrincipalsWithOnlyOneRightSubjectPrincipal() throws Exception {        
        principalTest("twoWrongPrincipal.policy",false,true);
    }
    
    /*
     * Test whether subject with TestingPrincipal principal can read java.home which has grant through wildcard for whatever TestingPrincipal
     */
    @Test
    public void testOnePrincipalWildcardWithSujectPrincipal() throws Exception {        
        principalTest("oneWildcardPrincipal.policy",true,true);
    }
    
    /*
     * Test whether subject with TestingPrincipal principal can read java.home which has grant through wildcard for whatever Principal
     */
    @Test
    public void testTwoPrincipalWildcardWithSujectPrincipal() throws Exception {        
        principalTest("twoWildcardPrincipal.policy",true,true);
    }
    
    /*
     * Test whether subject without any principal can't read java.home which has grant through wildcard for whatever TestingPrincipal
     */
    @Test
    public void testOnePrincipalWildcardWithoutSujectPrincipal() throws Exception {        
        principalTest("oneWildcardPrincipal.policy",false,false);
    }
    
    /*
     * Test whether subject without any principal can't read java.home which has grant through wildcard for whatever Principal
     */
    @Test
    public void testTwoPrincipalWildcardWithoutSujectPrincipal() throws Exception {        
        principalTest("twoWildcardPrincipal.policy",false,false);
    }
    
    private void principalTest(String policyName,boolean expectedResult,boolean login) throws Exception {
        LoginContext lc = null;
        Subject subject = null;
        PrivilegedAction action = null;
        if (login) {
            System.setProperty("java.security.auth.login.config","=src/test/resources/loginmodule.config");            
            try {
                lc = new LoginContext("Testing");
            } catch (LoginException ex) {
                throw new Exception("Can't create login context");
            }
            lc.login();
            subject = lc.getSubject();
            action = new ReadJavaHomeAction();
        }        
        System.setProperty("java.security.policy","=src/test/resources/policyfiles/"+policyName);
        System.setSecurityManager(new ProgradeSecurityManager());
        try {
            if (expectedResult) {
                try {
                    if (login) {
                        Subject.doAsPrivileged(subject, action, null);
                    } else {
                        System.getProperty("java.home");
                    }                    
                } catch (Exception e) {
                    throw new Exception("Access was denied, but policy file is set to allow it. Exception: " + e.getMessage());
                }
            } else {
                try {
                    if (login) {
                        Subject.doAsPrivileged(subject, action, null);
                    } else {
                        System.getProperty("java.home");
                    } 
                    fail("Policy allowed access, but policy file was set to deny it.");
                } catch (Exception e) {
                }
            }        
        } finally {
            System.setSecurityManager(null);
            if (login) {
                lc.logout();
            }
        }
    }
}
