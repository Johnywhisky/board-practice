package net.scit.spring7.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import net.scit.spring7.entity.BoardEntity;


public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

	Page<BoardEntity> findByTitleContains(String searchWord, PageRequest of);

	Page<BoardEntity> findByWriterContains(String searchWord, PageRequest of);

	Page<BoardEntity> findByContentContains(String searchWord, PageRequest of);

}
