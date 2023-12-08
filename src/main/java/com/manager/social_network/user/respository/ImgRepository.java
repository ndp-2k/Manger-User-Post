package com.manager.social_network.user.respository;

import com.manager.social_network.user.entity.Img;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImgRepository extends JpaRepository<Img, Long> {

    @Query("SELECT i FROM Img i WHERE i.imgId = :imgId AND i.createAt = (SELECT MAX(i2.createAt) FROM Img i2 WHERE i2.imgId = :imgId) AND i.type=:type")
    Optional<Img> findLatestImgByImgId(Long imgId, String type);


    List<Img> findAllByImgId(Long userId);
}
