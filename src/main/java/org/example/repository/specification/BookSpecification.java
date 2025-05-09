package org.example.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.domain.Author;
import org.example.domain.Book;
import org.example.domain.Catalog;
import org.example.model.SortOption;
import org.example.web.dto.book.request.BookFilterRequest;

import java.util.ArrayList;
import java.util.List;

public class BookSpecification {

    public static void applyFilter(BookFilterRequest filter, CriteriaBuilder cb, Root<Book> root, List<Predicate> predicates) {
        byName(filter.getName(), predicates, cb, root);
        byAuthorName(filter.getAuthorName(), predicates, cb, root);
        byCatalogIds(filter.getCatalogIds(), predicates, cb, root);
        byAvailability(filter.getIsAvailable(), predicates, cb, root);
    }

    private static void byName(String name, List<Predicate> predicates, CriteriaBuilder cb, Root<Book> root) {
        if (name != null && !name.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
    }

    private static void byAuthorName(String authorName, List<Predicate> predicates, CriteriaBuilder cb, Root<Book> root) {
        if (authorName != null && !authorName.isBlank()) {
            Join<Book, Author> authorJoin = root.join("authors", JoinType.LEFT);
            predicates.add(cb.like(cb.lower(authorJoin.get("name")), "%" + authorName.toLowerCase() + "%"));
        }
    }

    private static void byCatalogIds(List<Long> catalogIds, List<Predicate> predicates, CriteriaBuilder cb, Root<Book> root) {
        if (catalogIds != null && !catalogIds.isEmpty()) {
            Join<Book, Catalog> catalogJoin = root.join("catalogs", JoinType.LEFT);
            predicates.add(catalogJoin.get("id").in(catalogIds));
        }
    }

    private static void byAvailability(Boolean availability, List<Predicate> predicates, CriteriaBuilder cb, Root<Book> root) {
        if (availability != null) {
            if (availability) {
                predicates.add(cb.greaterThan(root.get("availableCount"), 0));
            } else {
                predicates.add(cb.equal(root.get("availableCount"), 0));
            }
        }
    }

    public static void applySort(List<SortOption> sortOptions, Root<Book> root, CriteriaQuery<Book> query, CriteriaBuilder cb) {
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
    }
}
