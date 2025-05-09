package org.example.scheduled;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.example.service.domain.OverdueRentalsViewService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OverdueRentalsViewRefresher {

    @PersistenceContext
    private EntityManager entityManager;
    private final OverdueRentalsViewService overdueRentalsViewService;

    @Scheduled(cron = "0 0 3 * * *")
    public void refreshViewDaily() {
        overdueRentalsViewService.refreshView();
    }
}
