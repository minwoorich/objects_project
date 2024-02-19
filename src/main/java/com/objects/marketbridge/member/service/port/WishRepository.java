package com.objects.marketbridge.member.service.port;

import com.objects.marketbridge.member.domain.Wishlist;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface WishRepository {

   Slice<Wishlist> findByMemberId(Pageable pageable, Long memberId);

   Long countByProductIdAndMemberId(Long memberId, Long productId);
   Wishlist save(Wishlist wishlist);

   void saveAll(List<Wishlist> wishlist);
}
