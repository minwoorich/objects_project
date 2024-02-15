package com.objects.marketbridge.member.service.port;

import com.objects.marketbridge.member.domain.Wishlist;

import java.util.List;

public interface WishRepository {

  List<Wishlist> findByMemberId(Long memberId);

   Wishlist save(Wishlist wishlist);
}
