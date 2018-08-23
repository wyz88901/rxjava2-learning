package com.ywei.test.rxjava.subject

import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.subjects.BehaviorSubject

/**
 * @Description 演示 BehaviorSubject
 *                 Observer会先接受到BehaviorSubject被订阅之前的最后一个数据,再接收订阅之后发射过来的数据
 *                 如果BehaviorSubject被订阅之前没有发射任何数据,则会发射一个默认数据
 * @author yingzhou.wei
 * @date 2018/8/5
 *
 */
class BehaviorSubjectDemo extends GroovyTestCase{

    // 此方法演示BehaviorSubject被订阅之前没有发射任何数据,只有默认数据
    void testBehaviorSubject(){
        BehaviorSubject subject = BehaviorSubject.createDefault("test1")
        subject.subscribe(new Consumer<String>() {
            @Override
            void accept(String s) throws Exception {
                println "BehaviorSubject receive msg: $s "

            }
        },new Consumer<Throwable>() {
            @Override
            void accept(Throwable throwable) throws Exception {
                println "BehaviorSubject receive error msg:   " + throwable.getMessage()
            }
        },new Action() {
            @Override
            void run() throws Exception {
                println "BehaviorSubject receive complete signal ....   "
            }
        })
        subject.onNext(" test 2")
        subject.onNext(" test 3")
    }


    // 此方法演示BehaviorSubject被订阅之前有发射任何数据,
    void testBehaviorSubject1(){
        BehaviorSubject subject = BehaviorSubject.createDefault("test1")
        subject.onNext(" test 2")
        subject.onNext(" test 3")
        subject.subscribe(new Consumer<String>() {
            @Override
            void accept(String s) throws Exception {
                println "BehaviorSubject receive msg: $s "

            }
        },new Consumer<Throwable>() {
            @Override
            void accept(Throwable throwable) throws Exception {
                println "BehaviorSubject receive error msg:   " + throwable.getMessage()
            }
        },new Action() {
            @Override
            void run() throws Exception {
                println "BehaviorSubject receive complete signal ....   "
            }
        })
        subject.onNext(" test 4")
        subject.onNext(" test 5")
    }

}
