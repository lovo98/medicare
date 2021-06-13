package com.Medicare.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class medicamentos {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idMedicamento;
	
	@Column
	private String medicamento;

	public Integer getIdMedicamento() {
		return idMedicamento;
	}

	public void setIdMedicamento(Integer idMedicamento) {
		this.idMedicamento = idMedicamento;
	}

	public String getMedicamento() {
		return medicamento;
	}

	public void setMedicamento(String medicamento) {
		this.medicamento = medicamento;
	}
	
	
}
