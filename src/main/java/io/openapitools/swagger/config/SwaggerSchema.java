package io.openapitools.swagger.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.media.Discriminator;
import org.apache.maven.plugins.annotations.Parameter;
import io.swagger.v3.oas.models.media.Schema;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class SwaggerSchema {
    /**
     * The name of the schema or property.
     **/
    @Parameter
    private String name;

    /**
     * A title to explain the purpose of the schema.
     **/
    @Parameter
    private String title;

    /**
     * Constrains a value such that when divided by the multipleOf, the remainder must be an integer.  Ignored if the value is 0.
     **/
    @Parameter
    private BigDecimal multipleOf;

    /**
     * Sets the maximum numeric value for a property.  Ignored if the value is an empty string.
     **/
    @Parameter
    private BigDecimal maximum;

    /**
     * if true, makes the maximum value exclusive, or a less-than criteria.
     **/
    @Parameter
    private Boolean exclusiveMaximum;

    /**
     * Sets the minimum numeric value for a property.  Ignored if the value is an empty string or not a number.
     **/
    @Parameter
    private BigDecimal minimum;

    /**
     * If true, makes the minimum value exclusive, or a greater-than criteria.
     **/
    @Parameter
    private Boolean exclusiveMinimum;

    /**
     * Sets the maximum length of a string value.  Ignored if the value is negative.
     **/
    @Parameter
    private Integer maxLength;

    /**
     * Sets the minimum length of a string value.  Ignored if the value is negative.
     **/
    @Parameter
    private Integer minLength;

    /**
     * A pattern that the value must satisfy. Ignored if the value is an empty string.
     **/
    @Parameter
    private String pattern;

    /**
     * Constrains the number of arbitrary properties when additionalProperties is defined.  Ignored if value is 0.
     **/
    @Parameter
    private Integer maxProperties;

    /**
     * Constrains the number of arbitrary properties when additionalProperties is defined.  Ignored if value is 0.
     **/
    @Parameter
    private Integer minProperties;

    /**
     * Allows multiple properties in an object to be marked as required.
     **/
    @Parameter
    private String[] requiredProperties;

    /**
     * Returns the properties property from a Schema instance.
     **/
    @Parameter
    private Map<String, Schema> properties;

    /**
     * Returns the additionalProperties property from a Schema instance. Can be either a Boolean or a Schema
     **/
    @Parameter
    private Object additionalProperties;

    /**
     * Mandates that the annotated item is required or not.
     **/
    @Parameter
    private List<String> required;

    /**
     * A description of the schema.
     **/
    @Parameter
    private String description;

    /**
     * Provides an optional override for the format.  If a consumer is unaware of the meaning of the format, they shall fall back to using the basic type without format.  For example, if \&quot;type: integer, format: int128\&quot; were used to designate a very large integer, most consumers will not understand how to handle it, and fall back to simply \&quot;type: integer\&quot;
     **/
    @Parameter
    private String format;

    /**
     * References a schema definition in an external OpenAPI document.
     **/
    @Parameter
    private String ref;

    /**
     * If true, designates a value as possibly null.
     **/
    @Parameter
    private Boolean nullable;

    /**
     * Sets whether the value should only be read during a response but not read to during a request.
     *
     * @deprecated As of 2.0.0, replaced by {@link #accessMode}
     *
     **/
    @Deprecated
    @Parameter
    private Boolean readOnly;

    /**
     * Sets whether a value should only be written to during a request but not returned during a response.
     *
     * @deprecated As of 2.0.0, replaced by {@link #accessMode}
     **/
    @Deprecated
    @Parameter
    private Boolean writeOnly;

    /**
     * Allows to specify the access mode (AccessMode.READ_ONLY, READ_WRITE)
     *
     * AccessMode.READ_ONLY: value will only be written to during a request but not returned during a response.
     * AccessMode.WRITE_ONLY: value will only be written to during a request but not returned during a response.
     * AccessMode.READ_WRITE: value will be written to during a request and returned during a response.
     *
     */
    @Parameter
    private AccessMode accessMode;

    /**
     * Provides an example of the schema.  When associated with a specific media type, the example string shall be parsed by the consumer to be treated as an object or an array.
     **/
    @Parameter
    private Object example;

    /**
     * Additional external documentation for this schema.
     **/
    @Parameter
    private ExternalDocumentation externalDocs;

    /**
     * Specifies that a schema is deprecated and should be transitioned out of usage.
     **/
    @Parameter
    private Boolean deprecated;

    /**
     * Provides an override for the basic type of the schema.  Must be a valid type per the OpenAPI Specification.
     **/
    @Parameter
    private String type;

    /**
     * Provides a list of allowable values.  This field map to the enum property in the OAS schema.
     */
    @Parameter
    private String[] allowableValues;

    /**
     * Provides a default value.
     */
    @Parameter
    private String defaultValue;

    /**
     * Provides a discriminator property value.
     */
    @Parameter
    private String discriminatorProperty;

    /**
     * Provides discriminator mapping values.
     */
    @Parameter
    private Map<String, String> discriminatorMapping;

    /**
     * Allows schema to be marked as hidden.
     */
    @Parameter
    private Boolean hidden;

    /**
     * Allows enums to be resolved as a reference to a scheme added to components section.
     */
    @Parameter
    private Boolean enumAsRef;

    /**
     * An array of the sub types inheriting from this model.
     */
    @Parameter
    private Schema<?>[] subTypes;

    /**
     * The list of optional extensions
     */
    @Parameter
    private Map<String, Object> extensions;

    enum AccessMode {
        AUTO,
        READ_ONLY,
        WRITE_ONLY,
        READ_WRITE;
    }

    @Parameter
    private String $ref;

    public <T> Schema<T> createSchemaModel() {
        final Schema<T> schema = new Schema<>();

        schema.setName(name);
        schema.setTitle(title);
        schema.setType(type);
        schema.setDefault(defaultValue);
        schema.setExample(example);
        schema.setDescription(description);
        schema.setRequired(required);
        schema.setPattern(pattern);
        schema.setFormat(format);
        schema.setExternalDocs(externalDocs);
        schema.setNullable(nullable);
        schema.setReadOnly(readOnly);
        schema.setWriteOnly(writeOnly);
        schema.setDeprecated(deprecated);

        if (multipleOf != null) {
            schema.setMultipleOf(multipleOf);
        }

        if (maximum != null) {
            schema.setMaximum(maximum);
        }
        schema.setExclusiveMaximum(exclusiveMaximum);
        schema.setMaxLength(maxLength);

        if (minimum != null) {
            schema.setMinimum(minimum);
        }
        schema.setExclusiveMinimum(exclusiveMinimum);
        schema.setMinLength(minLength);

        if (properties != null && !properties.isEmpty()) {
            schema.setProperties(properties);
        }
        schema.setMaxProperties(maxProperties);
        schema.setMinProperties(minProperties);
        if (additionalProperties != null) {
            schema.setAdditionalProperties(additionalProperties);
        }

        if (extensions != null && !extensions.isEmpty()) {
            schema.setExtensions(extensions);
        }

        if (discriminatorProperty != null) {
            final Discriminator discriminator = new Discriminator();
            discriminator.setPropertyName(discriminatorProperty);
            if (discriminatorMapping != null) {
                discriminator.setMapping(discriminatorMapping);
            }
            schema.setDiscriminator(discriminator);
        }

        // Alternative to setting all above properties: reference to other component
        schema.set$ref($ref);


        return schema;
    }
}
