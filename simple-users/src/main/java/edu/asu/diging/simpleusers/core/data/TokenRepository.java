package edu.asu.diging.simpleusers.core.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import edu.asu.diging.simpleusers.core.model.impl.PasswordResetToken;

public interface TokenRepository extends PagingAndSortingRepository<PasswordResetToken, Long>, CrudRepository<PasswordResetToken, Long> {

    PasswordResetToken findByToken(String token);
}
