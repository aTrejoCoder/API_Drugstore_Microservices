package microservice.client_service.Repository;


import microservice.client_service.Model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Page<Client> findAllByOrderByLastNameAscFirstNameAsc(Pageable pageable);
}
