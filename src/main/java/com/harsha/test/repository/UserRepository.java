package com.harsha.test.repository;

import com.harsha.test.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUserName(String userName);

  /* we can also use Method Query - List<User> findByNameContaining(String userName); */
  @Query("SELECT u FROM User u WHERE u.userName LIKE %:userName%")
  List<User> findLikeUserName(@Param("userName") String userName);
}
