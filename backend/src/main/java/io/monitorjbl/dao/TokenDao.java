package io.monitorjbl.dao;

import io.monitorjbl.model.Token;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TokenDao extends PagingAndSortingRepository<Token, String> {}
