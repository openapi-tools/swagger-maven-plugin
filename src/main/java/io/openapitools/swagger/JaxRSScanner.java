package io.openapitools.swagger;

import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.apache.maven.plugin.logging.Log;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        this.useResourcePackagesChildren = useResourcePackagesChildren != null && useResourcePackagesChildren;
    }

    Set<? extends ModelConverter> generateModelConverters() {
        ConfigurationBuilder config = ConfigurationBuilder
                .build(resourcePackages)
                .setScanners(new ResourcesScanner(), new TypeAnnotationsScanner(), new SubTypesScanner());
        Reflections reflections = new Reflections(config);

        return reflections.getSubTypesOf(ModelConverter.class).stream()
                .filter(this::filterClassByResourcePackages)
                .map(this::generateModelConverterInstance)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    Application applicationInstance() {
        ConfigurationBuilder config = ConfigurationBuilder
                .build(resourcePackages)
                .setScanners(new ResourcesScanner(), new TypeAnnotationsScanner(), new SubTypesScanner());
        Reflections reflections = new Reflections(config);
        Set<Class<? extends Application>> applicationClasses = reflections.getSubTypesOf(Application.class)
                .stream()
                .filter(this::filterClassByResourcePackages)
                .collect(Collectors.toSet());
        if (applicationClasses.isEmpty()) {
            return null;
        }
        if (applicationClasses.size() > 1) {
            log.warn("More than one javax.ws.rs.core.Application classes found on the classpath, skipping");
            return null;
        }
        return ClassUtils.createInstance(applicationClasses.iterator().next());
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

    private <T extends ModelConverter> T generateModelConverterInstance(Class<T> converter) {
        try {
            return converter.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException nsme) {
            log.error(String.format("Model converter %s should have a no-arg constructor. Will not skip registration of model converter.", converter.getName()), nsme);
            return null;
        }
    }

}
