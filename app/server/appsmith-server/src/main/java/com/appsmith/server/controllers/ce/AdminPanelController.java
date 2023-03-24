package com.appsmith.server.controllers.ce;

import com.appsmith.server.configurations.annotation.AuthorityRequired;
import com.appsmith.server.constants.Url;
import com.appsmith.server.domains.User;
import com.appsmith.server.dtos.ResponseDTO;
import com.appsmith.server.services.CustomAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@RequestMapping(Url.CUSTOM_ADMIN_PATH)
@RequiredArgsConstructor
@RestController
public class AdminPanelController {

    private final CustomAdminService customAdminService;


    @PostConstruct
    public void init(){
        log.info("************** CUSTOM CONTROLLER INITIALIZED *******");
    }

    @AuthorityRequired(value = "INSTANCE_ADMIN")
    @GetMapping("/users")
    public Mono<ResponseEntity<List<User>>> getPanelUsers(
            @RequestParam(required = false, defaultValue = "0") Integer offset,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "") String searchQuery
    ){
        return Mono.just(ResponseEntity.ok(customAdminService.getPanelUsers(offset, pageSize, searchQuery)));
    }

    @PutMapping("/disable/{userId}")
    public Mono<ResponseDTO<Boolean>> disableUser(@PathVariable String userId){
        return null;
    }
}
