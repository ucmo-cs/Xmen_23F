package edu.ucmo.cbbackend.repository;

import edu.ucmo.cbbackend.model.Roles;
import org.springframework.data.repository.CrudRepository;

public interface RolesRepository extends CrudRepository<Roles, Long> {


    Roles findByNameIgnoreCase(String name);


}
