package com.cuong.bookstore.repository;

import com.cuong.bookstore.model.RedisToken;
import org.springframework.data.repository.CrudRepository;

public interface RedisTokenRepository extends CrudRepository<RedisToken,String> {
}
