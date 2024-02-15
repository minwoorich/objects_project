package com.objects.marketbridge.member.infra;

import com.objects.marketbridge.member.domain.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishiListJpaRepository extends JpaRepository<Wishlist,Long> {


}
