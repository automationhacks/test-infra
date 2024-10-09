package io.automationhacks.testinfra.alerting;

import io.automationhacks.testinfra.attribution.annotations.Flow;
import io.automationhacks.testinfra.attribution.annotations.OnCall;
import io.automationhacks.testinfra.attribution.annotations.Service;
import io.automationhacks.testinfra.model.TestResult;

import org.testng.ITestResult;

import java.lang.reflect.Method;
import java.util.Date;

public class TestResultManager {
    public TestResult createTestResult(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        Method testMethod = result.getMethod().getConstructorOrMethod().getMethod();

        String onCall = getOnCallPerson(testMethod);
        String functionalFlow = getFlow(testMethod);
        String serviceMethod = getService(testMethod);

        return new TestResult(
                result.getTestClass().getName(),
                testName,
                result.getStatus(),
                result.getThrowable().getMessage(),
                new Date(),
                onCall,
                functionalFlow,
                serviceMethod);
    }

    private String getOnCallPerson(Method testMethod) {
        OnCall onCallAnnotation = testMethod.getAnnotation(OnCall.class);
        if (onCallAnnotation == null) {
            onCallAnnotation = testMethod.getDeclaringClass().getAnnotation(OnCall.class);
        }
        return onCallAnnotation != null ? onCallAnnotation.value() : "Unknown";
    }

    private String getFlow(Method testMethod) {
        Flow flowAnnotation = testMethod.getAnnotation(Flow.class);

        if (flowAnnotation == null) {
            flowAnnotation = testMethod.getDeclaringClass().getAnnotation(Flow.class);
        }
        return flowAnnotation != null ? flowAnnotation.value() : "Unknown";
    }

    private String getService(Method testMethod) {
        Service serviceMethodAnnotation = testMethod.getAnnotation(Service.class);
        return serviceMethodAnnotation != null ? serviceMethodAnnotation.value() : "Unknown";
    }
}
