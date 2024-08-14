package org.beyond.ordersystem.product.controller;

import lombok.RequiredArgsConstructor;
import org.beyond.ordersystem.common.dto.ProductUpdateStockDto;
import org.beyond.ordersystem.common.dto.SuccessResponse;
import org.beyond.ordersystem.product.domain.Product;
import org.beyond.ordersystem.product.dto.CreateProductRequest;
import org.beyond.ordersystem.product.dto.ProductResponse;
import org.beyond.ordersystem.product.dto.ProductSearchDto;
import org.beyond.ordersystem.product.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@RequiredArgsConstructor
@RestController
@RefreshScope // 해당 어노테이션 사용시 아래 스프링 빈은 실시간 config 변경 사항의 대상이 된다.
public class ProductController {
    private final ProductService productService;

    @Value("${message.hello}")
    private String helloWorld;
    @GetMapping("/product/config/test")
    public String configTest() {
        return helloWorld;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/product/create")
    public ResponseEntity<SuccessResponse> createProduct(@ModelAttribute CreateProductRequest createProductRequest) {
        Long productId = productService.createAwsProduct(createProductRequest);

        SuccessResponse response = SuccessResponse.builder()
                .httpStatus(HttpStatus.CREATED)
                .result(productId)
                .statusMessage("상품이 등록되었습니다.")
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/product/list")
    public ResponseEntity<SuccessResponse> productList(ProductSearchDto searchDto, @PageableDefault(size = 10) Pageable pageable) {
        // 검색을 위해 Specification 객체 사용
        // Specification 객체는 복잡한 쿼리를 명세를 이용하여 정의하는 방식으로 쿼리를 쉽게 생성

        Page<ProductResponse> productList = productService.productList(searchDto, pageable);
        SuccessResponse response = SuccessResponse.builder()
                .statusMessage("상품 리스트입니다.")
                .httpStatus(HttpStatus.OK)
                .result(productList)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> productDetail(@PathVariable Long id) {
        ProductResponse product = productService.productDetail(id);
        SuccessResponse response = SuccessResponse.builder()
                .statusMessage("상품 단건 정보입니다.")
                .httpStatus(HttpStatus.OK)
                .result(product)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/product/update-stock")
    public ResponseEntity<?> productStockUpdate(@RequestBody ProductUpdateStockDto dto) {
        Product product = productService.productUpdateStock(dto);

        SuccessResponse response = SuccessResponse.builder()
                .statusMessage("update is successful" + dto.getProductId())
                .httpStatus(HttpStatus.OK)
                .result(null)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
