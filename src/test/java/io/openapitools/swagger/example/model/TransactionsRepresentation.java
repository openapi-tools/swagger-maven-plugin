package io.openapitools.swagger.example.model;

import java.util.Collection;
import java.util.Collections;

import io.openapitools.jackson.dataformat.hal.HALLink;
import io.openapitools.jackson.dataformat.hal.annotation.EmbeddedResource;
import io.openapitools.jackson.dataformat.hal.annotation.Link;
import io.openapitools.jackson.dataformat.hal.annotation.Resource;

/**
 * Represents a set of transactions as returned by the REST service.
 */
@Resource
public class TransactionsRepresentation {
    @EmbeddedResource("transactions")
    private Collection<TransactionRepresentation> transactions;

    @Link
    private HALLink self;

    public Collection<TransactionRepresentation> getTransactions() {
        return Collections.unmodifiableCollection(transactions);
    }

    public HALLink getSelf() {
        return self;
    }
}
