package de.bund.bva.isyfact.polling.annotation;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.ClassUtils;

import de.bund.bva.isyfact.polling.PollingVerwalter;

/**
 * Interceptor for performing a polling action.
 * Updates the time of the last execution after calling the actual polling action.
 * 
 */
public class PollingAktionInterceptor implements MethodInterceptor {

    /** Access to the PollingVerwalter. Set by Spring. */
    private final PollingVerwalter pollingVerwalter;

    /**
     * Creates a new interceptor to perform a polling action.
     *
     * @param pollingVerwalter the {@link PollingVerwalter}
     */
    public PollingAktionInterceptor(PollingVerwalter pollingVerwalter) {
        this.pollingVerwalter = pollingVerwalter;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Class<?> targetClass =
            (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);

        PollingAktion pollingAktion = ermittlePollingAktionAnnotation(invocation.getMethod(), targetClass);

        try {
            return invocation.proceed();
        } finally {
            // Update the time of the last polling action.
            if (pollingAktion != null) {
                pollingVerwalter.aktualisiereZeitpunktLetztePollingAktivitaet(pollingAktion.pollingCluster());
            }
        }
    }

    /**
     * Determines the PollingAktion annotation.
     * 
     * @param method Called Method.
     * @param targetClass Class in which the method was called.
     * @return Annotation PollingAktion
     */
    private PollingAktion ermittlePollingAktionAnnotation(Method method, Class<?> targetClass) {

        // The strategy for determining the annotation is taken from AnnotationTransactionAttributeSource.

        // Ignore CGLIB subclasses - introspect the actual user class.
        Class<?> userClass = ClassUtils.getUserClass(targetClass);
        // The method may be on an interface, but we need attributes from the target class.
        // If the target class is null, the method will be unchanged.
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, userClass);
        // If we are dealing with method with generic parameters, find the original method.
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);

        // First try is the method in the target class.
        PollingAktion pollingAktion = specificMethod.getAnnotation(PollingAktion.class);
        if (pollingAktion != null) {
            return pollingAktion;
        }

        if (specificMethod != method) {
            // Fallback is to look at the original method.
            pollingAktion = method.getAnnotation(PollingAktion.class);
            if (pollingAktion != null) {
                return pollingAktion;
            }

            // Last fallback is the class of the original method.
            return method.getDeclaringClass().getAnnotation(PollingAktion.class);
        }

        return null;        
    }

}



