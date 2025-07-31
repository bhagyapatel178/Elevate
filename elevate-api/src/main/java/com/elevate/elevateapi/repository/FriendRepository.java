package com.elevate.elevateapi.repository;

import com.elevate.elevateapi.dto.FriendResponse;
import com.elevate.elevateapi.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("select count(f) > 0 from Friend f " +
            "where f.user.id = :a and f.friend.id = :b")
    boolean existsBetween(@Param("a") Long smaller, @Param("b") Long larger);


    @Query("""
        select new com.elevate.elevateapi.dto.FriendResponse(
            u.id, u.username)
        from Friend f
        join User u on
             (u.id = f.user.id   and f.friend.id = :id)
          or (u.id = f.friend.id and f.user.id   = :id)
    """)
    List<FriendResponse> findFriendsOf(@Param("id") Long id);

    @Query("""
        delete from Friend f
        where f.user.id   = :a and f.friend.id = :b
    """)
    void deleteSymmetric(@Param("a") Long smaller, @Param("b") Long larger);

}