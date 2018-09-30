package io.openapitools.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ws.rs.Path;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

/**
 * Scan for classes with {@link Path} annotation or {@link OpenAPIDefinition} annotation.
 */
class JaxRSScanner {
    private Set<String> resourcePackages = Collections.emptySet();

    Set<Class<?>> classes() {
        ConfigurationBuilder config = ConfigurationBuilder.build(resourcePackages)
                .setScanners(new ResourcesScanner(), new TypeAnnotationsScanner(), new SubTypesScanner());
        Reflections reflections = new Reflections(config);
        Stream<Class<?>> apiClasses = reflections.getTypesAnnotatedWith(Path.class).stream()
                .filter(cls -> resourcePackages.isEmpty() || resourcePackages.contains(cls.getPackage().getName()));
        Stream<Class<?>> defClasses = reflections.getTypesAnnotatedWith(OpenAPIDefinition.class).stream()
                .filter(cls -> resourcePackages.isEmpty() || resourcePackages.contains(cls.getPackage().getName()));
        return Stream.concat(apiClasses, defClasses).collect(Collectors.toSet());
    }

    void setResourcePackages(Set<String> resourcePackages) {
        this.resourcePackages = new HashSet<>(resourcePackages);
    }
}
