package com.example.demo.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.demo.Enitities.User;
import com.example.demo.Repositries.Urepo;


public class UserDetailServiceImpl implements UserDetailsService {
	@Autowired
	Urepo ur;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User findByEmail = ur.findByEmail(username);
		if (findByEmail == null) {
			throw new UsernameNotFoundException("Not found!!");
		}

		CustomUserDetailsService cd = new CustomUserDetailsService(findByEmail);
		return cd;
	}

}
