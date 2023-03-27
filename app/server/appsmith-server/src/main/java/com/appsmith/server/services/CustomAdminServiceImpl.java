package com.appsmith.server.services;

import com.appsmith.server.domains.User;
import com.appsmith.server.dtos.ResponseDTO;
import com.appsmith.server.dtos.UserSignupDTO;
import com.appsmith.server.repositories.UserRepository;
import com.appsmith.server.repositories.ce.UserPageableRepository;
import com.appsmith.server.services.ce.UserServiceCE;
import com.appsmith.server.solutions.ce.UserSignupCE;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomAdminServiceImpl implements CustomAdminService{



    private final UserPageableRepository userRepository;

    private final UserSignupCE userSignupCE;


    @Override
    public List<User> getPanelUsers(Integer offset, Integer pageSize, String searchQuery) {
        Page<User> userBySearchCriteria = userRepository
                .findUserBySearchCriteria(searchQuery, PageRequest.of(offset, pageSize));

        return userBySearchCriteria.stream().toList();
    }

    private boolean isSearchQueryPresent(List<String> sources, String searchQuery){
        if(Objects.isNull(searchQuery) || searchQuery.isBlank()){
            return false;
        } else {
            return sources.stream()
                    .anyMatch(s -> Objects.nonNull(s) && !s.isEmpty() && s.contains(searchQuery));
        }
    }



    @Override
    public Boolean archiveUser(String id) {
        return null;
    }

    @Override
    public Boolean unarchiveUser(String id) {
        return null;
    }



    @Override
    public Boolean updateUser(User user, ServerWebExchange exchange) {

        return null;
    }
}
