package io.openapitools.swagger;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.Path;
import javax.ws.rs.core.Application;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

/**
 * Scan for classes with {@link Path} annotation or {@link OpenAPIDefinition}
 * annotation.
 */
class JaxRSScanner {
    private Set<String> resourcePackages = Collections.emptySet();

    private boolean useResourcePackagesChildren;

    public JaxRSScanner(Boolean useResourcePackagesChildren) {
        this.useResourcePackagesChildren = useResourcePackagesChildren != null && useResourcePackagesChildren;
    }

    Set<Class<? extends Application>> applicationClasses() {
        ConfigurationBuilder config = ConfigurationBuilder
                .build(resourcePackages)
                .setScanners(new ResourcesScanner(), new TypeAnnotationsScanner(), new SubTypesScanner());
        Reflections reflections = new Reflections(config);
        return reflections.getSubTypesOf(Application.class)
                .stream()
                .filter(this::filterClassByResourcePackages)
                .collect(Collectors.toSet());
    }

    Set<Class<?>> classes() {
        ConfigurationBuilder config = ConfigurationBuilder
                .build(resourcePackages)
                .setScanners(new ResourcesScanner(), new TypeAnnotationsScanner(), new SubTypesScanner());
        Reflections reflections = new Reflections(config);
        Stream<Class<?>> apiClasses = reflections.getTypesAnnotatedWith(Path.class)
                .stream()
                .filter(this::filterClassByResourcePackages);
        Stream<Class<?>> defClasses = reflections.getTypesAnnotatedWith(OpenAPIDefinition.class)
                .stream()
                .filter(this::filterClassByResourcePackages);
        return Stream.concat(apiClasses, defClasses).collect(Collectors.toSet());
    }

    private boolean filterClassByResourcePackages(Class<?> cls) {
        return resourcePackages.isEmpty()
                || resourcePackages.contains(cls.getPackage().getName())
                || (useResourcePackagesChildren && resourcePackages.stream().anyMatch(p -> cls.getPackage().getName().startsWith(p)));
    }

    void setResourcePackages(Set<String> resourcePackages) {
        this.resourcePackages = new HashSet<>(resourcePackages);
    }
}
