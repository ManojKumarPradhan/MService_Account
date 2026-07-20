package com.bigob.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        name = "Customer",
        description = "Schema to hold Customer and Account information"
)
public class CustomerDTO {

	@NotEmpty(message = "Name can not be null")
	@Size(min = 4, max = 25)
    @Schema(
            description = "Name of the customer", example = "Eazy Bytes"
    )
    private String name;

	@NotEmpty(message = "Email can not be null")
	@Email(message = "Email should be valid")
    @Schema(
            description = "Email address of the customer", example = "tutor@eazybytes.com"
    )
    private String email;

    @NotEmpty(message = "Mobile Number can not be null")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number should be 10 numbers")
    private String mobileNumber;

    private AccountsDTO accountsDTO;
}
