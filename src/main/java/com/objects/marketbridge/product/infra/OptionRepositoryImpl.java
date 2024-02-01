package com.objects.marketbridge.product.infra;

import com.objects.marketbridge.common.domain.Option;
import com.objects.marketbridge.product.service.port.OptionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OptionRepositoryImpl implements OptionRepository {

    private final OptionJpaRepository optionJpaRepository;

    @Override
    public void save(Option option) {
        optionJpaRepository.save(option);
    }

    @Override
    public Optional<Option> findById(Long id) {
        return optionJpaRepository.findById(id);
    }

    @Override
    public Optional<Option> findByName(String name) {
        return optionJpaRepository.findByName(name);
    }
}
