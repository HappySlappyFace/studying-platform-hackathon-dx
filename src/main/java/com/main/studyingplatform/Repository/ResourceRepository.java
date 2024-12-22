package com.main.studyingplatform.Repository;

import com.main.studyingplatform.Entities.Resource;
import com.main.studyingplatform.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findByUploadedBy(User user);
}
