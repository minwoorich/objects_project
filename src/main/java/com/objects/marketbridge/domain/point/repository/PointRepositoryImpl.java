package com.objects.marketbridge.domain.point.repository;

import com.objects.marketbridge.domain.model.Point;
import com.objects.marketbridge.global.error.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository {

    private final PointJpaRepository pointJpaRepository;

    @Override
    public void save(Point point) {
        pointJpaRepository.save(point);
    }

    @Override
    public Point findById(Long id) {
        return pointJpaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("엔티티가 존재하지 않습니다"));
    }

    @Override
    public void deleteAllInBatch() {
        pointJpaRepository.deleteAllInBatch();
    }

    @Override
    public Point findByMemberId(Long memberId) {
        return pointJpaRepository.findByMemberId(memberId).orElseThrow(() -> new EntityNotFoundException("엔티티가 존재하지 않습니다"));
    }
}
