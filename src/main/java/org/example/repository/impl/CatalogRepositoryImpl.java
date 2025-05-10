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
import java.util.Optional;

@Repository
public class CatalogRepositoryImpl implements CatalogRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Catalog> findById(Long id) {
        return Optional.of(entityManager.find(Catalog.class, id));
    }

    @Override
    public List<Catalog> findAllByIds(List<Long> ids) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Catalog> query = cb.createQuery(Catalog.class);
        Root<Catalog> root = query.from(Catalog.class);

        query.where(root.get("id").in(ids));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Catalog> findAllSubCatalogsRecursiveByIds(List<Long> rootIds) {
        String sql = """
        WITH RECURSIVE catalog_tree AS (
            SELECT * FROM catalogs WHERE id IN (:rootIds)
            UNION ALL
            SELECT c.id, c.name, c.parent_id
            FROM catalogs c
            INNER JOIN catalog_tree ct ON c.parent_id = ct.id
        )
        SELECT id, name, parent_id FROM catalog_tree
        """;

        return entityManager.createNativeQuery(sql, Catalog.class)
                .setParameter("rootIds", rootIds)
                .getResultList();
    }

    @Override
    public List<Catalog> findAllRootCatalogs() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Catalog> cq = cb.createQuery(Catalog.class);
        Root<Catalog> root = cq.from(Catalog.class);

        cq.select(root).where(cb.isNull(root.get("parent")));
        cq.distinct(true);

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public Catalog save(Catalog catalog) {
        entityManager.persist(catalog);
        return catalog;
    }

    @Override
    public Catalog update(Catalog catalog) {
        return entityManager.merge(catalog);
    }
}
