package com.tcc.CadeMeuBichinho.service;


import java.util.ArrayList;
import java.util.Collection;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tcc.CadeMeuBichinho.repository.UserRepository;
import com.tcc.CadeMeuBichinho.model.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority; 



@Service
@Transactional
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService{
   
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	    User user = userRepository.findByEmail(email);

	    if (user == null) {
	        throw new UsernameNotFoundException("Not found!");
	    }  
	    //Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
	    
	    return new org.springframework.security.core.userdetails.User
				(user.getEmail(), user.getPassword(), getGrantedAuthorities());
	}

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	private Collection<GrantedAuthority> getGrantedAuthorities(){
    	Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    	GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("Role_User");
         grantedAuthorities.add(grantedAuthority);

        return grantedAuthorities;
    } 
}
