package com.storesystem.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.storesystem.backend.model.entity.DailyCount;


@Repository
public interface DailyCountRepository extends JpaRepository<DailyCount, Long>{

	@Query(value = "SELECT * FROM daily_count "
			+ "WHERE daily_count_name = :name "
			+ "FOR UPDATE "
			, nativeQuery = true)
	Optional<DailyCount> findByNameForUpdate(@Param("name") String name);
	
}
