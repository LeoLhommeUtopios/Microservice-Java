package org.example.demomicroservice.repository;

import org.example.demomicroservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository  extends JpaRepository<Product,Long> {

    List<Product> findProductByName (String name);

//    @Query("SELECT p FROM Product p where " +
//            "LOWER(p.name) like LOWER(CONCAT('%', :searchName, '%')) " +
//            "OR p.price < :price")
//    List<Product> search (@Param("searchName") String searchName, @Param("price") double price);

    boolean existsByName (String name);


}
