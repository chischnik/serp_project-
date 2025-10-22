package com.serp.consultant;

import com.serp.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity @Table(name="consultants", schema="consultant")
public class Consultant extends BaseEntity {
  @NotBlank private String firstName;
  @NotBlank private String lastName;
  private String email;
  public String getFirstName(){ return firstName; } public void setFirstName(String v){ this.firstName=v; }
  public String getLastName(){ return lastName; } public void setLastName(String v){ this.lastName=v; }
  public String getEmail(){ return email; } public void setEmail(String e){ this.email=e; }
}
