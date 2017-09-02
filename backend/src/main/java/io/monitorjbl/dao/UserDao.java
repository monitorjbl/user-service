package io.monitorjbl.dao;

import io.monitorjbl.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserDao extends PagingAndSortingRepository<User, Long> {

  User getUserByUsername(String username);
}
