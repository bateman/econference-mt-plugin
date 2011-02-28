package it.uniba.di.cdg.xcore.aspects;

import it.uniba.di.cdg.xcore.m2m.IMultiChatManager;
import it.uniba.di.cdg.xcore.m2m.model.Privileged;
import it.uniba.di.cdg.xcore.m2m.model.IParticipant.Role;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * There are operations that can be performed only by the local user when he has some privileges 
 * (i.e. he is a moderator).
 * 
 * TODO We can generalize this aspect by capturing only the role (need to take a lookt at how to
 * do this ...)
 */
@Aspect
public class AuthorizationPolicy {
    /**
     * Captures all methods marked by the {@see Privileged} annotation.
     * 
     * @param p the annotation instance
     */
    @Pointcut("execution( @Privileged * *.*(..) ) && @annotation( p )")
    protected void privilegedMethods( Privileged p ) {
    }

    //        @Pointcut( "this( rp )" )
    //        protected void roleProvider( IRoleProvider rp ) {}

    /**
     * Check the privileges of the user attempting the operation and deny the execution if 
     * the user has not enough privileges.
     * 
     * @param jp
     * @param p
     * @param mcm
     * @throws Throwable
     */
    @Around("privilegedMethods( p ) && target( mcm )")
    public void checkPrivileges( ProceedingJoinPoint jp, Privileged p, IMultiChatManager mcm )
            throws Throwable {
        final Role current = mcm.getRole();
        jp.getThis();
        try {
            if (current.compareTo( p.atleast() ) >= 0)
                jp.proceed();
            else
                mcm.getUihelper().showMessage(
                        String.format( "Sorry, you need to be %s to perform that operation", 
                                p.atleast() ) );
        } catch (Throwable e) {
            mcm.getUihelper().showErrorMessage(
                    "An error occured performing the operation: " + e.getMessage() );
            e.printStackTrace();
        }
    }
}