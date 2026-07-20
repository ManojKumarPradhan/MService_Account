package com.bigob.accounts.service;

import com.bigob.accounts.dto.CustomerDTO;

public interface IAccountService {

    /**
     * Create a new account based on the provided AccountsDTO.
     *
     * @param customerDTO The data transfer object containing account details.
     * @return The ID of the newly created account.
     */
    Long createAccount(CustomerDTO customerDTO);

    CustomerDTO getCustomerByMobileNumber(String mobileNumber);

    boolean updateCustomer(CustomerDTO customerDTO);
    
    boolean deleteCustomer(String mobileNumber);

}
