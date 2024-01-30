package com.objects.marketbridge;

import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.infra.OrderCommendRepositoryImpl;
import com.objects.marketbridge.order.service.port.OrderCommendRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MarketbridgeApplication {
	public static void main(String[] args) {
		SpringApplication.run(MarketbridgeApplication.class, args);
	}
}