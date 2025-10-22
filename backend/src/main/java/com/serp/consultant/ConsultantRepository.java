package com.serp.consultant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface ConsultantRepository extends JpaRepository<Consultant, UUID> {}
