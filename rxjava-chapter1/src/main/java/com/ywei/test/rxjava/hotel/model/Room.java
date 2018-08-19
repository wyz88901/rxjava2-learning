package com.ywei.test.rxjava.hotel.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author yingzhou.wei
 * @Description 酒店房间类
 * @date 2018/8/18
 */
@Setter
@Getter
@ToString
public class Room {
    private Long id;
    private String name;
    private String type; // 房间类型
    private Double price;
    private String orientation; //朝向: 东、南、西、北
}
