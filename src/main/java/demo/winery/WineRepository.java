package demo.winery;

import org.springframework.data.jpa.repository.JpaRepository;

interface WineRepository extends JpaRepository<Wine, Long> {
}
