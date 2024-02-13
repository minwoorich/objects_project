package com.objects.marketbridge.product.infra.option;

import com.objects.marketbridge.product.domain.Option;
import com.objects.marketbridge.product.service.port.OptionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OptionRepositoryImpl implements OptionRepository {

    private final OptionJpaRepository optionJpaRepository;

    @Override
    public void save(Option option) {
        optionJpaRepository.save(option);
    }

    @Override
    public Option findById(Long id) {
        return optionJpaRepository.findById(id).orElseThrow(() -> new JpaObjectRetrievalFailureException(new EntityNotFoundException()));
    }

    @Override
    public Option findByName(String name) {
        // 없는 경우 기본값 EMPTY로 반환
        return optionJpaRepository.findByName(name).orElseGet(()-> Option.builder().name("EMPTY").build());
    }
}
