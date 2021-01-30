package com.contactura.contactura.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contactura.contactura.model.Contactura;
import com.contactura.contactura.repository.ContacturaRepository;

@CrossOrigin()
@RestController
@RequestMapping({"/contactura"})
public class ContacturaController {
	@Autowired
	private ContacturaRepository repository;
	
//	1° possivel quebra do parkinho
//	Fluxo semelhante ao implements que define que este controlador com seus métodos
//	Será acessado atraves do repositorio
//	ContacturaController(ContacturaRepository contacturaRepository){
//		this.repository = contacturaRepository;
//	}
	
	
//	List All
	@GetMapping
//	http://localhost:8090/contactura
	public List findAll(){
		return repository.findAll();
	}
	
//	Find by id - Busca valor pelo id especifico
	@GetMapping(value = "{id}")
//	http://localhost:8090/contactura/1
	public ResponseEntity findById(@PathVariable long id){
		return repository.findById(id)
				.map(record -> ResponseEntity.ok().body(record))
				.orElse(ResponseEntity.notFound().build());		
	}
	
//	Create
	@PostMapping
//	http://localhost:8090/contactura
	public Contactura create(@RequestBody Contactura contactura){
		return repository.save(contactura);
	}
	
//	Update
	@PutMapping(value = "{id}")
//	 http://localhost:8090/contactura/2
//	2° possivel quebra do parkinho
//	public ResponseEntity update(@PathVariable("id") long id, @RequestBody Contactura contactura) {
	public ResponseEntity update(@PathVariable long id, 
			@RequestBody Contactura contactura){
		return repository.findById(id)
				.map(record -> {
					record.setName(contactura.getName());
					record.setEmail(contactura.getEmail());
					record.setPhone(contactura.getPhone());
					Contactura update = repository.save(record);
					return ResponseEntity.ok().body(update);
				}).orElse(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping(path = {"/{id}"})
//	 http://localhost:8090/contacts/1
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity <?> delete(@PathVariable long id){
		return repository.findById(id)
				.map(record -> {
					repository.deleteById(id);
					return ResponseEntity.ok().body("Deletado com sucesso");
					
				}).orElse(ResponseEntity.notFound().build());
	}
	
}
