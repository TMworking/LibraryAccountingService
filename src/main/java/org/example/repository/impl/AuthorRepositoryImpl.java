package org.example.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;
import org.example.domain.Author;
import org.example.repository.AuthorRepository;
import org.example.model.Page;
import org.example.web.dto.author.request.AuthorSortRequest;
import org.example.model.SortOption;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Author> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Author.class, id));
    }

    @Override
    public List<Author> findAllByIds(List<Long> ids) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Author> query = cb.createQuery(Author.class);
        Root<Author> root = query.from(Author.class);

        query.where(root.get("id").in(ids));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Page<Author> findAll(AuthorSortRequest filterRequest) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Author> query = cb.createQuery(Author.class);
        Root<Author> root = query.from(Author.class);

        List<Order> orders = new ArrayList<>();
        for (SortOption sortOption : filterRequest.getSortOptionList()) {
            Order order = "DESC".equalsIgnoreCase(sortOption.getDirection())
                    ? cb.desc(root.get(sortOption.getField()))
                    : cb.asc(root.get(sortOption.getField()));
            orders.add(order);
        }
        if (!orders.isEmpty()) {
            query.orderBy(orders);
        }

        TypedQuery<Author> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(filterRequest.getPage() * filterRequest.getSize());
        typedQuery.setMaxResults(filterRequest.getSize());
        List<Author> content = typedQuery.getResultList();


        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(Author.class)));
        long totalItems = entityManager.createQuery(countQuery).getSingleResult();

        return new Page<>(content, filterRequest.getPage(), filterRequest.getSize(), totalItems);
    }

    @Override
    public Author save(Author author) {
        entityManager.persist(author);
        return author;
    }

    @Override
    public Author update(Author author) {
        return entityManager.merge(author);
    }
}
