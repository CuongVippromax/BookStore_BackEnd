package com.cuong.bookstore.repository;

import com.cuong.bookstore.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    Optional<User> findById(Integer id);
    @Query(value = "select u from User u where (lower(u.username)) like :keyword "
            + "or (lower(u.email)) like :keyword "
            + "or (lower(u.phone)) like :keyword "
            + "or (lower(u.address)) like :keyword")
    Page<User> searchByKeyword(String keyword, Pageable pageable);
}
