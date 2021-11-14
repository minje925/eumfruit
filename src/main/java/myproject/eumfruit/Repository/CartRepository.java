package myproject.eumfruit.Repository;

import myproject.eumfruit.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
