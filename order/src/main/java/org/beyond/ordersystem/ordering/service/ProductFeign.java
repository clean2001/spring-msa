package org.beyond.ordersystem.ordering.service;

import com.sun.net.httpserver.Authenticator;
import org.beyond.ordersystem.common.config.FeignConfig;
import org.beyond.ordersystem.common.dto.SuccessResponse;
import org.beyond.ordersystem.ordering.dto.product.ProductUpdateStockDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="product-service", configuration = FeignConfig.class)
public interface ProductFeign {
    @GetMapping(value="/product/{id}")
    SuccessResponse getProductById(@PathVariable("id") Long id);

    @PutMapping(value="/product/update-stock")
    void updateProductStock(@RequestBody ProductUpdateStockDto dto);
}
