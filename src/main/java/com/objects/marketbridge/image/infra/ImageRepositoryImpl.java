package com.objects.marketbridge.image.infra;

import com.objects.marketbridge.image.domain.Image;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ImageRepositoryImpl implements ImageRepository {

    private final ImageJpaRepository imageJpaRepository;

    @Override
    public void save(Image image) {
        imageJpaRepository.save(image);
    }

    @Override
    public Image findById(Long id) {
        return imageJpaRepository.findById(id).orElseThrow(() -> new JpaObjectRetrievalFailureException(new EntityNotFoundException()));
    }

    public void delete(Image image) {
        imageJpaRepository.delete(image);
    }

    public void deleteById(Long id) {
        imageJpaRepository.delete(findById(id));
    }

    @Override
    public void deleteAll(Iterable<? extends Image> images) {
        imageJpaRepository.deleteAll(images);
    }

    @Override
    public List<Image> findByIds(List<Long> ids) {
        return imageJpaRepository.findByIds(ids);
    }

    @Override
    public void deleteByIdIn(List<Long> ids) {
        imageJpaRepository.deleteByIdIn(ids);
    }
}
