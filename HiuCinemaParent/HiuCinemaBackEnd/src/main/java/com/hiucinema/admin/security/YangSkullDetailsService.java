package com.hiucinema.admin.security;

import com.hiucinema.admin.user.UserRepository;
import com.hiucinema.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class YangSkullDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user =  userRepository.getUserByEmail(email);
        if(user != null){
            return new YangSkullUserDetails(user);
        }

        throw new UsernameNotFoundException("Could not find user with email: "+email);

    }
}
