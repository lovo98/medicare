package com.Medicare.security.controller;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.Medicare.security.dto.NuevoUsuario;
import com.Medicare.security.entity.Rol;
import com.Medicare.security.entity.Usuario;
import com.Medicare.security.enums.RolNombre;
import com.Medicare.security.dto.JwtDto;
import com.Medicare.security.dto.LoginUsuario;
import com.Medicare.security.dto.Mensaje;
import com.Medicare.security.jwt.JwtProvider;
import com.Medicare.security.repository.UsuarioRepository;
import com.Medicare.security.service.RolService;
import com.Medicare.security.service.UsuarioService;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
	private final static Logger logger = LoggerFactory.getLogger(JwtProvider.class);
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	RolService rolService;
	
	@Autowired
	JwtProvider jwtProvider;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@PostMapping("nuevo")
	public ResponseEntity<?> nuevo(@Validated @RequestBody NuevoUsuario nuevoUsuario, BindingResult bindingResult) {
		if (bindingResult.hasErrors())
			return new ResponseEntity(new Mensaje("campos mal puestos"), HttpStatus.BAD_REQUEST);
		
		if (usuarioService.existsByNombreUsuario(nuevoUsuario.getNombreUsuario()))
				return new ResponseEntity(new Mensaje("Ese nombre ya existe"), HttpStatus.BAD_REQUEST); 
		
		if (usuarioService.existsByEmail(nuevoUsuario.getEmail()))
			return new ResponseEntity(new Mensaje("Ese email ya existe"), HttpStatus.BAD_REQUEST);
		
		byte[] bytesEncoded;
		Usuario usuario = new Usuario(nuevoUsuario.getNombre(), nuevoUsuario.getNombreUsuario(), nuevoUsuario.getEmail(), passwordEncoder.encode(nuevoUsuario.getPassword()));
				//bytesEncoded = Base64.encodeBase64(nuevoUsuario.getPassword())
				//Base64.getEncoder().withoutPadding().encodeToString(nuevoUsuario.getPassword().getBytes()));
		
		Set<Rol> roles = new HashSet<>();
				roles.add(rolService.getByRolNombre(RolNombre.ROLE_USER).get());
		if (nuevoUsuario.getRoles().contains("admin"))
			roles.add(rolService.getByRolNombre(RolNombre.ROLE_ADMIN).get());
		
		usuario.setRoles(roles);
		usuarioService.save(usuario);
		return new ResponseEntity(new Mensaje("usuario guardado"), HttpStatus.CREATED);
	}
	
	@PostMapping("login")
	public ResponseEntity<JwtDto> login(@Validated @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult) {
		if (bindingResult.hasErrors())
			return new ResponseEntity(new Mensaje("campos mal puestos"), HttpStatus.BAD_REQUEST);
		
		org.springframework.security.core.Authentication authentication = 
				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getPassword()));
		
		System.out.println(authentication);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtProvider.generateToken(authentication);
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());
		return new ResponseEntity(jwtDto, HttpStatus.OK);
	}
	
	@GetMapping("listaUsuarios")
	public List<Usuario>listaUsuarios() {
		List<Usuario>list = new ArrayList<>();
		List<Usuario>listreturn = new ArrayList<>();
		Usuario user = new Usuario();
		
		list =  usuarioRepository.findAll();
		for (Usuario usuario : list) {
			
			Base64.Decoder decoder = Base64.getDecoder();
			String password = new String(decoder.decode(usuario.getPassword()));
			usuario.setPassword(password);
			
			listreturn.add(usuario);
		}
		
		return listreturn;
	}
	
	@DeleteMapping("deleteUser/{rol_id}")
	public void deleteUser(@PathVariable("rol_id") Integer id) {
		usuarioRepository.deleteById(id);
	}
	
	
}
