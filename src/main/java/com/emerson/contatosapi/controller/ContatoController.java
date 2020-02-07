package com.emerson.contatosapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.emerson.contatosapi.dao.ContatoDAO;
import com.emerson.contatosapi.exception.ContatoInvalidoException;
import com.emerson.contatosapi.model.Contato;
import com.emerson.contatosapi.service.ContatoService;

@RestController
@RequestMapping(value = "/contatos")
//@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:5500"})
public class ContatoController {

	@Autowired private ContatoDAO contatos;
	@Autowired private ContatoService contatoService;
	
	@GetMapping
	public List<Contato> contatos() {
		return this.contatos.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Contato> obterContato(@PathVariable(required = false) Integer id) {
		if (id == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O ID é obrigatório");
		} else {
			Optional<Contato> contato = this.contatos.findById(id);
			
			if (!contato.isPresent()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum contato encontrado com o ID fornecido");
			} else {
				return ResponseEntity.ok(contato.get());
			}
		}
	}
	
	@PostMapping
	public ResponseEntity<Contato> adicionar(@RequestBody Contato contato) {
		try {
			return ResponseEntity.ok(this.contatoService.salvar(contato));
		} catch (ContatoInvalidoException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@PutMapping
	public ResponseEntity<Contato> atualizar(@RequestBody Contato contato) {
		if (contato.getId() == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "O ID é obrigatório");
		} else if (!this.contatos.existsById(contato.getId())) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Este contato não existe, talvez ele foi excluído");
		}
		
		String emailAntigo = this.contatos.getOne(contato.getId()).getEmail();
		String emailAtual = contato.getEmail();
		if (!emailAtual.equals(emailAntigo) && this.contatos.existsByEmail(contato.getEmail())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Já existe contato com o e-mail informado");
		}
		
		try {			
			Contato contatoNovo = this.contatoService.salvar(contato);
			return ResponseEntity.ok(contatoNovo);
		} catch (ContatoInvalidoException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Contato> excluir(@PathVariable(required = false) Integer id) {
		if (id == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O ID é obrigatório");
		} else if (!this.contatos.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum contato encontrado com o ID fornecido");
		}
		
		this.contatos.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/gerar/{prefixoEmail}/{inicioId}/{fimId}")
	public ResponseEntity<Contato> gerar(@PathVariable String prefixoEmail, 
												@PathVariable Integer inicioId, 
												@PathVariable Integer fimId, 
			HttpServletResponse response) 
	{
		List<Contato> contatosParaSalvar = new ArrayList<Contato>();
		
		if (!Strings.isBlank(prefixoEmail) && inicioId != null && fimId != null) {
			for (int i = inicioId; i <= fimId; i++) {
				contatosParaSalvar.add(new Contato(prefixoEmail+"-"+i, prefixoEmail+"-"+i+"@email.com"));
			}
			this.contatos.saveAll(contatosParaSalvar);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe 'nome', 'inicioId' e 'fimId'.");
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
}
