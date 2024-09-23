package doafacil.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import doafacil.entities.Profile;
import doafacil.entities.User;

@Service
public class UserService implements UserDetailsService {

	Profile profile = new Profile(Long.parseLong("1"), "Admin");
	User userTest = new User(Long.parseLong("1"), "Teste", "teste@teste.com", new BCryptPasswordEncoder().encode("123"), profile);
	
	public User getUserById(Long userId) {
		return userTest;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails userDetails = null;
		
		if(username.equals(this.userTest.getEmail())) 
			userDetails = this.userTest;
		
		return userDetails;
	}
}
