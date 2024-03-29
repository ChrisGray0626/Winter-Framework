package pers.chris.winter.context.winter.util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Description Class Utility
 * @Author Chris
 * @Date 2023/2/26
 */
public class ClassUtil {

    private ClassUtil() {
    }

    public static Set<String> scanPackageForClassName(String... packageNames) {
        Set<String> classes = new HashSet<>();
        for (String packageName : packageNames) {
            String path = packageName.replace(".", "/");
            try {
                Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(path);
                while (resources.hasMoreElements()) {
                    URL resource = resources.nextElement();
                    if (resource.getProtocol().equals("file")) {
                        String filePath = URLDecoder.decode(resource.getFile(), StandardCharsets.UTF_8);
                        scanFile(packageName, filePath, classes);
                    } else if (resource.getProtocol().equals("jar")) {
                        JarURLConnection jarURLConnection = (JarURLConnection) resource.openConnection();
                        scanJar(packageName, jarURLConnection, classes);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return classes;
    }

    private static void scanFile(String packageName, String filePath, Set<String> classeNames)
            throws ClassNotFoundException {
        File file = new File(filePath);
        File[] files = file.listFiles();
        assert files != null;
        for (File subFile : files) {
            // If it is a directory, scan it recursively
            if (subFile.isDirectory()) {
                scanFile(packageName + "." + subFile.getName(), subFile.getAbsolutePath(), classeNames);
            } else {
                String className = packageName + "." + subFile.getName().substring(0, subFile.getName().length() - 6);
                classeNames.add(className);
            }
        }
    }

    private static void scanJar(String packageName, JarURLConnection jarURLConnection, Set<String> classes)
            throws ClassNotFoundException, IOException {
        JarFile jarFile = jarURLConnection.getJarFile();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String jarEntryName = jarEntry.getName();
            if (jarEntryName.endsWith(".class") && jarEntryName.startsWith(packageName.replace(".", "/"))) {
                String className = jarEntryName.substring(0, jarEntryName.length() - 6).replace("/", ".");
                classes.add(className);
            }
        }
    }
}
