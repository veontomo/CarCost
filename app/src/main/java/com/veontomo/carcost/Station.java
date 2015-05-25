package com.veontomo.carcost;

/**
 * Created by Mario Rossi on 21/05/2015 at 20:22.
 *
 * @author veontomo@gmail.com
 * @since 0.1
 */
public class Station {
    private String distributor;
    private String name;
    private String country;
    private String street;
    private String building;
    private Float latitude;
    private Float longitude;


    public void Station(String name, String distributor, String country, String street, String building){
        this.name = name;
        this.distributor = distributor;
        this.country = country;
        this.street = street;
        this.building = building;
    }

    public void save(){

    }
}
