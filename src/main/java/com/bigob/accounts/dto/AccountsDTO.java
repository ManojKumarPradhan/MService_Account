package com.bigob.accounts.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountsDTO {

	@NotEmpty
	@Pattern(regexp = "(^$|[0-9]{10})", message = "Account number should be 10 numbers")
    private Long accountNumber;

	@NotEmpty(message = "Branch Address can not be empty")
    private String branchAddress;

	@NotEmpty(message = "Account Type can not be empty")
    private String accountType;
}
