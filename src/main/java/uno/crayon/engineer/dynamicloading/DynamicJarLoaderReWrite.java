package uno.crayon.engineer.dynamicloading;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

public class DynamicJarLoaderReWrite {

    private static final String WATCH_DIR = "path/to/your/directory";
    private static final long POLL_INTERVAL = 5000; // 5 seconds

    // 存储加载过的类加载器，用于覆盖类
    private static final Map<String, URLClassLoader> loadedClassLoaders = new HashMap<>();

    public static void main(String[] args) throws Exception {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path dir = Paths.get(WATCH_DIR);
        dir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

        while (true) {
            WatchKey key = watchService.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                    Path newPath = (Path) event.context();
                    String jarFileName = newPath.toString();
                    handleNewJar(jarFileName);
                }
            }
            key.reset();
            Thread.sleep(POLL_INTERVAL);
        }
    }

    private static void handleNewJar(String jarFileName) throws Exception {
        // 加载新的 JAR 包
        URLClassLoader classLoader = loadNewJar(jarFileName);

        // 覆盖类
        overrideClasses(classLoader);

        // 存储加载的类加载器，用于后续覆盖
        loadedClassLoaders.put(jarFileName, classLoader);
    }

    private static URLClassLoader loadNewJar(String jarFileName) throws MalformedURLException {
        URLClassLoader classLoader = new URLClassLoader(new URL[]{new File(WATCH_DIR + File.separator + jarFileName).toURI().toURL()});
        return classLoader;
    }

    private static void overrideClasses(URLClassLoader classLoader) throws Exception {
        // 遍历已加载的类加载器
        for (Map.Entry<String, URLClassLoader> entry : loadedClassLoaders.entrySet()) {
            String jarFileName = entry.getKey();
            URLClassLoader oldClassLoader = entry.getValue();

            // 获取当前类加载器的 URL 数组
            URL[] oldUrls = oldClassLoader.getURLs();

            // 遍历 URL 数组
            for (URL oldUrl : oldUrls) {
                File file = new File(oldUrl.toURI());
                if (file.getName().equals(jarFileName)) {
                    // 获取新的类加载器的 URL 数组
                    URL[] newUrls = classLoader.getURLs();

                    // 覆盖类
                    for (URL newUrl : newUrls) {
                        if (new File(newUrl.toURI()).getName().equals(jarFileName)) {
                            overrideClasses(oldUrl, newUrl);
                        }
                    }
                }
            }
        }
    }

    private static void overrideClasses(URL oldUrl, URL newUrl) throws Exception {
        // 获取类的路径
        String className = "com.example.YourClass"; // 需要覆盖的类名
        String classPath = className.replace('.', '/') + ".class";

        // 读取新旧类的字节码
        byte[] oldClassBytes = Files.readAllBytes(Paths.get(String.valueOf(oldUrl.toURI()), classPath));
        byte[] newClassBytes = Files.readAllBytes(Paths.get(String.valueOf(newUrl.toURI()), classPath));

        // 覆盖类
        Files.write(Paths.get(String.valueOf(oldUrl.toURI()), classPath), newClassBytes);
    }
}
