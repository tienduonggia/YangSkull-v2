package com.yangskull.admin.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.yangskull.common.entity.Role;

@Repository
public interface RoleRepository  extends CrudRepository<Role, Integer>{

}
