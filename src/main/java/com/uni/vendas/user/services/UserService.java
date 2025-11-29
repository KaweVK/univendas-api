package com.uni.vendas.user.services;

import com.uni.vendas.infra.services.commom.UpImageService;
import com.uni.vendas.user.dto.DefaultUserDTO;
import com.uni.vendas.user.dto.RegisterUserDTO;
import com.uni.vendas.user.mapper.UserMapper;
import com.uni.vendas.user.models.User;
import com.uni.vendas.user.repository.UserRepository;
import com.uni.vendas.user.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.uni.vendas.user.repository.specs.UserSpecs.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;
    private final UpImageService upImageService;

    public Optional<DefaultUserDTO> findById(String id) {
        UUID uuid = UUID.fromString(id);
        var userOptional = userRepository.findById(uuid);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + id + " does not exist");
        }
        return userOptional.map(userMapper::toDefaultDTO);
    }

    protected Optional<User> findByIdInternal(String id) {
        Optional<User> userOptional = userRepository.findById(UUID.fromString(id));
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + id + " does not exist");
        }
        return userOptional;
    }

    public User createUser(RegisterUserDTO userDTO) {
        var user = userMapper.toEntity(userDTO);

        if (userDTO.image() != null && !userDTO.image().isEmpty()) {
            String url = upImageService.fazerUpload(userDTO.image());

            user.setImage(url);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userValidator.validate(user);
        return userRepository.save(user);

    }

    public Optional<DefaultUserDTO> updateUser(String id, RegisterUserDTO userDTO ) {
        Optional<User> userOptional = findByIdInternal(id);

        if (userOptional.isEmpty()) {
            return Optional.empty();
        }

        User user = userOptional.get();

        if (user.getId() == null) {
            throw new IllegalArgumentException("Item ID cannot be null for update operation");
        }

        user.setName(userDTO.name());
        user.setEmail(userDTO.email());
        user.setPhoneNumber(userDTO.phoneNumber());
        user.setCity(userDTO.city());
        user.setPassword(passwordEncoder.encode(userDTO.password()));
        if (userDTO.image() != null && !userDTO.image().isEmpty()) {
            String url = upImageService.fazerUpload(userDTO.image());

            user.setImage(url);
        }
        userValidator.validate(user);

        var updatedUser = userRepository.save(user);
        return Optional.of(userMapper.toDefaultDTO(updatedUser));
    }

    public void deleteUser(String id) {
        UUID uuid = UUID.fromString(id);
        Optional<User> userOptional = userRepository.findById(uuid);
        userOptional.ifPresent(userRepository::delete);
    }

    public Page<DefaultUserDTO> search(String name, String email, String phoneNumber, Integer page, Integer size) {
        Specification<User> spec = Specification
                .where((root, query, cb) -> cb.conjunction());

        if (name != null && !name.isEmpty()) {
            spec = spec.and(nameLike(name));
        }
        if (email != null && !email.isEmpty()) {
            spec = spec.and(emailLike(email));
        }
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            spec = spec.and(phoneNumberLike(phoneNumber));
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<User> result = userRepository.findAll(spec, pageable);

        return result.map(userMapper::toDefaultDTO);
    }

}
