package com.serp.project;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
  private final ProjectRepository repo;
  public ProjectController(ProjectRepository r){ this.repo = r; }

  @GetMapping public List<Project> all(){ return repo.findAll(); }

  @GetMapping("/{id}")
  public ResponseEntity<Project> one(@PathVariable UUID id){
    return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Project> create(@RequestBody Project p){
    return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(p));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Project> update(@PathVariable UUID id, @RequestBody Project in){
    return repo.findById(id).map(ex -> {
      ex.setName(in.getName());
      ex.setDescription(in.getDescription());
      ex.setStartDate(in.getStartDate());
      ex.setEndDate(in.getEndDate());
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
