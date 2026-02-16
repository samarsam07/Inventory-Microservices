package com.samar.product.dao;

import com.samar.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {

     Product findByProductName(String name);

    @Query("SELECT p.id FROM Product p WHERE p.productName = :productName")
     Integer findOneById(@Param("productName") String  name);
}
