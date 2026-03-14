package com.bk.redis.controller;

import com.bk.redis.dto.ProductDto;
import com.bk.redis.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<ProductDto> getProducts() {

        return productService.getProducts();
    }

    @PostMapping("/single")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public List<ProductDto> createProduct(@RequestBody List<ProductDto> productDtoList) {
        return productService.createMultipleProduct(productDtoList);
    }

    @GetMapping("/{productId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ProductDto getProduct(@PathVariable Long productId) {
        return productService.getProduct(productId);
    }

    @PutMapping
    @ResponseStatus(value = HttpStatus.OK)
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {
        return productService.updateProduct(productDto);
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long productId) {
        this.productService.deleteProduct(productId);
    }
}
