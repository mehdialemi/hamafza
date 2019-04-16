package ir.co.realtime.disaster.auth.service;

import ir.co.realtime.disaster.auth.model.*;
import ir.co.realtime.disaster.auth.repository.PrivilegeRepository;
import ir.co.realtime.disaster.auth.repository.RoleRepository;
import ir.co.realtime.disaster.auth.repository.UserRepository;
import ir.co.realtime.disaster.report.Report;
import ir.co.realtime.disaster.report.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${auth.admin.username:admin}")
    private String adminUsername;

    @Value("${auth.admin.password:asdf/1234}")
    private String adminPassword;

    boolean alreadySetup = false;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (alreadySetup)
            return;

        // == create initial privileges
        final Privilege readPrivilege = createPrivilegeIfNotFound(PrivilegeType.READ_PRIVILEGE);
        final Privilege writePrivilege = createPrivilegeIfNotFound(PrivilegeType.WRITE_PRIVILEGE);
        final Privilege passwordPrivilege = createPrivilegeIfNotFound(PrivilegeType.CHANGE_PASSWORD_PRIVILEGE);

        // == create initial roles
        final List<Privilege> adminPrivileges = new ArrayList <>(Arrays.asList(readPrivilege, writePrivilege, passwordPrivilege));
        final List<Privilege> userPrivileges = new ArrayList <>(Arrays.asList(readPrivilege, passwordPrivilege));
        final Role adminRole = createRoleIfNotFound(RoleName.ROLE_ADMIN, adminPrivileges);

        createRoleIfNotFound(RoleName.ROLE_USER, userPrivileges);
        createRoleIfNotFound(RoleName.ROLE_OPERATOR, adminPrivileges);

        createAdminIfNotFound(adminRole);
        createReportIfNotExist();
        alreadySetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(final PrivilegeType privilegeType) {
        Privilege privilege = privilegeRepository.findByPrivilegeType(privilegeType);
        if (privilege == null) {
            privilege = new Privilege(privilegeType);
            privilege = privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(RoleName roleName, final Collection<Privilege> privileges) {
        Optional <Role> byName = roleRepository.findByName(roleName);
        Role role = byName.orElse(new Role(roleName));
        role.setPrivileges(privileges);
        role = roleRepository.save(role);
        return role;
    }

    @Transactional
    User createAdminIfNotFound(Role role) {
        Optional <User> byUsername = userRepository.findByUsername(adminUsername);
        User user = byUsername.orElse(new User(adminUsername));
        user.setPassword(passwordEncoder.encode(adminPassword));
        user.setRoles(Collections.singleton(role));
        return userRepository.save(user);
    }

    void createReportIfNotExist() {
        Optional <Report> byId = reportRepository.findById(1L);
        Report report  = byId.orElse(new Report());
        report.setSource(Report.Source.GOVERNMENT);
        reportRepository.save(report);
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public String getAdminPassword() {
        return adminPassword;
    }
}
