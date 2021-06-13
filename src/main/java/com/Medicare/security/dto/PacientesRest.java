package com.Medicare.security.dto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Medicare.security.entity.pacientes;
import com.Medicare.security.jwt.JwtProvider;
import com.Medicare.security.repository.pacientesRepository;

@RestController
@RequestMapping("/pacientes")
@CrossOrigin("*")
public class PacientesRest {

	@Autowired
	private pacientesRepository pacientesDao;
	
	@Autowired
	JwtProvider jwtProvider;
	
	@PostMapping("/nuevoPaciente")
	public void nuevoPaciente(@RequestBody pacientes pacientes) {
		pacientesDao.save(pacientes);
	}
	
	@GetMapping("/listaPacientes")
	public List<pacientes> listaPacientes() {
		return pacientesDao.findAll();
	}
	
	@DeleteMapping("eliminarPaciente/{idPaciente}")
	public void eliminarMedicamento(@PathVariable("idPaciente") Integer idPaciente) {
		pacientesDao.deleteById(idPaciente);
	}
}
