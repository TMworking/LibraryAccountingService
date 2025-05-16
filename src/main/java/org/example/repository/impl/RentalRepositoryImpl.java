package org.example.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.domain.Rental;
import org.example.domain.Role;
import org.example.domain.User;
import org.example.model.Page;
import org.example.repository.RentalRepository;
import org.example.repository.specification.RentalSpecification;
import org.example.web.dto.rental.request.ClosedRentalFilterRequest;
import org.example.web.dto.rental.request.RentalFilterRequest;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RentalRepositoryImpl implements RentalRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Rental> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Rental.class, id));
    }

    @Override
    public Rental update(Rental rental) {
        return entityManager.merge(rental);
    }

    @Override
    public Rental save(Rental rental) {
        entityManager.persist(rental);
        return rental;
    }

    @Override
    public Page<Rental> findAllClosedWithFilter(ClosedRentalFilterRequest filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Rental> query = cb.createQuery(Rental.class);
        Root<Rental> root = query.from(Rental.class);

        Fetch<Rental, User> rentalUserFetch = root.fetch("user", JoinType.LEFT);
        Fetch<User, Role> userRoleFetch = rentalUserFetch.fetch("roles", JoinType.LEFT);
        root.fetch("book", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        RentalSpecification.applyFilter(filter, cb, root, predicates);
        RentalSpecification.applySort(filter.getSortOptions(), root, query, cb);

        query.where(cb.and(predicates.toArray(new Predicate[0])));

        List<Rental> content = entityManager.createQuery(query)
                .setFirstResult(filter.getPage() * filter.getSize())
                .setMaxResults(filter.getSize())
                .getResultList();


        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Rental> countRoot = countQuery.from(Rental.class);
        List<Predicate> countPredicates = new ArrayList<>();
        RentalSpecification.applyFilter(filter, cb, countRoot, countPredicates);
        countQuery.select(cb.count(countRoot)).where(cb.and(countPredicates.toArray(new Predicate[0])));
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new Page<>(content, filter.getPage(), filter.getSize(), total);
    }

    @Override
    public Page<Rental> findByUserIdWithFilter(Long userId, RentalFilterRequest filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Rental> query = cb.createQuery(Rental.class);
        Root<Rental> root = query.from(Rental.class);

        Fetch<Rental, User> rentalUserFetch = root.fetch("user", JoinType.LEFT);
        Fetch<User, Role> userRoleFetch = rentalUserFetch.fetch("roles", JoinType.LEFT);
        root.fetch("book", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("user").get("id"), userId));
        RentalSpecification.applyFilter(filter, cb, root, predicates);
        RentalSpecification.applySort(filter.getSortOptions(), root, query, cb);

        query.where(cb.and(predicates.toArray(new Predicate[0])));

        List<Rental> content = entityManager.createQuery(query)
                .setFirstResult(filter.getPage() * filter.getSize())
                .setMaxResults(filter.getSize())
                .getResultList();


        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Rental> countRoot = countQuery.from(Rental.class);
        List<Predicate> countPredicates = new ArrayList<>();
        countPredicates.add(cb.equal(countRoot.get("user").get("id"), userId));
        RentalSpecification.applyFilter(filter, cb, countRoot, countPredicates);
        countQuery.select(cb.count(countRoot)).where(cb.and(countPredicates.toArray(new Predicate[0])));
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new Page<>(content, filter.getPage(), filter.getSize(), total);
    }

    @Override
    public Page<Rental> findByBookIdWithFilter(Long bookId, RentalFilterRequest filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Rental> query = cb.createQuery(Rental.class);
        Root<Rental> root = query.from(Rental.class);

        Fetch<Rental, User> rentalUserFetch = root.fetch("user", JoinType.LEFT);
        Fetch<User, Role> userRoleFetch = rentalUserFetch.fetch("roles", JoinType.LEFT);
        root.fetch("book", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("book").get("id"), bookId));
        RentalSpecification.applyFilter(filter, cb, root, predicates);
        RentalSpecification.applySort(filter.getSortOptions(), root, query, cb);

        query.where(cb.and(predicates.toArray(new Predicate[0])));

        List<Rental> content = entityManager.createQuery(query)
                .setFirstResult(filter.getPage() * filter.getSize())
                .setMaxResults(filter.getSize())
                .getResultList();


        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Rental> countRoot = countQuery.from(Rental.class);
        List<Predicate> countPredicates = new ArrayList<>();
        countPredicates.add(cb.equal(countRoot.get("book").get("id"), bookId));
        RentalSpecification.applyFilter(filter, cb, countRoot, countPredicates);
        countQuery.select(cb.count(countRoot)).where(cb.and(countPredicates.toArray(new Predicate[0])));
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new Page<>(content, filter.getPage(), filter.getSize(), total);
    }

    @Override
    public long countActiveRentalsByBookId(Long bookId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Rental> root = query.from(Rental.class);

        query.select(cb.count(root));

        Predicate byBook = cb.equal(root.get("book").get("id"), bookId);
        Predicate notReturned = cb.isNull(root.get("returnDate"));
        query.where(cb.and(byBook, notReturned));

        return entityManager.createQuery(query).getSingleResult();
    }
}
