package com.order.utils.enumutil;


public enum LevelEnums {
    PROVINCE(1, "省"),
    CITY(2, "市"),
    AREA(3, "地区");

    private int id;
    private String name;

    private LevelEnums(int status, String des) {
        this.id = status;
        this.name = des;
    }


    public static LevelEnums of(int status) {
        for (int i = 0; i < values().length; ++i) {
            if (values()[i].getId() == status) {
                return values()[i];
            }
        }
        return PROVINCE;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
