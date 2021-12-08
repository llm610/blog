package com.pro.blog.admin.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AuthService {
     boolean auth(HttpServletRequest request, Authentication authentication);
}
