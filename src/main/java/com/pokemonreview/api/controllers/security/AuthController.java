package com.pokemonreview.api.controllers.security;

import com.pokemonreview.api.dto.security.AuthResponseDto;
import com.pokemonreview.api.dto.security.LoginDto;
import com.pokemonreview.api.dto.security.UserDto;
import com.pokemonreview.api.jwt.JWTGenerator;
import com.pokemonreview.api.models.security.RoleEntity;
import com.pokemonreview.api.models.security.UserEntity;
import com.pokemonreview.api.repository.security.RoleRepository;
import com.pokemonreview.api.repository.security.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JWTGenerator jwtGenerator;

//    public AuthController(UserRepository userRepository,
//                          RoleRepository roleRepository,
//                          PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.roleRepository = roleRepository;
//        this.passwordEncoder = passwordEncoder;
//    }

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            return new ResponseEntity<>("Username is taken!", HttpStatus.BAD_REQUEST);
        }

        UserEntity user = new UserEntity();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode((userDto.getPassword())));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());

        RoleEntity roles = roleRepository.findByName(userDto.getRole())
                .orElseGet(() -> {
                    System.out.println("Role 없음");
                    RoleEntity role = new RoleEntity();
                    role.setName(userDto.getRole());
                    return roleRepository.save(role);
                });
        user.setRoles(Collections.singletonList(roles));
        userRepository.save(user);

        return new ResponseEntity<>("User registered success!", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //token 생성
        String token = jwtGenerator.generateToken(authentication);

        AuthResponseDto authResponseDTO = new AuthResponseDto(token);
        authResponseDTO.setUsername(loginDto.getUsername());

        Optional<UserEntity> optionalUser =
                userRepository.findByUsername(loginDto.getUsername());
        if(optionalUser.isPresent()){
            UserEntity userEntity = optionalUser.get();
            authResponseDTO.setRole(userEntity.getRoles().get(0).getName());
        }
        return new ResponseEntity<>(authResponseDTO, HttpStatus.OK);
    }

}