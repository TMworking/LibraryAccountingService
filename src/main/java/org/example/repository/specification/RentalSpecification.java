package org.example.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.domain.Rental;
import org.example.model.SortOption;
import org.example.web.dto.rental.request.RentalFilterRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalSpecification {

    public static void applyFilter(RentalFilterRequest filter, CriteriaBuilder cb, Root<Rental> root, List<Predicate> predicates) {
        byIsClosed(filter.getIsClosed(), cb, root, predicates);
        byRentDateFrom(filter.getRentDateFrom(), cb, root, predicates);
        byRentDateTo(filter.getRentDateTo(), cb, root, predicates);
    }

    private static void byIsClosed(Boolean isClosed, CriteriaBuilder cb, Root<Rental> root, List<Predicate> predicates) {
        if (isClosed != null) {
            Predicate predicate = isClosed
                    ? cb.isNotNull(root.get("returnDate"))
                    : cb.isNull(root.get("returnDate"));
            predicates.add(predicate);
        }
    }

    private static void byRentDateFrom(LocalDate from, CriteriaBuilder cb, Root<Rental> root, List<Predicate> predicates) {
        if (from != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("rentDate"), from));
        }
    }

    private static void byRentDateTo(LocalDate to, CriteriaBuilder cb, Root<Rental> root, List<Predicate> predicates) {
        if (to != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("rentDate"), to));
        }
    }

    public static void applySort(List<SortOption> sortOptions, Root<Rental> root, CriteriaQuery<Rental> query, CriteriaBuilder cb) {
        if (sortOptions != null && !sortOptions.isEmpty()) {
            List<Order> orders = new ArrayList<>();
            for (SortOption option : sortOptions) {
                Path<Object> path = root.get(option.getField());
                orders.add("asc".equalsIgnoreCase(option.getDirection())
                        ? cb.asc(path)
                        : cb.desc(path));
            }
            query.orderBy(orders);
        }
    }
}
