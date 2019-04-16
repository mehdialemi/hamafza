package ir.co.realtime.disaster.auth.repository;

import ir.co.realtime.disaster.auth.model.Privilege;
import ir.co.realtime.disaster.auth.model.PrivilegeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    Privilege findByPrivilegeType(PrivilegeType name);

    @Override
    void delete(Privilege privilege);

}
