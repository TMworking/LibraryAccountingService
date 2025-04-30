package org.example.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;
import org.example.model.domain.Author;
import org.example.repository.AuthorRepository;
import org.example.repository.Page;
import org.example.web.dto.author.request.AuthorSortRequest;
import org.springframework.stereotype.Repository;

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
    public Page<Author> findAll(AuthorSortRequest filterRequest) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Author> query = cb.createQuery(Author.class);
        Root<Author> root = query.from(Author.class);

        if (filterRequest.getSortBy() != null) {
            Order order = "DESC".equalsIgnoreCase(filterRequest.getSortDirection())
                    ? cb.desc(root.get(filterRequest.getSortBy()))
                    : cb.asc(root.get(filterRequest.getSortBy()));
            query.orderBy(order);
        }

        TypedQuery<Author> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(filterRequest.getPage() * filterRequest.getSize());
        typedQuery.setMaxResults(filterRequest.getSize());
        List<Author> content = typedQuery.getResultList();


        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(Author.class)));
        long totalItems = entityManager.createQuery(countQuery).getSingleResult();

        return new Page<>(
                content,
                filterRequest.getPage(),
                filterRequest.getSize(),
                totalItems
        );
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
