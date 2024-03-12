package ru.gb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gb.model.PowerConsumer;

@Repository
public interface PowerConsumerRepository extends JpaRepository<PowerConsumer, Long> {
}
