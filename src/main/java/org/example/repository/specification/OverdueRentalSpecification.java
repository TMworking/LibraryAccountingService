package org.example.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.domain.OverdueRentalView;
import org.example.model.SortOption;
import org.example.web.dto.rental.request.OverdueRentalFilterRequest;

import java.util.ArrayList;
import java.util.List;

public class OverdueRentalSpecification {

    public static void applyFilter(OverdueRentalFilterRequest request, CriteriaBuilder cb, Root<OverdueRentalView> root, List<Predicate> predicates) {
        byUser(request.getUserId(), cb, root, predicates);
        byBook(request.getBookId(), cb, root, predicates);
    }

    private static void byUser(Long userId, CriteriaBuilder cb, Root<OverdueRentalView> root, List<Predicate> predicates) {
        if (userId != null) {
            predicates.add(cb.equal(root.get("userId"), userId));
        }
    }

    private static void byBook(Long bookId, CriteriaBuilder cb, Root<OverdueRentalView> root, List<Predicate> predicates) {
        if (bookId != null) {
            predicates.add(cb.equal(root.get("bookId"), bookId));
        }
    }

    public static void applySort(List<SortOption> sortOptions, Root<OverdueRentalView> root, CriteriaQuery<OverdueRentalView> query, CriteriaBuilder cb) {
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
