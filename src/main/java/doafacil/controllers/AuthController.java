package doafacil.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import doafacil.dtos.auth.AuthDTO;
import doafacil.dtos.auth.TokenDTO;
import doafacil.services.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping
	public ResponseEntity<TokenDTO> authenticate(@RequestBody AuthDTO authForm) {
		return ResponseEntity.ok(authService.authenticate(authForm));
	}
}
