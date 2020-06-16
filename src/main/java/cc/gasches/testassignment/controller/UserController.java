package cc.gasches.testassignment.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cc.gasches.testassignment.dto.CreateUserDto;
import cc.gasches.testassignment.dto.UpdateUserDto;
import cc.gasches.testassignment.dto.UserDto;
import cc.gasches.testassignment.service.UserService;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        return ResponseEntity.ok(userService.create(createUserDto));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<UserDto> updateSection(@PathVariable("id") long id,
            @Valid @RequestBody UpdateUserDto request) {
        return userService.update(id, request).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
