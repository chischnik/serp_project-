package com.serp.customer;

import com.serp.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity @Table(name="customers", schema="customer")
public class Customer extends BaseEntity {
  @NotBlank private String name;
  private String email;
  public String getName(){ return name; } public void setName(String n){ this.name=n; }
  public String getEmail(){ return email; } public void setEmail(String e){ this.email=e; }
}
