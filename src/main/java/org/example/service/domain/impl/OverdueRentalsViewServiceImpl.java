package org.example.service.domain.impl;

import lombok.RequiredArgsConstructor;
import org.example.repository.OverdueRentalsViewRepository;
import org.example.service.domain.OverdueRentalsViewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OverdueRentalsViewServiceImpl implements OverdueRentalsViewService {

    private final OverdueRentalsViewRepository overdueRentalsViewRepository;

    @Override
    @Transactional
    public void refreshView() {
        overdueRentalsViewRepository.refreshOverdueRentalsView();
    }
}
