package com.serp.common;

import jakarta.persistence.*;
import java.util.UUID;

@MappedSuperclass
public abstract class BaseEntity {
  @Id protected UUID id;
  @PrePersist public void pre(){ if(id==null) id = UUID.randomUUID(); }
  public UUID getId(){ return id; } public void setId(UUID id){ this.id = id; }
}
