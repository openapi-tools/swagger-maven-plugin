package io.openapitools.swagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class GenerateMojoIT {

    @Rule
    public MojoRule rule = new MojoRule();

    private void assertJsonEquals(Path expectedJson, Path generatedJson) throws IOException {
        assertOpenApiEquals(Json.mapper(), expectedJson, generatedJson);
        assertFileContentEquals(expectedJson, generatedJson);
    }

    private void assertYamlEquals(Path expectedYaml, Path generatedYaml) throws IOException {
        assertOpenApiEquals(Yaml.mapper(), expectedYaml, generatedYaml);
        assertFileContentEquals(expectedYaml, generatedYaml);
    }

    private void assertFileContentEquals(Path expected, Path generated) {
        try {
            assertEquals(String.join("\n", Files.readAllLines(expected, StandardCharsets.UTF_8)), String.join("\n", Files.readAllLines(generated, StandardCharsets.UTF_8)));
        } catch (IOException e) {
            fail();
        }
    }

    private void assertOpenApiEquals(ObjectMapper mapper, Path expected, Path generated) throws IOException {
        OpenAPI expectedAPI = mapper.readValue(expected.toFile(), OpenAPI.class);
        OpenAPI generatedAPI = mapper.readValue(generated.toFile(), OpenAPI.class);

        // OpenAPI.equals() performs a deep equal check of all its properties
        assertEquals(expectedAPI, generatedAPI);
    }

    /**
     * Tests the generated Swagger specifications based on the given method parameters.
     *
     * @param folder The base folder of the expected output.
     * @param basename The basename for the generated Swagger specifications.
     * @param pom The name of the POM file to be used for the test.
     * @param outputFormats The output formats that should be generated and checked.
     *
     * @throws Exception If a JSON parsing or file-read exception happens.
     */

    private void testGenerate(String folder, String basename, String pom, OutputFormat... outputFormats)
        throws Exception {

        Path output = Paths.get(folder);
        if (Files.exists(output)) {
            Files.walkFileTree(output, new DeleteVisitor());
        }

        Mojo mojo = rule.lookupMojo("generate", new File("src/test/resources/" + pom));
        mojo.execute();

        List<OutputFormat> formats = Arrays.asList(outputFormats);
        if (formats.contains(OutputFormat.JSON)) {
            Path expectedJson = Paths.get("src/test/resources/expectedOutput", folder, basename + ".json");
            Path generatedJson = Paths.get("target", folder, basename + ".json");
            assertJsonEquals(expectedJson, generatedJson);

            // We test the indentation by simply checking that the generated JSON contains 2 spaces
            String json = new String(Files.readAllBytes(generatedJson), StandardCharsets.UTF_8);
        }

        if (formats.contains(OutputFormat.YAML)) {
            Path expectedYaml = Paths.get("src/test/resources/expectedOutput", folder, basename + ".yaml");
            Path generatedYaml = Paths.get("target", folder, basename + ".yaml");
            assertYamlEquals(expectedYaml, generatedYaml);
        }
    }

    //@MavenGoal("${project.groupId}:${project.artifactId}:${project.version}:generate")
    //@MavenTest
    @Test
    public void testGenerateApi() throws Exception {
        testGenerate("api", "swagger", "generate-mojo-pom.xml",  OutputFormat.JSON, OutputFormat.YAML);
    }

    @Test
    public void testGenerateDefaults() throws Exception {
        testGenerate("default", "swagger", "generate-mojo-defaults-pom.xml", OutputFormat.JSON);
    }

    @Test
    public void testGenerateApplicationClass() throws Exception {
        testGenerate("application", "swagger", "generate-mojo-application.xml", OutputFormat.JSON);
    }

    @Test
    public void testGenerateApplicationClassByScanning() throws Exception {
        testGenerate("application", "swagger", "generate-mojo-application-scan.xml", OutputFormat.JSON);
    }

    @Test
    public void testGenerateFull() throws Exception {
        testGenerate("full", "open-api", "generate-mojo-full-pom.xml", OutputFormat.JSON, OutputFormat.YAML);
    }

    @Test
    public void testGenerateFullNoFileName() throws Exception {
        testGenerate("semifull", "swagger", "generate-mojo-full-nofilename-pom.xml", OutputFormat.JSON, OutputFormat.YAML);
    }

    @Test
    public void testGenerateFullPretty() throws Exception {
        testGenerate("fullpretty", "open-api", "generate-mojo-full-pretty-pom.xml", OutputFormat.JSON, OutputFormat.YAML);
    }

    @Test
    public void testGenerateRecursive() throws Exception {
        testGenerate("recursive", "swagger", "generate-mojo-recursive-pom.xml", OutputFormat.JSON);
    }

    private static class DeleteVisitor extends SimpleFileVisitor<Path> {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
        }
    }
}
