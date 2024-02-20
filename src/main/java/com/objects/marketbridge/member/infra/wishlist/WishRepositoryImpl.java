package com.objects.marketbridge.member.infra.wishlist;

import com.objects.marketbridge.member.domain.Wishlist;
import com.objects.marketbridge.member.service.port.WishRepository;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.objects.marketbridge.member.domain.QMember.member;
import static com.objects.marketbridge.member.domain.QWishlist.wishlist;
import static com.objects.marketbridge.product.domain.QProduct.product;

@Repository
public class WishRepositoryImpl implements WishRepository {

    private final WishiListJpaRepository wishiListJpaRepository;
    private final JPAQueryFactory queryFactory;

    public WishRepositoryImpl(WishiListJpaRepository wishiListJpaRepository, EntityManager em) {
        this.wishiListJpaRepository = wishiListJpaRepository;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Long countByProductIdAndMemberId(Long memberId, Long productId) {
        return queryFactory
                .select(ExpressionUtils.count(wishlist))
                .from(wishlist)
                .where(wishlist.member.id.eq(memberId)
                        .and(wishlist.product.id.eq(productId)))
                .fetchOne();
    }

    @Override
    public void deleteWishlist(Long memberId, Long productId) {
        wishiListJpaRepository.deleteByMemberIdAndProductId(memberId,productId);
    }

    @Override
    public Wishlist findbyId(Long wishlistId) {
        return wishiListJpaRepository.findById(wishlistId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<Wishlist> findByMemberId(Long memberId) {
        return wishiListJpaRepository.findByMemberId(memberId);
    }

    @Override
    public Slice<Wishlist> findByMemberId (Pageable pageable, Long memberId) {
        List<Wishlist> wishlistResult = queryFactory
                .selectFrom(wishlist)
                .join(wishlist.product, product).fetchJoin()
                .join(wishlist.member, member).fetchJoin()
                .where(wishlist.member.id.eq(memberId))
                .orderBy(wishlist.createdAt.desc())
                .limit(pageable.getPageSize()+1)
                .offset(pageable.getOffset())
                .fetch();

        boolean hasNext = false;
        if (wishlistResult.size() > pageable.getPageSize()) {
            wishlistResult.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(wishlistResult,pageable,hasNext);
    }

    @Override
    public void saveAll(List<Wishlist> wishlist) {
        wishiListJpaRepository.saveAll(wishlist);
    }

    @Override
    public Wishlist save(Wishlist wishlist) {
        return wishiListJpaRepository.save(wishlist);
    }

}
