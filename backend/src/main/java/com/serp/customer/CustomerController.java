package com.serp.customer;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
  private final CustomerRepository repo;
  public CustomerController(CustomerRepository r){ this.repo = r; }

  @GetMapping public List<Customer> all(){ return repo.findAll(); }

  @GetMapping("/{id}")
  public ResponseEntity<Customer> one(@PathVariable UUID id){
    return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Customer> create(@RequestBody Customer c){
    return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(c));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Customer> update(@PathVariable UUID id, @RequestBody Customer in){
    return repo.findById(id).map(ex -> {
      ex.setName(in.getName());
      ex.setEmail(in.getEmail());
      return ResponseEntity.ok(repo.save(ex));
    }).orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id){
    if(!repo.existsById(id)) return ResponseEntity.notFound().build();
    repo.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
