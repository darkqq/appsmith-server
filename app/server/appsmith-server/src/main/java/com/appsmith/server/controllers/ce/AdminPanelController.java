package com.appsmith.server.controllers.ce;

import com.appsmith.server.configurations.annotation.AuthorityRequired;
import com.appsmith.server.constants.Url;
import com.appsmith.server.domains.User;
import com.appsmith.server.dtos.ResponseDTO;
import com.appsmith.server.dtos.UserSignupDTO;
import com.appsmith.server.exceptions.AppsmithException;
import com.appsmith.server.services.CustomAdminService;
import com.appsmith.server.services.UserService;
import com.appsmith.server.services.ce.UserServiceCE;
import com.appsmith.server.solutions.ce.UserSignupCE;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@RequestMapping(Url.CUSTOM_ADMIN_PATH)
@RequiredArgsConstructor
@RestController
public class AdminPanelController {

    private final CustomAdminService customAdminService;

    private final UserSignupCE userSignupService;

    private final UserService userService;

    @AuthorityRequired(value = "INSTANCE_ADMIN")
    @GetMapping("/users")
    public Mono<ResponseEntity<List<User>>> getPanelUsers(
            @RequestParam(required = false, defaultValue = "0") Integer offset,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "") String searchQuery
    ) {
        return Mono.just(ResponseEntity.ok(customAdminService.getPanelUsers(offset, pageSize, searchQuery)));
    }

    @AuthorityRequired(value = "INSTANCE_ADMIN")
    @PutMapping("/users/update")
    public Mono<ResponseEntity<ResponseDTO<Boolean>>> updateUser(@RequestBody User user) {
        return userService.updateWithoutPermission(user.getId(), user)
                .map(updated -> ResponseEntity.ok(new ResponseDTO<>(HttpStatus.CREATED.value(), true, null)));
    }

    @AuthorityRequired(value = "INSTANCE_ADMIN")
    @PostMapping("/users/create")
    public Mono<ResponseEntity<ResponseDTO<Boolean>>> createUser(@RequestBody User user) {
        return userSignupService.registerUser(user)
                .map(created -> ResponseEntity.ok(new ResponseDTO<>(HttpStatus.CREATED.value(), true, null)));
    }

}
