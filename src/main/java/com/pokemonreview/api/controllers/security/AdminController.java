package com.pokemonreview.api.controllers.security;

import com.pokemonreview.api.dto.PageResponse;
import com.pokemonreview.api.dto.security.UserDto;
import com.pokemonreview.api.exceptions.ResourceNotFoundException;
import com.pokemonreview.api.models.security.UserEntity;
import com.pokemonreview.api.repository.security.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public PageResponse<?> getAllUsers(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "2", required = false) int pageSize) {

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
        Page<UserEntity> userEntityPage = userRepository.findAll(pageable);
        List<UserEntity> listOfUser = userEntityPage.getContent();
        List<UserDto> userDtoList =
                listOfUser.stream()
                    .map(entity -> UserDto.builder()
                    .id(entity.getId())
                    .username(entity.getUsername())
                    .firstName(entity.getFirstName())
                    .lastName(entity.getLastName())
                    .role(entity.getRoles().get(0).getName())
                    .build())
                    .collect(Collectors.toList());

        PageResponse<UserDto> userResponse = new PageResponse<>();
        userResponse.setContent(userDtoList);
        userResponse.setPageNo(userEntityPage.getNumber());
        userResponse.setPageSize(userEntityPage.getSize());
        userResponse.setTotalElements(userEntityPage.getTotalElements());
        userResponse.setTotalPages(userEntityPage.getTotalPages());
        userResponse.setLast(userEntityPage.isLast());

        return userResponse;
    }

    @GetMapping("/{id}")
	//@PreAuthorize("hasAuthority('ROLE_USER')")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public UserDto getUser(@PathVariable("id") int userId) {
        UserEntity existUser = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return mapToDto(existUser);
    }

    private UserDto mapToDto(UserEntity userEntity) {
        UserDto userDto = new UserDto();
        userDto.setId(userEntity.getId());
        userDto.setUsername(userEntity.getUsername());
				userDto.setFirstName(userEntity.getFirstName());
        userDto.setLastName(userEntity.getLastName());
        userDto.setPassword(userEntity.getPassword());
        userDto.setRole(userEntity.getRoles().get(0).getName());
        return userDto;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable("id") int userId,
            @RequestBody UserDto userDto) {
        UserEntity existUser = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        existUser.setFirstName(userDto.getFirstName());
        existUser.setLastName(userDto.getLastName());
        existUser.setPassword(passwordEncoder.encode((userDto.getPassword())));

        UserEntity updatedUser = userRepository.save(existUser);
        UserDto existUserDto = mapToDto(updatedUser);
        return ResponseEntity.ok(existUserDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable("id") int userId) {
        UserEntity userEntity = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepository.delete(userEntity);
        return new ResponseEntity<>("User delete", HttpStatus.OK);
    }
}