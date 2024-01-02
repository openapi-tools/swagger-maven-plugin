package io.openapitools.swagger;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.Path;
import javax.ws.rs.core.Application;

import org.apache.maven.plugin.logging.Log;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;

/**
 * Scan for classes with {@link Path} annotation or {@link OpenAPIDefinition}
 * annotation, and for {@link Application} instances.
 */
class JaxRSScanner {

    private final Log log;

    private final Set<String> resourcePackages;

    private final boolean useResourcePackagesChildren;

    public JaxRSScanner(Log log, Set<String> resourcePackages, Boolean useResourcePackagesChildren) {
        this.log = log;
        this.resourcePackages = resourcePackages == null ? Collections.emptySet() : new HashSet<>(resourcePackages);
        this.useResourcePackagesChildren = Boolean.TRUE.equals(useResourcePackagesChildren);
    }

    Application applicationInstance() {
        ClassGraph classGraph = new ClassGraph().enableClassInfo();
        try (ScanResult scanResult = classGraph.scan()) {
            List<ClassInfo> applicationClasses = scanResult.getSubclasses(Application.class.getName()).stream()
                    .filter(this::filterClassByResourcePackages)
                    .collect(Collectors.toList());
            if (applicationClasses.size() == 1) {
                return ClassUtils.createInstance(applicationClasses.get(0).loadClass(Application.class));
            }
            if (applicationClasses.size() > 1) {
                log.warn("More than one javax.ws.rs.core.Application classes found on the classpath, skipping");
            }
        }
        return null;
    }

    Set<Class<?>> classes() {
        ClassGraph classGraph = new ClassGraph().enableClassInfo().enableAnnotationInfo();
        try (ScanResult scanResult = classGraph.scan()) {
            ClassInfoList apiClasses = scanResult.getClassesWithAnnotation(Path.class.getName());
            ClassInfoList defClasses = scanResult.getClassesWithAnnotation(OpenAPIDefinition.class.getName());
            return Stream.concat(apiClasses.stream(), defClasses.stream())
                    .filter(this::filterClassByResourcePackages)
                    .map(ClassInfo::loadClass)
                    .collect(Collectors.toSet());
        }
    }

    private boolean filterClassByResourcePackages(ClassInfo cls) {
        return resourcePackages.isEmpty()
                || resourcePackages.contains(cls.getPackageName())
                || (useResourcePackagesChildren && resourcePackages.stream().anyMatch(p -> cls.getPackageName().startsWith(p)));
    }

}
