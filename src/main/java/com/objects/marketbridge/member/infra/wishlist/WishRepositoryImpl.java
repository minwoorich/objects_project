package com.objects.marketbridge.member.infra.wishlist;

import com.objects.marketbridge.member.domain.QMember;
import com.objects.marketbridge.member.domain.Wishlist;
import com.objects.marketbridge.member.service.port.WishRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.objects.marketbridge.member.domain.QMember.member;
import static com.objects.marketbridge.member.domain.QWishlist.wishlist;
import static com.objects.marketbridge.product.domain.QOption.option;
import static com.objects.marketbridge.product.domain.QProdOption.prodOption;
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
    public List<Wishlist> findByMemberId(Long memberId) {
       return queryFactory
               .select(wishlist)
               .from(wishlist)
               .join(wishlist.member , member).fetchJoin()
               .join(wishlist.productOption , prodOption).fetchJoin()
               .join(prodOption.product , product).fetchJoin()
               .join(prodOption.option, option).fetchJoin()
               .fetch();
    }

    @Override
    public Wishlist save(Wishlist wishlist) {
        return wishiListJpaRepository.save(wishlist);
    }

}
