package org.example.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.domain.OverdueRentalView;
import org.example.model.Page;
import org.example.repository.OverdueRentalsViewRepository;
import org.example.repository.specification.OverdueRentalSpecification;
import org.example.web.dto.rental.request.OverdueRentalFilterRequest;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OverdueRentalsViewRepositoryImpl implements OverdueRentalsViewRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void refreshOverdueRentalsView() {
        entityManager.createNativeQuery("REFRESH MATERIALIZED VIEW overdue_rentals").executeUpdate();
    }

    @Override
    public Page<OverdueRentalView> findAllWithFilter(OverdueRentalFilterRequest filterRequest) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<OverdueRentalView> query = cb.createQuery(OverdueRentalView.class);
        Root<OverdueRentalView> root = query.from(OverdueRentalView.class);

        List<Predicate> predicates = new ArrayList<>();
        OverdueRentalSpecification.applyFilter(filterRequest, cb, root, predicates);
        OverdueRentalSpecification.applySort(filterRequest.getSortOptions(), root, query, cb);

        query.where(cb.and(predicates.toArray(new Predicate[0])));

        List<OverdueRentalView> content = entityManager.createQuery(query)
                .setFirstResult(filterRequest.getPage() * filterRequest.getSize())
                .setMaxResults(filterRequest.getSize())
                .getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<OverdueRentalView> countRoot = countQuery.from(OverdueRentalView.class);
        List<Predicate> countPredicates = new ArrayList<>();
        OverdueRentalSpecification.applyFilter(filterRequest, cb, countRoot, countPredicates);
        countQuery.select(cb.count(countRoot)).where(cb.and(countPredicates.toArray(new Predicate[0])));
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new Page<>(content, filterRequest.getPage(), filterRequest.getSize(), total);
    }
}
