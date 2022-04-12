package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Client;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ClientRepositoryWithBagRelationships {
    Optional<Client> fetchBagRelationships(Optional<Client> client);

    List<Client> fetchBagRelationships(List<Client> clients);

    Page<Client> fetchBagRelationships(Page<Client> clients);
}
