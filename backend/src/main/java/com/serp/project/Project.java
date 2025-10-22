package com.serp.project;

import com.serp.common.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity @Table(name="projects", schema="project")
public class Project extends BaseEntity {
  private UUID customerId;
  private String name;
  @Column(columnDefinition="text") private String description;
  private LocalDate startDate;
  private LocalDate endDate;
  public UUID getCustomerId(){ return customerId; } public void setCustomerId(UUID id){ this.customerId=id; }
  public String getName(){ return name; } public void setName(String n){ this.name=n; }
  public String getDescription(){ return description; } public void setDescription(String d){ this.description=d; }
  public LocalDate getStartDate(){ return startDate; } public void setStartDate(LocalDate d){ this.startDate=d; }
  public LocalDate getEndDate(){ return endDate; } public void setEndDate(LocalDate d){ this.endDate=d; }
}
