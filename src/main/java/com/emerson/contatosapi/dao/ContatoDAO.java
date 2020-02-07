package com.emerson.contatosapi.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emerson.contatosapi.model.Contato;

public interface ContatoDAO extends JpaRepository<Contato, Integer> {

	boolean existsByEmail(String email);
	
}
