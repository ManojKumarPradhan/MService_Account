package com.bigob.accounts.service.impl;

import com.bigob.accounts.constant.AccountsConstants;
import com.bigob.accounts.dto.AccountsDTO;
import com.bigob.accounts.dto.CustomerDTO;
import com.bigob.accounts.entity.Accounts;
import com.bigob.accounts.entity.Customer;
import com.bigob.accounts.exception.CustomerAlreadyExistException;
import com.bigob.accounts.exception.ResourceNotFoundException;
import com.bigob.accounts.mapper.AccountMapper;
import com.bigob.accounts.mapper.CustomerMapper;
import com.bigob.accounts.repository.AccountRepository;
import com.bigob.accounts.repository.CustomerRepository;
import com.bigob.accounts.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    /**
     * Create a new account based on the provided AccountsDTO.
     *
     * @param customerDTO
     *         The data transfer object containing account details.
     * @return The ID of the newly created account.
     */
    @Override
    public Long createAccount(CustomerDTO customerDTO) {
        Customer customer = CustomerMapper.mapToCustomer(customerDTO, new Customer());
        Optional<Customer> existingCustomer = customerRepository.findByMobileNumber(customer.getMobileNumber());
        if (existingCustomer.isPresent()) {
            throw new CustomerAlreadyExistException("Customer with mobile number " + customer.getMobileNumber() + " already exists.");
        }
        Customer saveCustomer = customerRepository.save(customer);
        accountRepository.save(createNewAccount(saveCustomer));
        return saveCustomer.getId();
    }

    /**
     * Get customer details by mobile number.
     *
     * @param mobileNumber
     *         The mobile number of the customer.
     * @return CustomerDTO containing customer details.
     */
    @Override
    public CustomerDTO getCustomerByMobileNumber(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(()-> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
        Accounts account = accountRepository.findByCustomerId(customer.getId()).orElseThrow(() -> new ResourceNotFoundException("Account", "customerId", customer.getId().toString()));
        CustomerDTO customerDTO = CustomerMapper.mapToCustomerDto(customer, new CustomerDTO());
        customerDTO.setAccountsDTO(AccountMapper.mapToAccountsDto(account, new AccountsDTO()));
        return customerDTO;
    }

    /**
     * @param customer - Customer Object
     * @return the new account details
     */
    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);
        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        return newAccount;
    }

	@Override
	public boolean updateCustomer(CustomerDTO customerDTO) {
		AccountsDTO accountsDTO = customerDTO.getAccountsDTO();
		boolean isUpdated = false;
		if(accountsDTO!=null) {
			Accounts accounts = accountRepository.findById(accountsDTO.getAccountNumber())
					.orElseThrow(()-> new ResourceNotFoundException("Account", "Account Number", accountsDTO.getAccountNumber().toString()));
			AccountMapper.mapToAccounts(accountsDTO, accounts);
			accountRepository.save(accounts);
			Long customerID = accounts.getCustomerId();
			Customer customer = customerRepository.findById(customerID)
					.orElseThrow(()-> new ResourceNotFoundException("Customer", "Customer ID", customerID));
			customerRepository.save(customer);
			isUpdated = true;
		}
		return isUpdated;
	}

	@Override
	public boolean deleteCustomer(String mobileNumber) {
		Customer customer = customerRepository.findByMobileNumber(mobileNumber)
				.orElseThrow(()-> new ResourceNotFoundException("Customer", "Mobile Number", mobileNumber));
		customerRepository.deleteById(customer.getId());
		accountRepository.deleteByCustomerId(customer.getId());
		return true;
	}

}
