package io.openapitools.swagger;

import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.oas.models.OpenAPI;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

import javax.ws.rs.core.Application;


@Mojo(name = "generate", defaultPhase = LifecyclePhase.PREPARE_PACKAGE, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class JaxRsGenerateMojo extends GenerateMojo {
    @Override
    protected OpenAPI getOpenApi() {
        Reader reader = new Reader(getSwaggerConfig() == null ? new OpenAPI() : getSwaggerConfig().createSwaggerModel());

        JaxRSScanner reflectiveScanner = new JaxRSScanner(getLog(), createClassLoader(), getResourcePackages(), getUseResourcePackagesChildren());

        Application application = resolveApplication(reflectiveScanner);
        reader.setApplication(application);

        OpenAPI swagger = OpenAPISorter.sort(reader.read(reflectiveScanner.classes()));
        return swagger;
    }


    private Application resolveApplication(JaxRSScanner reflectiveScanner) {
        if (getApplicationClass() == null || getApplicationClass().isEmpty()) {
            return reflectiveScanner.applicationInstance();
        }

        Class<?> clazz = ClassUtils.loadClass(getApplicationClass(), Thread.currentThread().getContextClassLoader());

        if (clazz == null || !Application.class.isAssignableFrom(clazz)) {
            getLog().warn("Provided application class does not implement javax.ws.rs.core.Application, skipping");
            return null;
        } else {
            getLog().info("Found Application Class : " + clazz.getName());
        }

        @SuppressWarnings("unchecked")
        Class<? extends Application> appClazz = (Class<? extends Application>)clazz;
        return ClassUtils.createInstance(appClazz);
    }
}
