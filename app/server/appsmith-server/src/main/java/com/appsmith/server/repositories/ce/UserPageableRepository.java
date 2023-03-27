package com.appsmith.server.repositories.ce;

import com.appsmith.server.domains.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPageableRepository extends MongoRepository<User, String> {

    @Query(value = "{$or: [" +
            "{'id': {$regex: ?0,$options: 'i'}}, " +
            "{'email': {$regex: ?0, $options: 'i'}}, " +
            "{'name': {$regex: ?0,$options: 'i'}}" +
            "]}")
    Page<User> findUserBySearchCriteria(String searchQuery, Pageable pageable);
}
