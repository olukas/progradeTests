package net.sourceforge.prograde.progradetests.policyentry;

import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

/**
 *
 * @author Ondrej Lukas
 */
public class TestingLoginModule implements LoginModule {
    
    private Subject subject;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;   
    }

    @Override
    public boolean login() throws LoginException {
        return true;
    }

    @Override
    public boolean commit() throws LoginException {
        try {
            subject.getPrincipals().add(new TestingPrincipal("PrincipalA"));
            subject.getPrincipals().add(new TestingPrincipal("PrincipalB"));            
        } catch (Exception ex) {
            throw new LoginException("commit failed");
        }
        return true;
    }

    @Override
    public boolean abort() throws LoginException {
        return true;
    }

    @Override
    public boolean logout() throws LoginException {
        subject=null;
        return true;
    }
    
}
