package dk.nykredit.swagger;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.config.Scanner;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

/**
 * Specialization to also handle classed annotated with {@link io.swagger.annotations.SwaggerDefinition}.
 *
 * @see io.swagger.jaxrs.config.BeanConfig#classes()
 * @see io.swagger.jaxrs.config.ReflectiveJaxrsScanner
 */
class JaxRSScanner implements Scanner {
    private boolean prettyPrint;
    private Set<String> resourcePackages = Collections.emptySet();

    @Override
    public Set<Class<?>> classes() {
        ConfigurationBuilder config = ConfigurationBuilder.build(resourcePackages);
        config.setScanners(new ResourcesScanner(), new TypeAnnotationsScanner(), new SubTypesScanner());
        Reflections reflections = new Reflections(config);

        Set<Class<?>> apiClasses = reflections.getTypesAnnotatedWith(Api.class);
        Stream<Class<?>> filteredApiStrem = apiClasses.stream()
            .filter(cls -> resourcePackages.isEmpty() || resourcePackages.contains(cls.getPackage().getName()));

        Set<Class<?>> defClasses = reflections.getTypesAnnotatedWith(SwaggerDefinition.class);
        Stream<Class<?>> filteredDefStream = defClasses.stream()
            .filter(cls -> resourcePackages.isEmpty() || resourcePackages.contains(cls.getPackage().getName()));

        return Stream.concat(filteredApiStrem, filteredDefStream).collect(Collectors.toSet());
    }

    @Override
    public boolean getPrettyPrint() {
        return prettyPrint;
    }

    @Override
    public void setPrettyPrint(boolean shouldPrettyPrint) {
        this.prettyPrint = shouldPrettyPrint;
    }

    void setResourcePackages(Set<String> resourcePackages) {
        this.resourcePackages = new HashSet<>(resourcePackages);
    }
}
