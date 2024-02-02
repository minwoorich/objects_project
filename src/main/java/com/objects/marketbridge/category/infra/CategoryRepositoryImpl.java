package com.objects.marketbridge.category.infra;

import com.objects.marketbridge.category.service.port.CategoryRepository;
import com.objects.marketbridge.common.domain.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;
    private final EntityManager em;

    @Override
    public Category findById(Long id) {
        return categoryJpaRepository.findById(id)
                .orElseThrow(() -> new JpaObjectRetrievalFailureException(new EntityNotFoundException()));
    }

    @Override
    public Boolean existsByName(String name) {
        return categoryJpaRepository.existsByName(name);
    }

    @Override
    public void save(Category category) {
        categoryJpaRepository.save(category);
    }

    @Override
    public Category findByName(String name) {
        return categoryJpaRepository.findByName(name);

//    public List<ProductImage> findAllByProductId(Long productId){
//        return em.createQuery("select pi from ProductImage pi where pi.product = :product", ProductImage.class)
//                .setParameter("product", productRepository.findById(productId))
//                .getResultList();
    }

    @Override
    public Category findByNameAndLevel(String name, Long level) {
//        Category category = em.createQuery
//                        ("select c from Category c where c.name = :name and c.level = :level", Category.class)
//                .setParameter("name", name)
//                .setParameter("level", level)
//                .getSingleResult();
        return categoryJpaRepository.findByNameAndLevel(name, level);
    }

    @Override
    public Boolean existsByNameAndLevel(String name, Long level) {
        return categoryJpaRepository.existsByNameAndLevel(name, level);
    }

    @Override
    public List<Category> findAllByNameAndLevel(String name, Long level) {
        List<Category> categories = categoryJpaRepository.findAllByNameAndLevel(name, level);
        return categories;
    }

    @Override
    public List<Category> findAllByLevelAndParentIdIsNull(Long level) {
        return categoryJpaRepository.findAllByLevelAndParentIdIsNull(level);
    }

    @Override
    public List<Category> findAllByLevelAndParentId(Long level, Long parentId) {
        return categoryJpaRepository.findAllByLevelAndParentId(level, parentId);
    }

    @Override
    public List<Category> findAllByNameAndParentId(String name, Long parentId) {
        return categoryJpaRepository.findAllByNameAndParentId(name, parentId);
    }

}
