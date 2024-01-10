package uno.crayon.engineer.dynamicloading;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class DynamicJarLoader {

    // 监听的目录
    private static final String WATCH_DIR = "path/to/your/directory";
    
    // 轮询间隔时间（毫秒）
    private static final long POLL_INTERVAL = 5000; // 5 秒

    public static void main(String[] args) throws IOException, InterruptedException {
        // 创建 WatchService 对象
        WatchService watchService = FileSystems.getDefault().newWatchService();
        
        // 注册监听目录
        Path dir = Paths.get(WATCH_DIR);
        dir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

        while (true) {
            // 获取 WatchKey
            WatchKey key = watchService.take();
            
            // 处理目录变化事件
            for (WatchEvent<?> event : key.pollEvents()) {
                if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                    Path newPath = (Path) event.context();
                    String jarFileName = newPath.toString();
                    handleNewJar(jarFileName);
                }
            }
            
            // 重置 WatchKey，以便继续监听
            key.reset();
            
            // 等待一段时间后再次轮询
            Thread.sleep(POLL_INTERVAL);
        }
    }

    private static void handleNewJar(String jarFileName) {
        // 卸载运行中的同名类
        unloadClassesWithSameName(jarFileName);
        
        // 动态加载新的 JAR 包
        loadNewJar(jarFileName);
    }

    private static void unloadClassesWithSameName(String jarFileName) {
        List<ClassLoader> classLoaders = new ArrayList<>();
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        classLoaders.add(currentClassLoader);

        // 获取所有已加载的类加载器
        while (currentClassLoader.getParent() != null) {
            classLoaders.add(currentClassLoader.getParent());
            currentClassLoader = currentClassLoader.getParent();
        }

        // 遍历所有类加载器
        for (ClassLoader classLoader : classLoaders) {
            if (classLoader instanceof URLClassLoader) {
                URLClassLoader urlClassLoader = (URLClassLoader) classLoader;

                // 获取类加载器的 URL 数组
                URL[] urls = urlClassLoader.getURLs();

                // 遍历 URL 数组，检查是否存在同名类
                for (URL url : urls) {
                    try {
                        File file = new File(url.toURI());
                        if (file.getName().equals(jarFileName)) {
                            // 卸载类加载器
                            unloadClassLoader(classLoader);
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void unloadClassLoader(ClassLoader classLoader) {
        try {
            //classLoader.close();
            classLoader.wait();
        //} catch (IOException e) {
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void loadNewJar(String jarFileName) {
        JarClassLoader classLoader = new JarClassLoader(new URL[]{});
        try {
            classLoader.addJarFile(WATCH_DIR + File.separator + jarFileName);
            
            // 使用加载的类执行相应的逻辑
            Class<?> loadedClass = classLoader.loadClass("com.example.YourClass");
            
            // 实例化类、调用方法等...
            Object instance = loadedClass.newInstance();
            
            // ...
        } catch (MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    static class JarClassLoader extends URLClassLoader {
        public JarClassLoader(URL[] urls) {
            super(urls);
        }

        public void addJarFile(String pathToJar) throws MalformedURLException {
            URL url = new File(pathToJar).toURI().toURL();
            addURL(url);
        }
    }
}
