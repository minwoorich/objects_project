package com.objects.marketbridge.member.infra.wishlist;

import com.objects.marketbridge.member.domain.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WishiListJpaRepository extends JpaRepository<Wishlist,Long> {

}