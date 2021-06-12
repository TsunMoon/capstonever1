package com.example.demo.jwt;

import com.example.demo.entity.Account;
import com.example.demo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerUserDetailService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountByPhone(phoneNumber);
        List<GrantedAuthority> listGrantedAuthorities = new ArrayList<>();
        listGrantedAuthorities.add(new SimpleGrantedAuthority(account.getRole().getName()));
        return new User(account.getPhone(), account.getPassword(),listGrantedAuthorities );
    }
}
