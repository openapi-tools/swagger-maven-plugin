package io.openapitools.swagger;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import io.openapitools.swagger.config.SwaggerConfig;
import io.swagger.jaxrs.Reader;
import io.swagger.models.Swagger;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;

/**
 * Maven mojo to generate OpenAPI documentation document based on Swagger.
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.PREPARE_PACKAGE, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class GenerateMojo extends AbstractMojo {

    /**
     * Static information to provide for the generation.
     */
    @Parameter
    private SwaggerConfig swaggerConfig;

    /**
     * List of packages which contains API resources. This is <i>not</i> recursive.
     */
    @Parameter
    private Set<String> resourcePackages;

    /**
     * Directory to contain generated documentation.
     */
    @Parameter(defaultValue = "${project.build.directory}")
    private File outputDirectory;

    /**
     * Choosing the output format. Supports JSON or YAML.
     */
    @Parameter
    private Set<OutputFormat> outputFormats = Collections.singleton(OutputFormat.JSON);

    /**
     * Attach generated documentation as artifact to the Maven project. If true documentation will be deployed along
     * with other artifacts.
     */
    @Parameter(defaultValue = "false")
    private boolean attachSwaggerArtifact;

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    @Component
    private MavenProjectHelper projectHelper;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Reader reader = new Reader(swaggerConfig == null ? new Swagger() : swaggerConfig.createSwaggerModel());

        JaxRSScanner reflectiveScanner = new JaxRSScanner();
        if (resourcePackages != null && !resourcePackages.isEmpty()) {
            reflectiveScanner.setResourcePackages(resourcePackages);
        }

        Swagger swagger = reader.read(reflectiveScanner.classes());

        if (outputDirectory.mkdirs()) {
            getLog().debug("Created output directory " + outputDirectory);
        }

        outputFormats.forEach(format -> {
            try {
                File outputFile = new File(outputDirectory, "swagger." + format.name().toLowerCase());
                format.write(swagger, outputFile);
                if (attachSwaggerArtifact) {
                    projectHelper.attachArtifact(project, format.name().toLowerCase(), "swagger", outputFile);
                }
            } catch (IOException e) {
                throw new RuntimeException("Unable write Swagger document", e);
            }
        });
    }

}
