package myproject.eumfruit.Repository;

import myproject.eumfruit.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
