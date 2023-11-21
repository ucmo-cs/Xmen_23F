package edu.ucmo.cbbackend.repository;

import edu.ucmo.cbbackend.model.ChangeRequest;
import edu.ucmo.cbbackend.model.ChangeRequestState;
import edu.ucmo.cbbackend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ChangeRepository extends PagingAndSortingRepository<ChangeRequest, Long>, CrudRepository<ChangeRequest, Long> {

    ChangeRequest findById(long id);

    Page<ChangeRequest> findByAuthorAndState(User author, ChangeRequestState state, Pageable pageable);


    Page<ChangeRequest> findByRoles_NameAndState(String name, ChangeRequestState state, Pageable pageable);

    Page<ChangeRequest> findAllByState(ChangeRequestState state, Pageable pageable);


}
