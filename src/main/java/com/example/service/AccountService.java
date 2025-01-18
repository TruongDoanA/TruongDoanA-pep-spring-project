package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account registerUser(Account account){
        if(account.getUsername().isBlank())
            throw new RuntimeException("Username cannot be blank");

        if(account.getPassword().length() < 4)
            throw new RuntimeException("Password must be at least 4 characters long");

        Optional<Account> optionalAccount = accountRepository.findByUsername(account.getUsername());
        if(optionalAccount.isPresent())
            throw new RuntimeException("Username has already been taken");
        return accountRepository.save(account);
        
    }

    public Account loginWithUsernameAndPassword(String username, String password){
        Optional<Account> optionalAccount = accountRepository.findByUsernameAndPassword(username, password);
        if(optionalAccount.isPresent()){
            return optionalAccount.get();
        } else {
            return null;
        }
    }
    
}
