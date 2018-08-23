package com.ywei.test.rxjava.hotCold

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Consumer
import io.reactivex.observables.ConnectableObservable
import io.reactivex.schedulers.Schedulers

import java.util.concurrent.TimeUnit


/**
 * @Description 演示Cold Observable
 * @author yingzhou.wei
 * @date 2018/8/19
 *
 */
class ColdObservable extends GroovyTestCase {

    def void testColdObservalbe(){
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
        Consumer subscriber3 = new Consumer<Long>() {
            @Override
            void accept(Long aLong) throws Exception {
                println "     subscriber3 : $aLong"
            }
        }

        Observable<Long> observable =  Observable.interval(10,TimeUnit.MILLISECONDS,Schedulers.io())
                .take(100).subscribeOn(Schedulers.newThread())

        observable.subscribe(subscriber1)
        observable.subscribe(subscriber2)
        observable.subscribe(subscriber3)

        Thread.sleep(30000L)
    }

    // cold Observable转为Hot Observable，可以使用publish()
    def void testColdToHotObservable(){
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

        Consumer subscriber3 = new Consumer<Long>() {
            @Override
            void accept(Long aLong) throws Exception {
                println "   subscriber3 : $aLong"
            }
        }

        ConnectableObservable<Long> observable =  Observable.interval(10,TimeUnit.MILLISECONDS,Schedulers.io())
                .take(200).subscribeOn(Schedulers.io()).publish()
        observable.connect()

        observable.subscribe(subscriber1)
        observable.subscribe(subscriber2)

        Thread.sleep(300L)
        observable.subscribe(subscriber3)

        Thread.sleep(30000L)
    }
}
