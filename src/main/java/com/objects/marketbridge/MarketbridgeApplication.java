package com.objects.marketbridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableJpaAuditing -> JpaAuditingConfig.class 파일로 (common/config) 따로 분리하였습니다 (컨트롤러 테스트시 문제발생)
// [주의] 만일 @SpringBootTest 대신 @DataJpaTest 을 사용하신다면
// JpaAuditing관련 에러가 발생하므로,
// 해당 테스트 코드클래스에는 @Import(JpaAuditingConfig.class) 를 해주셔야합니다.
@SpringBootApplication
public class MarketbridgeApplication {
	public static void main(String[] args) {
		SpringApplication.run(MarketbridgeApplication.class, args);
	}
}