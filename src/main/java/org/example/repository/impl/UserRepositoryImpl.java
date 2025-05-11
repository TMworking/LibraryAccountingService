package org.example.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.example.domain.User;
import org.example.model.Page;
import org.example.model.SortOption;
import org.example.repository.UserRepository;
import org.example.web.dto.user.request.UserSortRequest;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            return Optional.of(entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Page<User> findAll(UserSortRequest request) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);

        root.fetch("roles", JoinType.LEFT);

        List<SortOption> sortOptions = request.getSortOptionList();
        if (sortOptions != null && !sortOptions.isEmpty()) {
            List<Order> orders = new ArrayList<>();
            for (SortOption sortOption : sortOptions) {
                Path<Object> sortField = root.get(sortOption.getField());
                Order order = "asc".equalsIgnoreCase(sortOption.getDirection())
                        ? cb.asc(sortField)
                        : cb.desc(sortField);
                orders.add(order);
            }
            query.orderBy(orders);
        }

        List<User> content = entityManager.createQuery(query)
                .setFirstResult(request.getPage() * request.getSize())
                .setMaxResults(request.getSize())
                .getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(User.class)));
        long totalItems = entityManager.createQuery(countQuery).getSingleResult();

        return new Page<>(content, request.getPage(), request.getSize(), totalItems);
    }

    @Override
    public User save(User user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    public User update(User user) {
        return entityManager.merge(user);
    }
}
