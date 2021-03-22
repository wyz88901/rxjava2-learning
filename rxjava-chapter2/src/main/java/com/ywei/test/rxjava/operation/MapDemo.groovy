package com.ywei.test.rxjava.operation

import com.google.common.collect.Lists
import com.ywei.test.rxjava.operation.model.Address
import com.ywei.test.rxjava.operation.model.User
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

import java.util.concurrent.TimeUnit

/**
 * @Description 变换操作 -- Map相关操作符
 * @author yingzhou.wei
 * @date 2018/9/23
 *
 */
class MapDemo extends GroovyTestCase{

    /**
     * Map: 对Observable发射的每一项数据应用一个函数，执行变换操作
     *
     */
    void testMap(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1)
                emitter.onNext(2)
                emitter.onNext(3)
                emitter.onComplete()
            }
        }).map(new Function<Integer, String>() {
            @Override
            String apply(@NonNull Integer integer) throws Exception {
                return "This is result " + integer
            }
        }).subscribe(new Observer<String>() {

            @Override
            void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            void onNext(@NonNull String s) {
                println "receive message: " + s
            }

            @Override
            void onError(@NonNull Throwable e) {

            }

            @Override
            void onComplete() {
                println "receive  complete signal "
            }
        })
    }

    /**
     *  Map demo
     */
    void testMapByLine(){
        Observable.just("HELLO WORLD","SPRING . SUMMER")
          .map(new Function<String,String>(){

            @Override
            String apply(@NonNull String s) throws Exception {
                return s.toLowerCase()
            }
        }).subscribe(new Consumer<String>(){

            @Override
            void accept(String s) throws Exception {
                println " 转换后的字符串: $s"
            }
        })
    }

    /**
     * FlatMap: 将一个发射数据的Observable变换为多个Observables，
     *          然后将它们发射的数据合并后放进一个单独的Observable, 不能保证事件的顺序
     */
    void testFlatMap(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                def range = 0..4
                range.each {it -> emitter.onNext(it)}
                emitter.onComplete()
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            ObservableSource<String> apply(@NonNull Integer integer) throws Exception {
                List<String> list = new ArrayList<>();
                [0,1,2,3].each {it ->
                    list.add( "我是事件 " + integer + "拆分后的子事件" + it)
                }
                //随机生成一个时间
                int delayTime = (int) (1 + Math.random() * 10);
                return Observable.fromIterable(list).delay(delayTime, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            void accept(String s) throws Exception {
                println "accept: " + s ;
            }
        } )
        Thread.sleep(5000)
    }


    void testFlatMap2(){
        def user = new User()
        user.name="James"
        user.mobile="13878976521"
        user.id="2232"
        user.age="33"
        user.sex="M"

        def addressList = Lists.newArrayList()
        def address = new Address()
        address.city="SHANGHAI"
        address.street="淞虹路"
        addressList.add(address)

        address = new Address()
        address.city="苏州"
        address.street="人民路"
        addressList.add(address)
        user.addressList=addressList

    }

    /**
     * ConcatMap: ConcatMap 与 FlatMap 的唯一区别就是 concatMap 保证了顺序
     */
    void testConcatMap(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                def range = 0..3
                range.each {it -> emitter.onNext(it)}
                emitter.onComplete()
            }
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            ObservableSource<String> apply(@NonNull Integer cnt) throws Exception {
                def list = Lists.newArrayList()

                [0,1,2,3].each {it ->

                    list.add( "我是事件 " + cnt + "拆分后的子事件" + it)
                }
                //随机生成一个时间
                int delayTime = (int) (1 + Math.random() * 10);
                return Observable.fromIterable(list).delay(delayTime, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            void accept(String result) throws Exception {
                println "concatmap receive msg: " + result
            }
        })
        Thread.sleep(5000)
    }


    void testZip(){
        Observable.zip( Observable<Address>.empty(),Observable<User>.empty(),
                Observable.create(new ObservableOnSubscribe<Integer>() {

                    @Override
                    void subscribe( ObservableEmitter<Integer> emitter) throws Exception {
                        emitter.onNext(1)
                        emitter.onComplete()
                    }
                }).subscribeOn(Schedulers.io()), (Observable<Address>)address,user, count ->{
                   if(addresss.is){
                       println "address is null"
                   }
                    if(user == null){
                        println "user is null"
                    }

        })

    }
}
