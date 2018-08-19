package com.ywei.test.rxjava.hotel;

import com.google.common.collect.Lists;
import com.ywei.test.rxjava.hotel.model.Hotel;
import com.ywei.test.rxjava.hotel.model.Room;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

import java.util.List;
import java.util.Random;

/**
 * @author yingzhou.wei
 * @Description  举例展示 rxjava
 * @date 2018/8/18
 */
public class HotelDisplay {
    public static void main(String[] args) throws InterruptedException {

        HotelDisplay display = new HotelDisplay();
        display.tranditionDisplay();

    }

    // 传统Java实现方式
    public void tranditionDisplay(){
        new Thread(){
            @Override
            public void run(){
                List<Hotel> hotelList = getHotelsFromServer();

                hotelList.stream().forEach(hotel -> {
                     hotel.getRoomList().stream().filter(room ->
                         room.getPrice() > 500
                      ).forEach(room -> {
                          new Thread(new Runnable(){

                              @Override
                              public void run() {
                                  displayUI(room);
                              }
                          }).start();
                     });
                });
            }
        }.start();
    }

    // rxjava实现方式
    public void rxJavaDisplay() throws InterruptedException {
        Observable.fromIterable(getHotelsFromServer())
                .flatMap(new Function<Hotel, ObservableSource<Room>>() {
                    @Override
                    public ObservableSource<Room> apply(Hotel hotel) throws Exception {
                        return Observable.fromIterable(hotel.getRoomList());
                    }
                }).filter(new Predicate<Room>() {
            @Override
            public boolean test(Room room) throws Exception {
                return room.getPrice() > 500;
            }
        }).subscribeOn(Schedulers.io()).subscribe(new Consumer<Room>() {
            @Override
            public void accept(Room room) throws Exception {
                displayUI(room);
            }
        });

        // Lambda表达式 实现
        Observable.fromIterable(getHotelsFromServer())
                .flatMap(hotel ->Observable.fromIterable(hotel.getRoomList()))
                .filter(room -> room.getPrice() > 500)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(this::displayUI);

        Thread.sleep(100000L);

    }

    /**
     *  模拟从数据库获取所有房间
     * @return
     */
    public   List<Hotel> getHotelsFromServer(){
        List<Hotel> hotelList = Lists.newArrayList();

        int hcnt = 0;
        while(hcnt < 20){
            int cnt = 1;
            List<Room> roomList = Lists.newArrayList();
            Hotel hotel = new Hotel();
            hotel.setId(Long.valueOf(hcnt));
            hotel.setName("酒店"+cnt);
            while(cnt < 150){
                Random random = new Random();
                Room room = new Room();
                Integer id = random.nextInt(500);
                room.setId(Long.valueOf(id));
                room.setName("test" + id.toString());
                room.setType(getType(id));
                room.setPrice(Math.ceil((id*10)/2));
                room.setOrientation(getOrientation(cnt));
                roomList.add(room);
                cnt ++;
            }
            hotel.setRoomList(roomList);
            hotelList.add(hotel);
            hcnt++;
        }
        return hotelList;
    }

    /**
     *  Mock: 将房间信息添加到UI上
     * @param room
     */
    public   void displayUI(Room room){
        System.out.println(room);
    }

    private   String getOrientation(Integer id){
        String result = null;
        switch (Math.floorMod(id,4)){
            case 0:
                result = "朝东";
                break;
            case 1:
                result = "朝南";
                break;
            case 2:
                result = "朝西";
                break;
            case 3:
                result = "朝北";
                break;
        }
        return result;
    }

    private   String getType(Integer id){
        String result = null;
        switch (Math.floorMod(id,3)){
            case 0:
                result = "单间";
                break;
            case 1:
                result = "双人间";
                break;
            case 2:
                result = "豪华套房";
                break;
        }
        return result;
    }

}
