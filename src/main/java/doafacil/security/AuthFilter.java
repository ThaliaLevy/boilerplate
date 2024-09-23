package doafacil.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import doafacil.entities.User;
import doafacil.services.AuthService;
import doafacil.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthFilter extends OncePerRequestFilter {
	private final AuthService authService;
	private final UserService userService;
	
	public AuthFilter(AuthService authService, UserService userService) {
		this.authService = authService;
		this.userService = userService; 
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String header = request.getHeader("Authorization");
		String token = null;
		
		if(header != null && header.startsWith("Bearer")) 
			token = header.substring(7, header.length());
		
		if(authService.verifyToken(token)) {
			Long userId = authService.getUserId(token);
			User user = userService.getUserById(userId);											
			SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
		}
		
		filterChain.doFilter(request, response);
	}
}
