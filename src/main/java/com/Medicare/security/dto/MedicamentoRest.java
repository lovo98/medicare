package com.Medicare.security.dto;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Medicare.security.entity.medicamentos;
import com.Medicare.security.jwt.JwtProvider;
import com.Medicare.security.repository.medicamentosRepository;

@RestController
@RequestMapping("/medicamentos")
@CrossOrigin("*")
public class MedicamentoRest {

	@Autowired
	private medicamentosRepository medicamentosDao;
	
	@Autowired
	JwtProvider jwtProvider;
	
	@PostMapping("/addMedicamento")
	public void addMedicamento(@RequestBody medicamentos medicamento) {
		medicamentosDao.save(medicamento);
	}
	
	@GetMapping("/listaMedicamentos")
	public List<medicamentos> listaMedicamentos() {
		return medicamentosDao.findAll();
	}
	
	@PutMapping("/editMedicamento")
	public void editPerson(@RequestBody medicamentos medicamento) {
		medicamentosDao.save(medicamento);
	}
	
	@DeleteMapping("eliminarMedicamento/{idMedicamento}")
	public void eliminarMedicamento(@PathVariable("idMedicamento") Integer idMedicamento) {
		medicamentosDao.deleteById(idMedicamento);
	}
}
