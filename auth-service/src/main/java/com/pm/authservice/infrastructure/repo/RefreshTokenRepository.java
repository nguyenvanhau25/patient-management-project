package com.pm.authservice.infrastructure.repo;

import com.pm.authservice.domain.RefreshToken;
import com.pm.authservice.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByToken(String token);

    // để có thể xóa nhiều bản ghi
    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshToken rt WHERE rt.userEmail = :email")
    int deleteByUserEmail(@Param("email") String email);
}
