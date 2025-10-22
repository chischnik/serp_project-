package com.serp.auth;

import org.springframework.data.jpa.repository.*;
import java.util.*;

public interface UserRepository extends JpaRepository<UserEntity, java.util.UUID> {
  Optional<UserEntity> findByUsername(String username);
}
