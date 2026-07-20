package com.bigob.accounts.controller;

import com.bigob.accounts.dto.ResponseError;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bigob.accounts.constant.AccountsConstants;
import com.bigob.accounts.dto.CustomerDTO;
import com.bigob.accounts.dto.ResponseDTO;
import com.bigob.accounts.service.IAccountService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;


@Tag(
		name = "CRUD REST APIs for Accounts in Bigob",
		description = "CRUD REST APIs in Bigob to CREATE, UPDATE, FETCH AND DELETE account details"
)


@RestController
@RequestMapping(value = "/api/accounts", produces = { MediaType.APPLICATION_JSON_VALUE })
@AllArgsConstructor
@Validated
public class AccountController {

	private final IAccountService accountService;


	@Operation(
			summary = "Create Account REST API",
			description = "REST API to create new Customer &  Account inside EazyBank"
	)

	@ApiResponse(
			responseCode = "201",
			description = "HTTP Status CREATED"
	)
	@ApiResponse(
			responseCode = "500",
			description = "HTTP Status Internal Server Error",
			content = @Content(
					schema = @Schema(implementation = ResponseError.class)
			)
	)

	@PostMapping("/create")
	@RateLimiter(name = "postPutLimiter")
	public ResponseEntity<ResponseDTO<Long>> createAccount(@Valid @RequestBody CustomerDTO customerDTO) {

		Long accountId = accountService.createAccount(customerDTO);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ResponseDTO.<Long>builder().statusCode(AccountsConstants.STATUS_201)
						.statusMessage(AccountsConstants.MESSAGE_201).body(accountId).build());
	}

	@GetMapping(path = "/getByMObile")
	@Bulkhead(name = "getDeleteLimiter")
	public ResponseEntity<CustomerDTO> getCustomerByMobileNumber(
			@RequestParam @Pattern(regexp = "(^$|\\d{10})", message = "Mobile number should be 10 numbers") String mobileNumber) {
		CustomerDTO customerDTO = accountService.getCustomerByMobileNumber(mobileNumber);
		return ResponseEntity.status(HttpStatus.FOUND).body(customerDTO);
	}

	@PutMapping("/update")
	@RateLimiter(name = "postPutLimiter")
	public ResponseEntity<ResponseDTO<String>> updateCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
		boolean isUpdate = accountService.updateCustomer(customerDTO);
		if (isUpdate) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseDTO.<String>builder().statusCode(AccountsConstants.STATUS_200)
							.statusMessage(AccountsConstants.MESSAGE_200).body("Updated Successfully").build());
		} else {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.body(ResponseDTO.<String>builder().statusCode(AccountsConstants.STATUS_417)
							.statusMessage(AccountsConstants.MESSAGE_417_UPDATE).body("Updated Successfully").build());
		}
	}

	@DeleteMapping("/delete")
	@Bulkhead(name = "getDeleteLimiter")
	public ResponseEntity<ResponseDTO<String>> deleteCustomer(
			@RequestParam @Pattern(regexp = "(^$|\\d{10})", message = "Mobile number should be 10 numbers") String mobileNumber) {
		boolean isUpdate = accountService.deleteCustomer(mobileNumber);
		if (isUpdate) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseDTO.<String>builder().statusCode(AccountsConstants.STATUS_200)
							.statusMessage(AccountsConstants.MESSAGE_200).body("Delete Successfully").build());
		} else {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
					.body(ResponseDTO.<String>builder().statusCode(AccountsConstants.STATUS_417)
							.statusMessage(AccountsConstants.MESSAGE_417_DELETE).body("Delete Successfully").build());
		}
	}

}
