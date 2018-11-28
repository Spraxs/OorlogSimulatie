package com.spraxs.oorlogsimulatie.mongo.collections.dao;

import com.spraxs.oorlogsimulatie.mongo.collections.GearUserEntity;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

public class GearUserDAO extends BasicDAO<GearUserEntity, String> {

    public GearUserDAO(Class<GearUserEntity> entityClass, Datastore datastore) {
        super(entityClass, datastore);
    }
}
