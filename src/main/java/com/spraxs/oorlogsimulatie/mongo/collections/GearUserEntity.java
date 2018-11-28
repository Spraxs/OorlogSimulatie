package com.spraxs.oorlogsimulatie.mongo.collections;

import com.spraxs.oorlogsimulatie.utils.GearType;
import com.spraxs.oorlogsimulatie.utils.WeaponType;
import net.zoutepopcorn.core.database.mongo.user.BaseUser;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;

@Entity(value = "gear_users", noClassnameStored = true)
public class GearUserEntity extends BaseUser {

    @Property("weapon")
    private String selected_weapon;

    @Property("helmet")
    private String selected_helmet;

    @Property("chestplate")
    private String selected_chestplate;

    @Property("leggings")
    private String selected_leggings;

    @Property("boots")
    private String selected_boots;

    public void setId(ObjectId objectId) {
        this.id = objectId;
    }

    // --- Weapon ---
    public WeaponType getWeapon() {
        for (WeaponType weaponType : WeaponType.values()) {
            if (weaponType.getName().equals(this.selected_weapon)) {
                return weaponType;
            }
        }

        return null;
    }

    public void setWeapon(WeaponType weapon) {
        this.selected_weapon = weapon.getName();
    }

    // --- Helmet ---
    public GearType getHelmet() {
        for (GearType gearType : GearType.values()) {
            if (gearType.getName().equals(this.selected_helmet)) {
                return gearType;
            }
        }

        return null;
    }

    public void setHelmet(GearType helmet) {
        this.selected_helmet = helmet.getName();
    }

    // --- Chestplate ---
    public GearType getChestplate() {
        for (GearType gearType : GearType.values()) {
            if (gearType.getName().equals(this.selected_chestplate)) {
                return gearType;
            }
        }

        return null;
    }

    public void setChestplate(GearType chestplate) {
        this.selected_chestplate = chestplate.getName();
    }

    // --- Leggings ---
    public GearType getLeggings() {
        for (GearType gearType : GearType.values()) {
            if (gearType.getName().equals(this.selected_leggings)) {
                return gearType;
            }
        }

        return null;
    }

    public void setLeggings(GearType leggings) {
        this.selected_leggings = leggings.getName();
    }

    // --- Boots ---
    public GearType getBoots() {
        for (GearType gearType : GearType.values()) {
            if (gearType.getName().equals(this.selected_boots)) {
                return gearType;
            }
        }

        return null;
    }

    public void setBoots(GearType boots) {
        this.selected_boots = boots.getName();
    }
}
