package io.automationhacks.testinfra.listing;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.automationhacks.testinfra.attribution.annotations.Flow;
import io.automationhacks.testinfra.attribution.annotations.OnCall;
import io.automationhacks.testinfra.attribution.annotations.Service;

import lombok.Data;

import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class TestListing {
    private static final Map<String, Integer> flowCount = new HashMap<>();
    private static final Map<String, Integer> serviceToTestCount = new HashMap<>();
    private static final List<TestClassInfo> testClassInfoList = new ArrayList<>();

    public static void listTestsWithMetadata(String[] args) {
        String packageName = "io.automationhacks.testinfra.reqres";
        List<Class<?>> testClasses = findTestClasses(packageName);

        for (Class<?> testClass : testClasses) {
            TestClassInfo classInfo = new TestClassInfo(testClass.getSimpleName());

            String classOncall = getAnnotationValue(testClass, OnCall.class);
            classInfo.setOnCall(classOncall);

            String classFlow = getAnnotationValue(testClass, Flow.class);
            if (classFlow != null) {
                classInfo.setFlow(classFlow);
                incrementCount(flowCount, classFlow);
            }

            for (Method method : testClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Test.class)) {
                    TestMethodInfo methodInfo = new TestMethodInfo(method.getName());

                    String methodOnCall = getAnnotationValue(method, OnCall.class);
                    String methodFlow = getAnnotationValue(method, Flow.class);
                    String methodService = getAnnotationValue(method, Service.class);

                    methodInfo.setOnCall(methodOnCall != null ? methodOnCall : classOncall);
                    methodInfo.setFlow(methodFlow != null ? methodFlow : classFlow);
                    methodInfo.setService(methodService);

                    if (methodFlow != null) {
                        incrementCount(flowCount, methodFlow);
                    } else if (classFlow != null) {
                        incrementCount(flowCount, classFlow);
                    }

                    if (methodService != null) {
                        incrementCount(serviceToTestCount, methodService);
                    }

                    classInfo.addTestMethod(methodInfo);
                }
            }

            testClassInfoList.add(classInfo);
        }

        printSummaryStatistics();
        writeToJsonFile("test_listing_output.json");
    }

    private static void writeToJsonFile(String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(
                            new File(fileName),
                            new TestListingOutput(
                                    testClassInfoList, flowCount, serviceToTestCount));
            System.out.println("JSON output has been written to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Data
    static class TestListingOutput {
        private List<TestClassInfo> testClasses;
        private Map<String, Integer> flowCoverage;
        private Map<String, Integer> serviceCoverage;

        public TestListingOutput(
                List<TestClassInfo> testClasses,
                Map<String, Integer> flowCoverage,
                Map<String, Integer> serviceCoverage) {
            this.testClasses = testClasses;
            this.flowCoverage = flowCoverage;
            this.serviceCoverage = serviceCoverage;
        }
    }

    @Data
    static class TestClassInfo {
        private String className;
        private String onCall;
        private String flow;
        private List<TestMethodInfo> testMethods = new ArrayList<>();

        public TestClassInfo(String className) {
            this.className = className;
        }

        public void addTestMethod(TestMethodInfo methodInfo) {
            testMethods.add(methodInfo);
        }
    }

    @Data
    static class TestMethodInfo {
        private String methodName;
        private String onCall;
        private String flow;
        private String service;

        public TestMethodInfo(String methodName) {
            this.methodName = methodName;
        }
    }

    private static <T> String getAnnotationValue(
            Class<?> clazz, Class<? extends Annotation> annotationClass) {
        if (clazz.isAnnotationPresent(annotationClass)) {
            try {
                return (String)
                        annotationClass
                                .getMethod("value")
                                .invoke(clazz.getAnnotation(annotationClass));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static <T> String getAnnotationValue(
            Method method, Class<? extends Annotation> annotationClass) {
        if (method.isAnnotationPresent(annotationClass)) {
            try {
                return (String)
                        annotationClass
                                .getMethod("value")
                                .invoke(method.getAnnotation(annotationClass));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static void incrementCount(Map<String, Integer> map, String key) {
        map.put(key, map.getOrDefault(key, 0) + 1);
    }

    private static void printSummaryStatistics() {
        System.out.println("\nSummary Statistics:");
        System.out.println("Functional Flow Coverage:");
        for (Map.Entry<String, Integer> entry : flowCount.entrySet()) {
            System.out.printf("  %s: %d test(s)%n", entry.getKey(), entry.getValue());
        }

        System.out.println("\nService Method Coverage:");
        for (Map.Entry<String, Integer> entry : serviceToTestCount.entrySet()) {
            System.out.printf("  %s: %d test(s)%n", entry.getKey(), entry.getValue());
        }
    }

    private static List<Class<?>> findTestClasses(String packageName) {
        List<Class<?>> testClasses = new ArrayList<>();

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = packageName.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File directory = new File(resource.getFile());

                if (directory.exists()) {
                    testClasses.addAll(findTestClassesInDirectory(directory, packageName));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return testClasses;
    }

    private static List<Class<?>> findTestClassesInDirectory(File directory, String packageName) {
        List<Class<?>> testClasses = new ArrayList<>();
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    testClasses.addAll(
                            findTestClassesInDirectory(file, packageName + "." + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    String className =
                            packageName
                                    + '.'
                                    + file.getName().substring(0, file.getName().length() - 6);
                    try {
                        Class<?> clazz = Class.forName(className);
                        if (clazz.isAnnotationPresent(Test.class) || hasTestMethods(clazz)) {
                            testClasses.add(clazz);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return testClasses;
    }

    private static boolean hasTestMethods(Class<?> clazz) {
        for (java.lang.reflect.Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Test.class)) {
                return true;
            }
        }
        return false;
    }
}
