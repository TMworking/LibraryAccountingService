package org.example.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.repository.OverdueRentalsViewRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OverdueRentalsViewRepositoryImpl implements OverdueRentalsViewRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void refreshOverdueRentalsView() {
        entityManager.createNativeQuery("REFRESH MATERIALIZED VIEW overdue_rentals").executeUpdate();
    }
}
