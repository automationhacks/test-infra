package testinfra;


import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class TestNGTestListing {

    public static void main(String[] args) {
        String packageName = "reqres"; // Replace with your package name
        List<Class<?>> testClasses = findTestClasses(packageName);

        for (Class<?> testClass : testClasses) {
            System.out.println("Test Class: " + testClass.getSimpleName());
            for (java.lang.reflect.Method method : testClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Test.class)) {
                    System.out.println("  Test Method: " + method.getName());
                }
            }
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
