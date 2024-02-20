package com.objects.marketbridge.image.infra;

import com.objects.marketbridge.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface ImageJpaRepository extends JpaRepository<Image, Long> {
    void deleteAll(Iterable<? extends Image> images);

    @Modifying
    @Query("DELETE FROM Image i WHERE i.id IN :ids")
    void deleteByIdIn(@Param("ids") List<Long> ids);

    @Query("SELECT i FROM Image i WHERE i.id IN :ids")
    List<Image> findByIds(@Param("ids") List<Long> ids);

}
