package ir.co.realtime.disaster.auth.repository;

import ir.co.realtime.disaster.auth.model.Privilege;
import ir.co.realtime.disaster.auth.model.PrivilegeType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    Privilege findByName(PrivilegeType name);

    @Override
    void delete(Privilege privilege);

}
