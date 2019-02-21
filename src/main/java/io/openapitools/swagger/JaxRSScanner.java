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
 * Scan for classes with {@link Path} annotation or {@link OpenAPIDefinition}
 * annotation.
 */
class JaxRSScanner {
    private Set<String> resourcePackages = Collections.emptySet();

    private boolean useResourcePackagesChildren = false;

    public JaxRSScanner(Boolean useResourcePackagesChildren) {
	this.useResourcePackagesChildren = useResourcePackagesChildren != null && useResourcePackagesChildren;
    }

    Set<Class<?>> classes() {
	ConfigurationBuilder config = ConfigurationBuilder.build(resourcePackages).setScanners(new ResourcesScanner(),
		new TypeAnnotationsScanner(), new SubTypesScanner());
	Reflections reflections = new Reflections(config);
	Stream<Class<?>> apiClasses = reflections.getTypesAnnotatedWith(Path.class).stream()
		.filter(cls -> filterClassByResourcePackages(cls));
	Stream<Class<?>> defClasses = reflections.getTypesAnnotatedWith(OpenAPIDefinition.class).stream()
		.filter(cls -> filterClassByResourcePackages(cls));
	return Stream.concat(apiClasses, defClasses).collect(Collectors.toSet());
    }

    private boolean filterClassByResourcePackages(Class<?> cls) {
	if(resourcePackages.isEmpty()) {
	    return true;
	}
	
	if (useResourcePackagesChildren) {
	    for (String resourcePackage : resourcePackages) {
		if(cls.getPackage().getName().startsWith(resourcePackage+".") || cls.getPackage().getName().equals(resourcePackage)) {
		    return true;
		}
	    }
	} else {
	    return resourcePackages.contains(cls.getPackage().getName());
	}
	
	return false;
    }

    void setResourcePackages(Set<String> resourcePackages) {
	this.resourcePackages = new HashSet<>(resourcePackages);
    }
}
