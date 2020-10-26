package com.picard.community.community.dao;

import org.springframework.stereotype.Repository;

@Repository("alphahibernate")
public class AlphaDaoHiberImpl implements AlphaDao{
    @Override
    public String select() {
        return "Hibernate";
    }
}
