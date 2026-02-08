package com.uni.vendas.user.controller;

import com.uni.vendas.user.dto.DefaultUserDTO;
import com.uni.vendas.user.dto.RegisterUserDTO;
import com.uni.vendas.user.models.User;
import com.uni.vendas.user.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@SecurityRequirement(name = "bearer-key")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<Object> findUserById(@PathVariable("id") String id) {
        var user = userService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok().body(user);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<Object> createUser(@ModelAttribute @Valid RegisterUserDTO userDTO) {
        User user = userService.createUser(userDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();
        return ResponseEntity.created(location).body("User created successfully with ID: " + user.getId());
    }

    @PutMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<Object> updateUser(@PathVariable("id") String id, @ModelAttribute @Valid RegisterUserDTO userDTO) {
        Optional<DefaultUserDTO> userOptional = userService.updateUser(id, userDTO);
        if (userOptional.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") String id) {
        Optional<DefaultUserDTO> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(
            value = "/search",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<Page<DefaultUserDTO>> sarchUsers(
            @RequestParam(value = "name", required = false)
            String name,
            @RequestParam(value = "email", required = false)
            String email,
            @RequestParam(value = "phone_number", required = false)
            String phoneNumber,
            @RequestParam(value = "page", defaultValue = "0")
            Integer page,
            @RequestParam(value = "size", defaultValue = "10")
            Integer size
    ) {
        Page<DefaultUserDTO> pageResult = userService.search(name, email, phoneNumber, page, size);

        return ResponseEntity.ok(pageResult);
    }

}
