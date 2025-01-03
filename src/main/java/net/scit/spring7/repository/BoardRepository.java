package net.scit.spring7.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import net.scit.spring7.entity.BoardEntity;


public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

	List<BoardEntity> findByTitleContains(String searchWord, Sort by);

	List<BoardEntity> findByWriterContains(String searchWord, Sort by);

	List<BoardEntity> findByContentContains(String searchWord, Sort by);

}
