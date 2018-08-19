package com.ywei.test.rxjava.hotel.model;

import groovy.transform.SelfType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;
import java.util.List;

/**
 * @author yingzhou.wei
 * @Description TODO
 * @date 2018/8/18
 */
@Setter
@Getter
@ToString
public class Hotel {
    private Long id;
    private  String name;
    private List<Room> roomList;

}
