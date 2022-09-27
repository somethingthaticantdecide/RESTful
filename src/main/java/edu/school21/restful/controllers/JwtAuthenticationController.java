package edu.school21.restful.controllers;

import java.util.Objects;

import edu.school21.restful.models.User;
import edu.school21.restful.models.dto.UserDto;
import edu.school21.restful.services.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import edu.school21.restful.configs.JwtTokenUtil;
import edu.school21.restful.models.JwtRequest;
import edu.school21.restful.models.JwtResponse;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

	private final AuthenticationManager authenticationManager;
	private final JwtTokenUtil jwtTokenUtil;
	private final UsersService usersService;
	private final PasswordEncoder passwordEncoder;

	public JwtAuthenticationController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UsersService usersService, PasswordEncoder passwordEncoder) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenUtil = jwtTokenUtil;
		this.usersService = usersService;
		this.passwordEncoder = passwordEncoder;
	}

	private void authenticate(String username, String password) throws Exception {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		User user = usersService.loadUserByUsername(authenticationRequest.getUsername());
		final String token = jwtTokenUtil.generateToken(user);
		return ResponseEntity.ok(new JwtResponse(token));
	}

	@PostMapping("/signUp")
	public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
		User user = usersService.addNewUser(userDto);
		final String token = jwtTokenUtil.generateToken(user);
		return ResponseEntity.ok(new JwtResponse(token));
	}
}
