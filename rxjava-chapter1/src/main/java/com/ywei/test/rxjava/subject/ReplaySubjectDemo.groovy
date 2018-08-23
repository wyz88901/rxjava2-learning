package com.ywei.test.rxjava.subject

import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.ReplaySubject

import java.util.concurrent.TimeUnit

/**
 * @Description 演示 ReplaySubject
 *                  ReplaySubject会发射所有来自原始Observable的数据给观察者,无论它们何时发射
 *                  可以通过createWithSize()来限制缓存数据的数量
 *                  可以通过createWithTime()来限制缓存的时间
 * @author yingzhou.wei
 * @date 2018/8/22
 *
 */
class ReplaySubjectDemo extends GroovyTestCase{

    void testReplaySubjectDemo(){
        ReplaySubject subject = ReplaySubject.create()
        subject.onNext(" replaySubject1")
        subject.onNext(" replaySubject2")
        subject.subscribe(new Consumer<String>() {
            @Override
            void accept(String s) throws Exception {
                println " ReplaySubject receive msg: $s"
            }
        },new Consumer<Throwable>() {
            @Override
            void accept(Throwable throwable) throws Exception {
                println " ReplaySubject receive error msg:  " + throwable.getMessage()
            }
        },new Action() {
            @Override
            void run() throws Exception {
                println " ReplaySubject receive complete signal ..."
            }
        })
        subject.onNext(" replaySubject3")
        subject.onNext(" replaySubject4")
        subject.onComplete()
        subject.onNext(" replaySubject5")
        subject.onNext(" replaySubject6")
    }

   // 只缓存订阅前最后发送的一条数据 演示限制缓存数据的数量
    void testReplaySubjectWithSizeDemo(){
        ReplaySubject<String> subject = ReplaySubject.createWithSize(1)
        subject.onNext(" replaySubject1")
        subject.onNext(" replaySubject2")
        subject.onNext(" replaySubject21")
        subject.subscribe(new Consumer<String>() {
            @Override
            void accept(String s) throws Exception {
                println " ReplaySubject receive msg: $s"
            }
        },new Consumer<Throwable>() {
            @Override
            void accept(Throwable throwable) throws Exception {
                println " ReplaySubject receive error msg:  " + throwable.getMessage()
            }
        },new Action() {
            @Override
            void run() throws Exception {
                println " ReplaySubject receive complete signal ..."
            }
        })
        subject.onNext(" replaySubject3")
        subject.onNext(" replaySubject4")
     //   subject.onComplete()
        subject.onNext(" replaySubject5")
        subject.onNext(" replaySubject6")

        subject.onComplete()
    }

    // 演示ReplaySubject.createWithTime 限制缓存的时间
    void testReplaySubjectWithTime(){
        ReplaySubject<String> subject = ReplaySubject.createWithTime(150, TimeUnit.MILLISECONDS,Schedulers.io())
        subject.onNext(" replaySubject1")
        Thread.sleep(100L)
        subject.onNext(" replaySubject2")
        Thread.sleep(100L)
        subject.onNext(" replaySubject21")
        Thread.sleep(100L)
        subject.subscribe(new Consumer<String>() {
            @Override
            void accept(String s) throws Exception {
                println " ReplaySubject receive msg: $s"
            }
        },new Consumer<Throwable>() {
            @Override
            void accept(Throwable throwable) throws Exception {
                println " ReplaySubject receive error msg:  " + throwable.getMessage()
            }
        },new Action() {
            @Override
            void run() throws Exception {
                println " ReplaySubject receive complete signal ..."
            }
        })
        subject.onNext(" replaySubject3")
        subject.onNext(" replaySubject4")
        //   subject.onComplete()
        subject.onNext(" replaySubject5")
        subject.onNext(" replaySubject6")

        subject.onComplete()
    }

}
