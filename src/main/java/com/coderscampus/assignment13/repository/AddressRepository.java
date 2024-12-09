package com.coderscampus.assignment13.repository;

import com.coderscampus.assignment13.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
