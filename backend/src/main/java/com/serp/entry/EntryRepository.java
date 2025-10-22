package com.serp.entry;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface EntryRepository extends JpaRepository<Entry, UUID> {}
