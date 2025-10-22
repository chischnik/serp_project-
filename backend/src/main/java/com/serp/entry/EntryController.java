package com.serp.entry;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/entries")
public class EntryController {
  private final EntryRepository repo;
  public EntryController(EntryRepository r){ this.repo = r; }

  @GetMapping
  public List<Entry> all(){ return repo.findAll(); }

  @GetMapping("/{id}")
  public ResponseEntity<Entry> one(@PathVariable UUID id){
    return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Entry> create(@RequestBody Entry e){
    return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(e));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Entry> update(@PathVariable UUID id, @RequestBody Entry in){
    return repo.findById(id).map(ex -> {
      ex.setProjectId(in.getProjectId());
      ex.setConsultantId(in.getConsultantId());
      ex.setWorkDate(in.getWorkDate());
      ex.setHours(in.getHours());
      ex.setNotes(in.getNotes());
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
