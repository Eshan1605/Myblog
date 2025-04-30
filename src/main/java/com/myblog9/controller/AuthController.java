package com.myblog9.controller;

import com.myblog9.entity.Role;
import com.myblog9.entity.User;
import com.myblog9.payload.JWTAuthResponse;
import com.myblog9.payload.LoginDto;
import com.myblog9.payload.SignUpDto;
import com.myblog9.repository.RoleRepository;
import com.myblog9.repository.UserRepository;
import com.myblog9.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    //http://localhost:8080/api/auth

    @Autowired
    private UserRepository userrepo;

    @Autowired
    private PasswordEncoder passwordencoder;

    @Autowired
    private RoleRepository rolerepo;

    @Autowired
    private AuthenticationManager authenticationmanager;

    @Autowired
    private JwtTokenProvider tokenProvider;


    //http://localhost:8080/api/auth/signup
    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody SignUpDto signupdto){

        if(userrepo.existsByUsername(signupdto.getUsername())){
            return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
        }

        if(userrepo.existsByEmail(signupdto.getEmail())){
            return new ResponseEntity<>("Email id already exists",HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setName(signupdto.getName());
        user.setEmail(signupdto.getEmail());
        user.setUsername(signupdto.getUsername());
        user.setPassword(passwordencoder.encode(signupdto.getPassword()));

        Role roles = rolerepo.findByname("ROLE_USER").get();
        Set<Role> role = new HashSet<>();
        role.add(roles);
        user.setRoles(role);

        User saveduser = userrepo.save(user);

        SignUpDto dto = new SignUpDto();
        dto.setId(saveduser.getId());
        dto.setName(saveduser.getName());
        dto.setEmail(saveduser.getEmail());
        dto.setUsername(saveduser.getName());

        return new ResponseEntity<>(dto,HttpStatus.CREATED);
    }

    //http://localhost:8080/api/auth/signin
    @PostMapping("/signin")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto logindto){

        Authentication authentication = authenticationmanager.authenticate(
                new UsernamePasswordAuthenticationToken(logindto.getUsernameOrEmail(),logindto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // get token form tokenProvider
        String token = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTAuthResponse(token));


    }
}
