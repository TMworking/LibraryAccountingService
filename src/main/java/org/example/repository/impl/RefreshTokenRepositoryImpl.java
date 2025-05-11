package org.example.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.example.domain.RefreshToken;
import org.example.domain.User;
import org.example.repository.RefreshTokenRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        try {
            RefreshToken refreshToken = entityManager.createQuery(
                            "SELECT rt FROM RefreshToken rt WHERE rt.token = :token", RefreshToken.class)
                    .setParameter("token", token)
                    .getSingleResult();
            return Optional.of(refreshToken);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        entityManager.persist(refreshToken);
        return refreshToken;
    }

    @Override
    public RefreshToken update(RefreshToken refreshToken) {
        return entityManager.merge(refreshToken);
    }

    @Override
    public void deleteAllByUser(User user) {
        entityManager.createQuery(
                        "DELETE FROM RefreshToken rt WHERE rt.user = :user")
                .setParameter("user", user)
                .executeUpdate();
    }

    @Override
    public Long countByUser(User user) {
        return (Long) entityManager.createQuery("SELECT COUNT(rt) FROM RefreshToken rt WHERE rt.user = :user")
                .setParameter("user", user)
                .getSingleResult();
    }
}
