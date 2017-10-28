package io.openapitools.swagger.example.model;

import io.openapitools.jackson.dataformat.hal.HALLink;
import io.openapitools.jackson.dataformat.hal.annotation.Link;
import io.openapitools.jackson.dataformat.hal.annotation.Resource;

/**
 * Represents a single transaction as returned by the REST service.
 */
@Resource
public class TransactionRepresentation {
    private String id;
    private String description;
    private String amount;

    @Link
    private HALLink self;

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getAmount() {
        return amount;
    }

    public HALLink getSelf() {
        return self;
    }
}
