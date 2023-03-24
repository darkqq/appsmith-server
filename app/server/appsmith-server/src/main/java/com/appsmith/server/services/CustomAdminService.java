package com.appsmith.server.services;

import com.appsmith.server.domains.User;
import com.appsmith.server.dtos.UserSignupDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CustomAdminService {
   List<User> getPanelUsers(Integer offset, Integer pageSize, String searchQuery);

   Boolean archiveUser(String id);
   Boolean unarchiveUser(String id);

   Boolean createUser(UserSignupDTO userSignupDTO);

}
