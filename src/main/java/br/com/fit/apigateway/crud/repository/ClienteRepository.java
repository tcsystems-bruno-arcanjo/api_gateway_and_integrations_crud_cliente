package br.com.fit.apigateway.crud.repository;

import br.com.fit.apigateway.crud.model.Cliente;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "clientes", path = "clientes")
public interface ClienteRepository extends PagingAndSortingRepository<Cliente, Long> {

    List<Cliente> findByNome(@Param("nome") String nome);
}
