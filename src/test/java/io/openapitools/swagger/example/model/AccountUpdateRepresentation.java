package io.openapitools.swagger.example.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Creation and update of account.
 */
public class AccountUpdateRepresentation {

    @Pattern(regexp = "^[0-9]{4}$")
    private String regNo;

    @Pattern(regexp = "^[0-9]+$")
    private String accountNo;

    @NotNull
    @Pattern(regexp = ".{1,40}")
    private String name;

    public String getRegNo() {
        return regNo;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public String getName() {
        return name;
    }
}
