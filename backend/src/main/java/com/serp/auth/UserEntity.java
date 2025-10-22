package com.serp.auth;

import jakarta.persistence.*;
import java.util.UUID;

@Entity @Table(name="users", schema="auth")
public class UserEntity {
  @Id private UUID id;
  @Column(unique=true, nullable=false) private String username;
  @Column(nullable=false, name="password_hash") private String passwordHash;
  private boolean enabled = true;
  @PrePersist public void pre(){ if(id==null) id = UUID.randomUUID(); }
  public UUID getId(){ return id; } public void setId(UUID id){ this.id=id; }
  public String getUsername(){ return username; } public void setUsername(String u){ this.username=u; }
  public String getPasswordHash(){ return passwordHash; } public void setPasswordHash(String p){ this.passwordHash=p; }
  public boolean isEnabled(){ return enabled; } public void setEnabled(boolean e){ this.enabled=e; }
}
