package edu.asu.diging.simpleusers.core.data;

import org.springframework.data.repository.PagingAndSortingRepository;

import edu.asu.diging.simpleusers.core.model.impl.PasswordResetToken;

public interface TokenRepository extends PagingAndSortingRepository<PasswordResetToken, Long> {

    PasswordResetToken findByToken(String token);
}
