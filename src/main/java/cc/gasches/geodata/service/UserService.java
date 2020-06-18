package cc.gasches.geodata.service;

import java.util.Collections;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cc.gasches.geodata.dto.CreateUserDto;
import cc.gasches.geodata.dto.UpdateUserDto;
import cc.gasches.geodata.dto.UserDto;
import cc.gasches.geodata.mapper.UserMapper;
import cc.gasches.geodata.model.User;
import cc.gasches.geodata.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public Optional<UserDetails> findUserDetailsByName(@Nonnull String username) {
        return userRepository.findByName(username).map(UserService::createDetails);
    }

    @Transactional
    public void createIfNotExists(@Nonnull String name, @Nonnull String password) {
        if (!userRepository.existsByName(name)) {
            userRepository.save(new User(name, passwordEncoder.encode(password)));
        }
    }

    @Transactional
    public UserDto create(@Nonnull CreateUserDto createUserDto) {
        User user = userOf(createUserDto);
        return userMapper.userToDto(userRepository.save(user));
    }

    @Transactional
    public Optional<UserDto> update(long id, @Nonnull UpdateUserDto updateUserDto) {
        return userRepository.findById(id).map(u -> {
            u.setPassword(updateUserDto.getPassword());
            return userRepository.save(u);
        }).map(userMapper::userToDto);
    }

    private User userOf(CreateUserDto createUserDto) {
        return new User(createUserDto.getName(), passwordEncoder.encode(createUserDto.getPassword()));
    }

    private static UserDetails createDetails(User u) {
        return new org.springframework.security.core.userdetails.User(u.getName(), u.getPassword(),
                Collections.emptySet());
    }
}
