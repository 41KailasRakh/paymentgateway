package org.paygateway.repositoryimpl;

import org.paygateway.exceptions.ProductException;
import org.paygateway.model.Product;
import org.paygateway.pojo.CreateProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    Product createProduct(CreateProductRequest productRequest);
    String deleteProduct(Long productId) throws ProductException;
    Product updateProduct(Long productId, Product product) throws ProductException;
    Product findProductById(Long productId) throws ProductException;
    List<Product> findProductByCategory(String category);
    Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize);
    List<Product> findAllProducts();

}
