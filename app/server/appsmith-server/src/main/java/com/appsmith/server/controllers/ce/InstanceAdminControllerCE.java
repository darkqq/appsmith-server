package com.appsmith.server.controllers.ce;

import com.appsmith.server.constants.Url;
import com.appsmith.server.domains.User;
import com.appsmith.server.dtos.EnvChangesResponseDTO;
import com.appsmith.server.dtos.ResponseDTO;
import com.appsmith.server.dtos.TestEmailConfigRequestDTO;
import com.appsmith.server.repositories.UserRepository;
import com.appsmith.server.services.UserService;
import com.appsmith.server.solutions.EnvManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RequestMapping(Url.INSTANCE_ADMIN_URL)
@RequiredArgsConstructor
@Slf4j
public class InstanceAdminControllerCE {

    private final EnvManager envManager;


    @GetMapping("/env")
    public Mono<ResponseDTO<Map<String, String>>> getAll() {
        log.debug("Getting all env configuration");
        return envManager.getAllNonEmpty()
                .map(data -> new ResponseDTO<>(HttpStatus.OK.value(), data, null));
    }

    @GetMapping("/env/download")
    public Mono<Void> download(ServerWebExchange exchange) {
        log.debug("Getting all env configuration");
        return envManager.download(exchange);
    }

    @Deprecated
    @PutMapping(value = "/env", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<ResponseDTO<EnvChangesResponseDTO>> saveEnvChangesJSON(
            @Valid @RequestBody Map<String, String> changes
    ) {
        log.debug("Applying env updates {}", changes.keySet());
        return envManager.applyChanges(changes)
                .map(res -> new ResponseDTO<>(HttpStatus.OK.value(), res, null));
    }

    @PutMapping(value = "/env", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Mono<ResponseDTO<EnvChangesResponseDTO>> saveEnvChangesMultipartFormData(
            ServerWebExchange exchange
    ) {
        log.debug("Applying env updates from form data");
        return exchange.getMultipartData()
                .flatMap(envManager::applyChangesFromMultipartFormData)
                .map(res -> new ResponseDTO<>(HttpStatus.OK.value(), res, null));
    }

    @PostMapping("/restart")
    public Mono<ResponseDTO<Boolean>> restart() {
        log.debug("Received restart request");
        return envManager.restart()
                .thenReturn(new ResponseDTO<>(HttpStatus.OK.value(), true, null));
    }

    @PostMapping("/send-test-email")
    public Mono<ResponseDTO<Boolean>> sendTestEmail(@RequestBody @Valid TestEmailConfigRequestDTO requestDTO) {
        log.debug("Sending test email");
        return envManager.sendTestEmail(requestDTO)
                .thenReturn(new ResponseDTO<>(HttpStatus.OK.value(), true, null));
    }


}
