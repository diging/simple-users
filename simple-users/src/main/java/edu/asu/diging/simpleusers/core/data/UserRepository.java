package edu.asu.diging.simpleusers.core.data;

import org.springframework.data.repository.PagingAndSortingRepository;

import edu.asu.diging.simpleusers.core.model.impl.User;

public interface UserRepository extends PagingAndSortingRepository<User, String> {

}