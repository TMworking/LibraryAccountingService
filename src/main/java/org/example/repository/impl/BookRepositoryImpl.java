package org.example.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.domain.Book;
import org.example.model.Page;
import org.example.repository.BookRepository;
import org.example.repository.specification.BookSpecification;
import org.example.web.dto.book.request.BookFilterRequest;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryImpl implements BookRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Book.class, id));
    }

    @Override
    public List<Book> findAllByIds(List<Long> ids) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> query = cb.createQuery(Book.class);
        Root<Book> root = query.from(Book.class);

        query.where(root.get("id").in(ids));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Page<Book> findAll(BookFilterRequest filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> query = cb.createQuery(Book.class);
        Root<Book> root = query.from(Book.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.isNull(root.get("deletedAt")));
        BookSpecification.applyFilter(filter, cb, root, predicates);
        BookSpecification.applySort(filter.getSortOptionList(), root, query, cb);

        query.where(cb.and(predicates.toArray(new Predicate[0])));

        List<Book> content = entityManager.createQuery(query)
                .setFirstResult(filter.getPage() * filter.getSize())
                .setMaxResults(filter.getSize())
                .getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Book> countRoot = countQuery.from(Book.class);
        List<Predicate> countPredicates = new ArrayList<>();
        BookSpecification.applyFilter(filter, cb, countRoot, countPredicates);
        countQuery.select(cb.count(countRoot));
        countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));
        long total = entityManager.createQuery(countQuery).getSingleResult();

        return new Page<>(content, filter.getPage(), filter.getSize(), total);
    }

    @Override
    public Book save(Book book) {
        entityManager.persist(book);
        return book;
    }

    @Override
    public Book update(Book book) {
        return entityManager.merge(book);
    }
}
