package loja.springboot.model;


import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "horas", indexes = {@Index(name = "idx_hora_minuto", columnList = "hora_minuto")})
public class Horario extends AbstractEntity {
	
	@Column(name = "hora_minuto", unique = true, nullable = false)
	private LocalTime horaMinuto;

	public LocalTime getHoraMinuto() {
		return horaMinuto;
	}

	public void setHoraMinuto(LocalTime horaMinuto) {
		this.horaMinuto = horaMinuto;
	}
}
