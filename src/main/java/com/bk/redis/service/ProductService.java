package com.bk.redis.service;

import com.bk.redis.dto.ProductDto;
import com.bk.redis.entity.Product;
import com.bk.redis.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @CachePut(value = "PRODUCT_CACHE", key="#result.id()")
    public ProductDto createProduct(ProductDto productDto) {
        var product = new Product();
        product.setName(productDto.name());
        product.setPrice(productDto.price());
        Product entity = productRepository.save(product);
        return new ProductDto(entity.getId(), entity.getName(), entity.getPrice());
    }

    @CachePut(value = "PRODUCT_CACHE")
    public List<ProductDto> createMultipleProduct(List<ProductDto> productDto) {
        List<Product> productEntities = productDto.stream()
                .map(dto -> {
                    Product entity = new Product();
                    entity.setName(dto.name());
                    entity.setPrice(dto.price());
                    return entity;
                })
                .toList();
        List<Product> savedEntities = productRepository.saveAll(productEntities);
        return savedEntities.stream()
                .map(entity ->
                    new ProductDto(entity.getId(), entity.getName(), entity.getPrice())
                )
                .toList();
    }

    @Cacheable(value="PRODUCT_CACHE", key="#productId")
    public ProductDto getProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find product with id:"+productId));
        return new ProductDto(product.getId(),
                product.getName(), product.getPrice());
    }

    @CachePut(value = "PRODUCT_CACHE", key="#result.id()")
    public ProductDto updateProduct(ProductDto productDto) {
        Long productId = productDto.id();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find product with id:"+productId));

        product.setName(productDto.name());
        product.setPrice(productDto.price());

        Product savedProduct = productRepository.save(product);
        return new ProductDto(savedProduct.getId(),
                savedProduct.getName(), savedProduct.getPrice());
    }

    @CacheEvict(value="PRODUCT_CACHE", key = "@productId")
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    @Cacheable(value="PRODUCT_CACHE")
    public List<ProductDto> getProducts() {
        return productRepository.findAll()
                .stream()
                .map(product -> new ProductDto(product.getId(),
                        product.getName(), product.getPrice()))
                .toList();
    }
}
