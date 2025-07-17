package com.example.si.repository;

import com.example.si.entity.Liked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikedRepository extends JpaRepository<Liked, Integer> {


    List<Liked> findLikedByUserEmail(String email);
////////////////////////
   Optional<Liked>  findLikedByProductId(int id);

    void deleteByProductId(int id);

    void deleteById(int id);
}
