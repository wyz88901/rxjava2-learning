package com.ywei.test.rxjava.hotCold

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.observables.ConnectableObservable
import io.reactivex.schedulers.Schedulers

import java.util.concurrent.TimeUnit

/**
 * @Description 演示Hot Observable
 * @author yingzhou.wei
 * @date 2018/8/19
 *
 */
class HotObservable extends GroovyTestCase {

    /**
     *  如果所有的订阅者/观察者都取消订阅,则数据流停止
     *  如果重新订阅,则重新开始数据流
     */
    def void testHotToColdObservable(){
        Consumer subscriber1 = new Consumer<Long>() {
            @Override
            void accept(Long aLong) throws Exception {
                println "subscriber1 : $aLong"
            }
        }

        Consumer subscriber2 = new Consumer<Long>() {
            @Override
            void accept(Long aLong) throws Exception {
                println "   subscriber2 : $aLong"
            }
        }

        Observable<Long> observable =  Observable.interval(10,TimeUnit.MILLISECONDS,Schedulers.io())
                .take(30).subscribeOn(Schedulers.io()).publish().refCount()
       // connectableObservable.connect()
      //  Observable<Long> observable = connectableObservable.refCount()

       Disposable disposable1 = observable.subscribe(subscriber1)
       Disposable disposable2 = observable.subscribe(subscriber2)
        Thread.sleep(1000)
         disposable1.dispose()
        disposable2.dispose()

        println "重新开始发射数据......................"
         disposable1 = observable.subscribe(subscriber1)
       disposable2 = observable.subscribe(subscriber2)

        Thread.sleep(5000L)
    }


    def void testPartHotToColdObservable(){
        Consumer subscriber1 = new Consumer<Long>() {
            @Override
            void accept(Long aLong) throws Exception {
                println "subscriber1 : $aLong"
            }
        }

        Consumer subscriber2 = new Consumer<Long>() {
            @Override
            void accept(Long aLong) throws Exception {
                println "   subscriber2 : $aLong"
            }
        }

        Observable<Long> observable =  Observable.interval(10,TimeUnit.MILLISECONDS,Schedulers.io())
                .take(30).subscribeOn(Schedulers.io()).publish().refCount()
        // connectableObservable.connect()
        //  Observable<Long> observable = connectableObservable.refCount()

        Disposable disposable1 = observable.subscribe(subscriber1)
        Disposable disposable2 = observable.subscribe(subscriber2)
        Thread.sleep(1000)
        disposable1.dispose()
       // disposable2.dispose()

        println "重新开始发射数据......................"
        disposable1 = observable.subscribe(subscriber1)
      //  disposable2 = observable.subscribe(subscriber2)

        Thread.sleep(5000L)
    }

}
