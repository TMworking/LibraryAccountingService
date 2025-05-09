package org.example.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.domain.Catalog;
import org.example.repository.CatalogRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CatalogRepositoryImpl implements CatalogRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Catalog> findAllByIds(List<Long> ids) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Catalog> query = cb.createQuery(Catalog.class);
        Root<Catalog> root = query.from(Catalog.class);

        query.where(root.get("id").in(ids));

        return entityManager.createQuery(query).getResultList();
    }
}
