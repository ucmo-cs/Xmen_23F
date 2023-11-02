package edu.ucmo.cbbackend.repository;

import edu.ucmo.cbbackend.model.ChangeRequest;
import edu.ucmo.cbbackend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ChangeRepository extends PagingAndSortingRepository<ChangeRequest, Long>, CrudRepository<ChangeRequest, Long> {

    ChangeRequest findById(long id);

    Page<ChangeRequest> findAllByAuthor(User author, Pageable pageable);


}
