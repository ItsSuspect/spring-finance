package com.web.finance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.web.finance.entities.RefreshToken;
import com.web.finance.entities.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Query("SELECT rt.user FROM refreshtoken rt WHERE rt.token = :token")
    User findUserByToken(@Param("token") String token);
    Optional<RefreshToken> findRefreshTokenByUser(User user);
}
