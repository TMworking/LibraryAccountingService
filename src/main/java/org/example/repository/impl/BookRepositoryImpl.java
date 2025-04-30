package org.example.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.model.domain.Book;
import org.example.repository.BookRepository;
import org.springframework.stereotype.Repository;

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
    public List<Book> findAll(int page, int size) {
        return entityManager.createQuery("SELECT a FROM Author a", Book.class)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
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

    @Override
    public void delete(Book book) {
        entityManager.remove(book);
    }

    @Override
    public Long countAll() {
        return (Long) entityManager.createQuery("SELECT COUNT(b) FROM Book b")
                .getSingleResult();
    }
}
