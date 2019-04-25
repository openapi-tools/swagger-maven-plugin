package io.openapitools.swagger;

final class ClassUtils {

    private ClassUtils() {
    }

    public static Class<?> loadClass(String className, ClassLoader classLoader) {
        try {
            return Class.forName(className, true, classLoader);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static <T> T createInstance(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException | IllegalArgumentException | SecurityException e) {
            return null;
        }
    }

}
