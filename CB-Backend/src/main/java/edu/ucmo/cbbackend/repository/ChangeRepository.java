package edu.ucmo.cbbackend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.ucmo.cbbackend.model.ChangeRequest;

@Repository
public interface ChangeRepository  extends CrudRepository<ChangeRequest, Long> {

    ChangeRequest findByID (long id);
    ChangeRequest findByStatus (String status);

}
