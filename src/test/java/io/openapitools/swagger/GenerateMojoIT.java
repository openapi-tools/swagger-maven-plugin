package io.openapitools.swagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class GenerateMojoIT {

    @Rule
    public MojoRule rule = new MojoRule();

    private void assertJsonEquals(Path expectedJson, Path generatedJson) throws IOException {
        assertOpenApiEquals(Json.mapper(), expectedJson, generatedJson);
    }

    private void assertYamlEquals(Path expectedYaml, Path generatedYaml) throws IOException {
        assertOpenApiEquals(Yaml.mapper(), expectedYaml, generatedYaml);
    }

    private void assertOpenApiEquals(ObjectMapper mapper, Path expected, Path generated) throws IOException {
        OpenAPI expectedAPI = mapper.readValue(expected.toFile(), OpenAPI.class);
        OpenAPI generatedAPI = mapper.readValue(generated.toFile(), OpenAPI.class);
        
        // OpenAPI.equals() performs a deep equal check of all its properties
        Assert.assertTrue(expectedAPI.equals(generatedAPI));
    }

    /**
     * Tests the generated Swagger specifications based on the given method parameters.
     * 
     * @param folder The base folder of the expected output.
     * @param basename The basename for the generated Swagger specifications.
     * @param pom The name of the POM file to be used for the test.
     * @param prettyPrinted If true, the generated JSON Swagger specification should be pretty-printed.
     * @param outputFormats The output formats that should be generated and checked.
     * 
     * @throws Exception If a JSON parsing or file-read exception happens.
     */
    private void testGenerate(String folder, String basename, String pom, boolean prettyPrinted, OutputFormat... outputFormats)
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
            Assert.assertEquals(prettyPrinted, json.contains("  "));
        }

        if (formats.contains(OutputFormat.YAML)) {
            Path expectedYaml = Paths.get("src/test/resources/expectedOutput", folder, basename + ".yaml");
            Path generatedYaml = Paths.get("target", folder, basename + ".yaml");
            assertYamlEquals(expectedYaml, generatedYaml);
        }
    }

    @Test
    public void testGenerateApi() throws Exception {
        testGenerate("api", "swagger", "generate-mojo-pom.xml", false, OutputFormat.JSON, OutputFormat.YAML);
    }

    @Test
    public void testGenerateDefaults() throws Exception {
        testGenerate("default", "swagger", "generate-mojo-defaults-pom.xml", false, OutputFormat.JSON);
    }

    @Test
    public void testGenerateFull() throws Exception {
        testGenerate("full", "open-api", "generate-mojo-full-pom.xml", false, OutputFormat.JSON, OutputFormat.YAML);
    }

    @Test
    public void testGenerateFullNoFileName() throws Exception {
        testGenerate("semifull", "swagger", "generate-mojo-full-nofilename-pom.xml", false, OutputFormat.JSON, OutputFormat.YAML);
    }

    @Test
    public void testGenerateFullPretty() throws Exception {
        testGenerate("fullpretty", "open-api", "generate-mojo-full-pretty-pom.xml", true, OutputFormat.JSON, OutputFormat.YAML);
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
