package com.spraxs.oorlogsimulatie.mongo;


import com.spraxs.oorlogsimulatie.mongo.collections.GearUserEntity;
import com.spraxs.oorlogsimulatie.mongo.collections.dao.GearUserDAO;
import com.spraxs.oorlogsimulatie.utils.GearType;
import com.spraxs.oorlogsimulatie.utils.WeaponType;
import lombok.Getter;
import net.zoutepopcorn.core.api.CoreAPI;
import net.zoutepopcorn.core.database.mongo.MongoDBType;
import net.zoutepopcorn.core.database.mongo.collections.MainUserEntity;

public final class MongoManager {

    private static @Getter MongoManager instance;

    private GearUserDAO gearUserDAO;

    public MongoManager() {
        instance = this;

        CoreAPI coreAPI = CoreAPI.getAPI();

        String oorlogsimulatieDataStoreName = "oorlogsimulatie";

        coreAPI.setupMongoDatastore(MongoDBType.I7, oorlogsimulatieDataStoreName);

        this.gearUserDAO = new GearUserDAO(GearUserEntity.class, coreAPI.getMongoDatastore(oorlogsimulatieDataStoreName));
    }

    // --- Gear ---

    public GearUserEntity getGearUser(MainUserEntity mainUserEntity) {
        GearUserEntity user = this.gearUserDAO.findOne("_id", mainUserEntity.getId());

        if (user == null) {

            user = new GearUserEntity();

            user.setId(mainUserEntity.getId());

            user.setUUID(mainUserEntity.getUUID());
            user.setName(mainUserEntity.getName());


            user.setHelmet(GearType.SOLDIER_HELMET);
            user.setChestplate(GearType.SOLDIER_CHESTPLATE);
            user.setLeggings(GearType.SOLDIER_LEGGINGS);
            user.setBoots(GearType.SOLDIER_BOOTS);
            user.setWeapon(WeaponType.SOLDIER_SWORD);

            this.gearUserDAO.save(user);
        }

        return user;
    }

    public void saveGearUser(GearUserEntity user) {
        this.gearUserDAO.save(user);
    }
}
