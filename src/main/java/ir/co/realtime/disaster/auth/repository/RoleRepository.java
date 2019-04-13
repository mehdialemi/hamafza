package ir.co.realtime.disaster.auth.repository;

import ir.co.realtime.disaster.auth.model.Role;
import ir.co.realtime.disaster.auth.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);


    @Override
    void delete(Role role);

}