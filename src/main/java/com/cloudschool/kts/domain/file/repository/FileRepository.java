package com.cloudschool.kts.domain.file.repository;

import com.cloudschool.kts.domain.file.model.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

}
