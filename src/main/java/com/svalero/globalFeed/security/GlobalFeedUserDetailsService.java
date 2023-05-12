package com.svalero.globalFeed.security;

import com.svalero.globalFeed.domain.Role;
import com.svalero.globalFeed.domain.User;
import com.svalero.globalFeed.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GlobalFeedUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User person = userService.findByUsername(username);
        if(person == null){
            throw new UsernameNotFoundException("Invalid username/password");
        }

        List<GrantedAuthority> authorities = getUserAuthority(person.getRoles());
        return buildUserForAuthentication(person, authorities);
    }

    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        Set<GrantedAuthority> roles = new HashSet<>();
        userRoles.forEach(role -> roles.add(new SimpleGrantedAuthority(role.getName())));
        return new ArrayList<>(roles);
    }

    private UserDetails buildUserForAuthentication(User person, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(person.getUsername(), person.getPassword(),
                person.isActive(), true, true, true, authorities);
    }

}
