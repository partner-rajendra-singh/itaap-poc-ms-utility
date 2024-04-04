package com.philips.itaap.utility.repository;

import com.philips.itaap.utility.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsRepo extends JpaRepository<Comments, Integer> {

}
