package testinfra;


import io.automationhacks.testinfra.attribution.OnCall;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.testng.annotations.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class TestNGTestListing {
    private Logger logger = LoggerFactory.getLogger(TestNGTestListing.class);

    public static void main(String[] args) {
        String packageName = "reqres"; // Replace with your package name
        List<Class<?>> testClasses = findTestClasses(packageName);

        for (Class<?> testClass : testClasses) {

            System.out.printf("Test Class: %s%n", testClass.getSimpleName());
            String classOncall = getOnCallValue(testClass);
            System.out.printf("  Class oncall: %s%n", classOncall);

            for (java.lang.reflect.Method method : testClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Test.class)) {
                    System.out.println("    Test Method: " + method.getName());

                    String methodOnCall = getOnCallValue(method);
                    if (methodOnCall != null) {
                        System.out.printf("     Method oncall: %s%n", methodOnCall);
                    } else if (classOncall != null) {
                        System.out.printf("  Class oncall: %s%n", classOncall);
                    }
                }
            }
        }
    }

    private static String getOnCallValue(Method method) {
        if (method.isAnnotationPresent(OnCall.class)) {
            return method.getAnnotation(OnCall.class).value();
        }

        return null;
    }

    private static String getOnCallValue(Class<?> clazz) {
        if (clazz.isAnnotationPresent(OnCall.class)) {
            return clazz.getAnnotation(OnCall.class).value();
        }
        return null;
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
                    File[] files = directory.listFiles();
                    for (File file : files) {
                        if (file.isFile() && file.getName().endsWith(".class")) {
                            String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                            Class<?> clazz = Class.forName(className);
                            if (clazz.isAnnotationPresent(Test.class) || hasTestMethods(clazz)) {
                                testClasses.add(clazz);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
