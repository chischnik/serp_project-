package com.serp.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/auth")
public class MeController {
  @GetMapping("/me")
  public Map<String,Object> me(){
    Authentication a = SecurityContextHolder.getContext().getAuthentication();
    Map<String,Object> m = new HashMap<>();
    if(a != null){
      m.put("name", a.getName());
      m.put("authenticated", a.isAuthenticated());
    } else {
      m.put("authenticated", false);
    }
    return m;
  }
}
