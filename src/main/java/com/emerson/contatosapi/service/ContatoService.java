package com.emerson.contatosapi.service;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emerson.contatosapi.dao.ContatoDAO;
import com.emerson.contatosapi.exception.ContatoInvalidoException;
import com.emerson.contatosapi.model.Contato;

@Service
public class ContatoService {

	@Autowired
	private ContatoDAO contatos;
	
	public Contato salvar(Contato contato) throws ContatoInvalidoException {
		if (contato != null) {
			if (Strings.isBlank(contato.getNome())) {
				throw new ContatoInvalidoException("O nome é obrigatório");
			} else if (Strings.isBlank(contato.getEmail())) {
				throw new ContatoInvalidoException("O e-mail é obrigatório");
			} else if (!contato.getEmail().matches("^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[A-z]{2,})$")) {
				throw new ContatoInvalidoException("E-mail em formato inválido");
			} else if (contato.getId() == null && this.contatos.existsByEmail(contato.getEmail())) {
				throw new ContatoInvalidoException("Já existe contato com o e-mail informado");
			} else {
				return this.contatos.save(contato);
			}
		} else {
			throw new ContatoInvalidoException("Nenhum contato recebido");
		}
	}
	
}
