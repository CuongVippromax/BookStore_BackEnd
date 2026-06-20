package com.cuong.electronicstore.repository;

import com.cuong.electronicstore.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> getProductByName(String name);

    @Query("select p from Product p where lower(p.name) like :keyword "
            + "or lower(p.brand) like :keyword "
            + "or lower(p.sku) like :keyword "
            + "or lower(p.model) like :keyword "
            + "or lower(p.category.name) like :keyword")
    Page<Product> searchByKeyword(String keyword, Pageable pageable);

    Page<Product> findByCategory_Id(Integer categoryId, Pageable pageable);
}
