package com.example.spring.service;

import com.example.spring.dao.AccountRepository;
import com.example.spring.dto.AccountDTO;
import com.example.spring.model.Account;
import com.example.spring.model.AccountDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService {
    AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException(username);
        }
        return new AccountDetails(account);
    }

    public boolean addAccount(AccountDTO accountDTO) {
        Account existedAccount = accountRepository.findByUsername(accountDTO.getUsername());
        if (existedAccount != null) {
            return false;
        }
        Account account = new Account();
        String passwordEncoded = new BCryptPasswordEncoder().encode(accountDTO.getPassword());
        account.setUsername(accountDTO.getUsername());
        account.setPassword(passwordEncoded);
        accountRepository.save(account);
        return true;
    }
}
