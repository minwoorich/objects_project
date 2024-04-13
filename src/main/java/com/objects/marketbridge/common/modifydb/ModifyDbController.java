package com.objects.marketbridge.common.modifydb;

import com.objects.marketbridge.common.responseobj.ApiResponse;
import com.objects.marketbridge.common.security.annotation.UserAuthorize;
import com.objects.marketbridge.domains.product.service.ProductGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@UserAuthorize
@RequestMapping("/update-db")
public class ModifyDbController {

    private final ProductGroupService productGroupService;

    @PostMapping("/product-to-product-group")
    public ApiResponse<String> productToProductGroup() {

        productGroupService.productToProductGroup();

        return ApiResponse.ok("good");
    }
}
