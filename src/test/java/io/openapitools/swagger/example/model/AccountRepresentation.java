package io.openapitools.swagger.example.model;

import java.util.Collection;
import java.util.Collections;

import io.openapitools.jackson.dataformat.hal.HALLink;
import io.openapitools.jackson.dataformat.hal.annotation.EmbeddedResource;
import io.openapitools.jackson.dataformat.hal.annotation.Link;
import io.openapitools.jackson.dataformat.hal.annotation.Resource;
import io.swagger.annotations.ApiModelProperty;

/**
 * Represents a single as returned from REST service.
 */
@Resource
public class AccountRepresentation {
    private String regNo;
    private String accountNo;
    private String name;

    @EmbeddedResource("transactions")
    private Collection<TransactionRepresentation> transactions;

    @Link("account:transactions")
    private HALLink transactionsResource;

    @Link
    private HALLink self;

    public String getRegNo() {
        return regNo;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public String getName() {
        return name;
    }

    @ApiModelProperty("Embeds the latest transaction of account.")
    public Collection<TransactionRepresentation> getTransactions() {
        if (transactions == null) {
            return null;
        } else {
            return Collections.unmodifiableCollection(transactions);
        }
    }

    public HALLink getTransactionsResource() {
        return transactionsResource;
    }

    public HALLink getSelf() {
        return self;
    }
}
