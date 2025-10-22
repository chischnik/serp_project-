package com.serp.consultant;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/consultants")
public class ConsultantController {
  private final ConsultantRepository repo;
  public ConsultantController(ConsultantRepository r){ this.repo = r; }

  @GetMapping public List<Consultant> all(){ return repo.findAll(); }

  @GetMapping("/{id}")
  public ResponseEntity<Consultant> one(@PathVariable UUID id){
    return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Consultant> create(@RequestBody Consultant c){
    return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(c));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Consultant> update(@PathVariable UUID id, @RequestBody Consultant in){
    return repo.findById(id).map(ex -> {
      ex.setFirstName(in.getFirstName());
      ex.setLastName(in.getLastName());
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
