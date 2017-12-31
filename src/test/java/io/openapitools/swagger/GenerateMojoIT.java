package io.openapitools.swagger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Rule;
import org.junit.Test;

public class GenerateMojoIT {

    @Rule
    public MojoRule rule = new MojoRule();

    @Test
    public void testGenerate() throws Exception {
        Path output = Paths.get("target/api");
        if (Files.exists(output)) {
            Files.walkFileTree(output, new DeleteVisitor());
        }

        Mojo mojo = rule.lookupMojo("generate", new File("src/test/resources/generate-mojo-pom.xml"));
        mojo.execute();

        File swaggerYaml = new File("target/api/swagger.yaml");
        assertTrue(swaggerYaml.exists());

        File swaggerJson = new File("target/api/swagger.json");
        assertTrue(swaggerJson.exists());

        String contents = new String(Files.readAllBytes(swaggerJson.toPath()), StandardCharsets.UTF_8);
        assertFalse(contents.contains("\"/alternate\":"));
        assertTrue(contents.contains("\"/accounts/{regNo}-{accountNo}\":"));
    }

    @Test
    public void testGenerateDefaults() throws Exception {
        Path output = Paths.get("target/default");
        if (Files.exists(output)) {
            Files.walkFileTree(output, new DeleteVisitor());
        }

        Mojo mojo = rule.lookupMojo("generate", new File("src/test/resources/generate-mojo-defaults-pom.xml"));
        mojo.execute();

        File swaggerJson = new File("target/default/swagger.json");
        assertTrue(swaggerJson.exists());
        assertTrue(swaggerJson.length() > 3000);
        String contents = new String(Files.readAllBytes(swaggerJson.toPath()), StandardCharsets.UTF_8);
        assertTrue(contents.contains("\"/alternate\":"));
        assertFalse(contents.contains("Manipulator Title"));
        assertTrue(contents.contains("Description from ReaderListener"));
    }

    @Test
    public void testGenerateFull() throws Exception {
        Path output = Paths.get("target/full");
        if (Files.exists(output)) {
            Files.walkFileTree(output, new DeleteVisitor());
        }

        Mojo mojo = rule.lookupMojo("generate", new File("src/test/resources/generate-mojo-full-pom.xml"));
        mojo.execute();

        File swaggerJson = new File("target/full/open-api.json");
        assertTrue(swaggerJson.exists());
        assertTrue(swaggerJson.length() > 3000);

        File swaggerYaml = new File("target/full/open-api.yaml");
        assertTrue(swaggerYaml.exists());
        assertTrue(swaggerYaml.length() > 3000);
    }

    @Test
    public void testGenerateFullNoFileName() throws Exception {
        Path output = Paths.get("target/semifull");
        if (Files.exists(output)) {
            Files.walkFileTree(output, new DeleteVisitor());
        }

        Mojo mojo = rule.lookupMojo("generate", new File("src/test/resources/generate-mojo-full-nofilename-pom.xml"));
        mojo.execute();

        File swaggerJson = new File("target/semifull/swagger.json");
        assertTrue(swaggerJson.exists());
        assertTrue(swaggerJson.length() > 3000);

        File swaggerYaml = new File("target/semifull/swagger.yaml");
        assertTrue(swaggerYaml.exists());
        assertTrue(swaggerYaml.length() > 3000);
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
