package com.elevate.elevateapi.repository;

import com.elevate.elevateapi.dto.friends.FriendResponse;
import com.elevate.elevateapi.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("select count(f) > 0 from Friend f " +
            "where f.user.id = :a and f.friend.id = :b")
    boolean existsBetween(@Param("a") Long smaller, @Param("b") Long larger);


    @Query("""
        select u.id
        from Friend f
        join User u on
             (u.id = f.user.id   and f.friend.id = :id)
          or (u.id = f.friend.id and f.user.id   = :id)
    """)
    List<Long> findFriendsOf(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("""
        delete from Friend f
        where f.user.id   = :a and f.friend.id = :b
    """)
    void deleteSymmetric(@Param("a") Long smaller, @Param("b") Long larger);

}