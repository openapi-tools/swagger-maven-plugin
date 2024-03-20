package io.openapitools.swagger.jakarta;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import io.openapitools.swagger.ClassUtils;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Application;
import org.apache.maven.plugin.logging.Log;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Scan for classes with {@link Path} annotation or {@link OpenAPIDefinition}
 * annotation, and for {@link Application} instances.
 */
class JakartaScanner {

    private final Log log;

    private final ClassGraph classGraph;

    private final Set<String> resourcePackages;

    private final boolean useResourcePackagesChildren;

    public JakartaScanner(Log log, ClassLoader clzLoader, Set<String> resourcePackages, Boolean useResourcePackagesChildren) {
        this.log = log;
        this.classGraph = new ClassGraph().enableClassInfo().enableAnnotationInfo()
                .addClassLoader(clzLoader);
        this.resourcePackages = resourcePackages == null ? Collections.emptySet() : new HashSet<>(resourcePackages);
        this.useResourcePackagesChildren = Boolean.TRUE.equals(useResourcePackagesChildren);
    }

    Application applicationInstance() {
        Application applicationInstance = null;
        try (ScanResult scanResult = classGraph.scan()) {
            ClassInfoList applicationClasses = scanResult.getSubclasses(Application.class.getName())
                    .filter(this::filterClassByResourcePackages);
            if (applicationClasses.size() == 1) {
                applicationInstance = ClassUtils.createInstance(applicationClasses.get(0).loadClass(Application.class));
            } else if (applicationClasses.size() > 1) {
                log.warn("More than one javax.ws.rs.core.Application classes found on the classpath, skipping");
            }
        }
        return applicationInstance;
    }

    Set<Class<?>> classes() {
        Set<Class<?>> classes;
        try (ScanResult scanResult = classGraph.scan()) {
            ClassInfoList apiClasses = scanResult.getClassesWithAnnotation(Path.class.getName());
            ClassInfoList defClasses = scanResult.getClassesWithAnnotation(OpenAPIDefinition.class.getName());
            classes = Stream.of(apiClasses, defClasses)
                    .flatMap(classList -> classList.filter(this::filterClassByResourcePackages).stream())
                    .map(ClassInfo::loadClass)
                    .collect(Collectors.toSet());
        }
        if (classes.isEmpty()) {
            log.warn("No @Path or @OpenAPIDefinition annotated classes found in given resource packages: " + resourcePackages);
        }
        return classes;
    }

    private boolean filterClassByResourcePackages(ClassInfo cls) {
        return resourcePackages.isEmpty()
                || resourcePackages.contains(cls.getPackageName())
                || (useResourcePackagesChildren && resourcePackages.stream().anyMatch(p -> cls.getPackageName().startsWith(p)));
    }

}
