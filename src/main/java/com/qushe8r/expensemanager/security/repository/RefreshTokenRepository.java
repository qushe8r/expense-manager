package com.qushe8r.expensemanager.security.repository;

import com.qushe8r.expensemanager.security.jwt.RefreshToken;
import jakarta.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

  List<RefreshToken> findByEmail(String email);

  Optional<RefreshToken> findById(@Nonnull String id);
}
