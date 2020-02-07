package com.emerson.contatosapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "contatos")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Contato {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nome;
	
	@Column(unique = true)
	private String email;
	
	public Contato() {}
	public Contato(String nome, String email) {
		this.nome = nome;
		this.email = email;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {
		return "Contato [id=" + id + ", nome=" + nome + ", email=" + email + "]";
	}
	
}
