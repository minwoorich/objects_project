package com.objects.marketbridge.category.service.port;

import java.util.List;

public interface CategoryCustomRepository {

    String findByChildId(Long categoryId);
}
