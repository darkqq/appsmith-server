package com.appsmith.server.configurations.annotation;

import com.appsmith.server.domains.User;
import com.appsmith.server.exceptions.AppsmithError;
import com.appsmith.server.exceptions.AppsmithException;
import com.appsmith.server.repositories.UserRepository;
import com.appsmith.server.services.SessionUserService;
import com.appsmith.server.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class EndpointProtectionAspect {

    private final UserRepository userRepository;

    @PostConstruct
    public void init() {
        System.out.println("*** ASPECT INIT ***");
    }


    @Around("@annotation(AuthorityRequired)")
    public Mono<Object> secureEndPoint(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        final Method method = signature.getMethod();
        final AuthorityRequired annotation = method.getAnnotation(AuthorityRequired.class);

        Mono<Tuple2<Object, Object>> zipMono = Mono.zip(
                ReactiveSecurityContextHolder.getContext()
                        .map(context ->(User) context.getAuthentication().getPrincipal())
                        .flatMap(user -> userRepository.findByEmail(user.getEmail()))
                        .flatMap(userFromDb -> {
                            if(userFromDb.hasClientRole(annotation.value())){
                                try {
                                    return Mono.just(proceedingJoinPoint.proceed());
                                } catch (Throwable e) {
                                    return Mono.just(new AppsmithException(AppsmithError.UNAUTHORIZED_ACCESS));
                                }
                            }
                            return Mono.just(new AppsmithException(AppsmithError.UNAUTHORIZED_ACCESS));
                        }),
                    (Mono<Object>) proceedingJoinPoint.proceed()
        );


        return  zipMono
                .map(objects -> {
                    if(objects.getT1() instanceof AppsmithException){
                        AppsmithException ex = (AppsmithException) objects.getT1();
                        return ResponseEntity.status(ex.getHttpStatus());
                    }
                    return objects.getT2();
                });
    }
}
