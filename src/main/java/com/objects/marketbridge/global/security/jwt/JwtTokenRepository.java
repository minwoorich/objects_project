package com.objects.marketbridge.global.security.jwt;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtTokenRepository extends CrudRepository<JwtToken, String> {


}
