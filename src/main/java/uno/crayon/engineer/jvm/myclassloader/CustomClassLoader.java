package uno.crayon.engineer.jvm.myclassloader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CustomClassLoader extends ClassLoader {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        // 创建自定义类加载器实例
        CustomClassLoader customClassLoader = new CustomClassLoader();

        // 加载自定义java.lang.Object类
        Class<?> customObjectClass = customClassLoader.loadClass("java.lang.Object");

        // 创建自定义java.lang.Object类的实例
        Object customObjectInstance = customObjectClass.newInstance();

        // 打印自定义java.lang.Object类的类加载器
        System.out.println("Class loaded by: " + customObjectClass.getClassLoader());
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        // 将类名转换为文件路径
        String classFilePath = className.replace('.', '/') + ".class";

        // 读取class文件的字节数组
        byte[] classBytes;
        try {
            Path path = Paths.get(classFilePath);
            classBytes = Files.readAllBytes(path);
        } catch (IOException e) {
            throw new ClassNotFoundException("Class not found: " + className, e);
        }

        // 使用defineClass方法将字节数组转换为Class对象
        return defineClass(className, classBytes, 0, classBytes.length);
    }
}
