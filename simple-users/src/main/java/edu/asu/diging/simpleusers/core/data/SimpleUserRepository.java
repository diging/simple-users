package edu.asu.diging.simpleusers.core.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import edu.asu.diging.simpleusers.core.model.impl.SimpleUser;

public interface SimpleUserRepository extends PagingAndSortingRepository<SimpleUser, String>, CrudRepository<SimpleUser, String> {

    public SimpleUser findByEmail(String email);
}