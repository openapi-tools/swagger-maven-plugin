package io.openapitools.swagger;

import org.apache.maven.model.Build;

public class MavenProjectStub extends org.apache.maven.plugin.testing.stubs.MavenProjectStub {

    public MavenProjectStub() {
        Build b = new Build();
        b.setOutputDirectory("target/test-classes");
        setBuild(b);
    }

}
