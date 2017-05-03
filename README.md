# Swagger Maven Plugin

This plugin is intended to use the [Swagger Core library](https://github.com/swagger-api/swagger-core) to generate
OpenAPI documentation from a JAX-RS based REST service with as little change as possible. This allows for @SwaggerDefinition, @ReaderListener and ModelConverters to work the same way as with the core Swagger library.


# Status

Module is considered production ready.

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/dk.nykredit.swagger/swagger-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/dk.nykredit.swagger/swagger-maven-plugin/)
[![Javadoc](https://javadoc-emblem.rhcloud.com/doc/dk.nykredit.swagger/swagger-maven-plugin/badge.svg)](https://www.javadoc.io/doc/dk.nykredit.swagger/swagger-maven-plugin)

# Usage

To have Swagger generate the OpenAPI specifications as part of the build add in the plugin to the POM.

```xml
<build>
  <plugins>
    ...
    <plugin>
      <groupId>dk.nykredit.swagger</groupId>
      <artifactId>swagger-maven-plugin</artifactId>
      <configuration>
        <resourcePackages>
          <resourcePackage>dk.nykredit.swagger.example</resourcePackage>
          <resourcePackage>dk.nykredit.swagger.example.alternate</resourcePackage>
        </resourcePackages>
      </configuration>
    </plugin>
    ...
  </plugins>
</build>
```

This will run the generation in the prepare-package lifecycle stage of the Maven build.

## Specifying Packages with JAX-RS Endpoints

The packages containing JAX-RS endpoints must be configured using the resourcePackages element. See the minimal configuration above.

## Properties of Swagger model

Most general properties of the Swagger model is configurable using the swaggerConfig element. Note this may also be configured through the @SwaggerDefinition annotation - see [Customizing your auto-generated Swagger Definitions](http://swagger.io/customizing-your-auto-generated-swagger-definitions-in-1-5-x/).

```xml
<plugin>
  <groupId>dk.nykredit.swagger</groupId>
  <artifactId>swagger-maven-plugin</artifactId>
  <configuration>
    <swaggerConfig>
      <schemes>http,https</schemes>
      <host>services.nykredit.it</host>
      <basePath>/base/path</basePath>
      <info>
        <title>Title</title>
        <version>1.0.0</version>
        <termsOfService>Terms</termsOfService>
        <contact>
          <email>e@mail.com</email>
          <name>My Name</name>
          <url>https://google.com</url>
        </contact>
        <license>
          <url>https://license</url>
          <name>MIT</name>
        </license>
      </info>
      <descriptionFile>src/test/resources/descriptions.md</descriptionFile>
    </swaggerConfig>
```


## Deploying

The generated OpenAPI specifications may be installed and deployed as Maven artifact. To enable this add the configuration parameter attachSwaggerArtifact.

```xml
<plugin>
  <groupId>dk.nykredit.swagger</groupId>
  <artifactId>swagger-maven-plugin</artifactId>
  <configuration>
    <attachSwaggerArtifact>true</attachSwaggerArtifact>
```

# Acknowledgement

Thanks to [Yukai Kong](https://github.com/kongchen) for his work on
[Swagger Maven plugin](https://github.com/kongchen/swagger-maven-plugin). This plugin is heavily inspired by that.
