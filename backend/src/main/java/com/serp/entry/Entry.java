package com.serp.entry;

import com.serp.common.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "entries", schema = "entry")
public class Entry extends BaseEntity {
  private UUID projectId;
  private UUID consultantId;
  private LocalDate workDate;
  private BigDecimal hours;
  @Column(columnDefinition = "text")
  private String notes;

  public UUID getProjectId() { return projectId; }
  public void setProjectId(UUID id) { this.projectId = id; }

  public UUID getConsultantId() { return consultantId; }
  public void setConsultantId(UUID id) { this.consultantId = id; }

  public LocalDate getWorkDate() { return workDate; }
  public void setWorkDate(LocalDate d) { this.workDate = d; }

  public BigDecimal getHours() { return hours; }
  public void setHours(BigDecimal h) { this.hours = h; }

  public String getNotes() { return notes; }
  public void setNotes(String n) { this.notes = n; }
}
