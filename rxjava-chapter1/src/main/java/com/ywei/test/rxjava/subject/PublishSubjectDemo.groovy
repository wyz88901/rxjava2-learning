package com.ywei.test.rxjava.subject

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

/**
 * @Description 演示 PublishSubject
 *                  Observer只接收PublishSubject被订阅之后发送的数据
 * @author yingzhou.wei
 * @date 2018/8/23
 *
 */
class PublishSubjectDemo extends GroovyTestCase{
    void testPublishSubject(){
        PublishSubject<String> subject = PublishSubject.create()
        subject.onNext(" publishSubject 1")
        subject.onNext(" publishSubject 2")
       // subject.onComplete()
        subject.subscribe(new Consumer<String>() {
            @Override
            void accept(String s) throws Exception {
                println " PublishSubject receive msg: $s"
            }
        },new Consumer<Throwable>() {
            @Override
            void accept(Throwable throwable) throws Exception {
                println "PublishSubject receive error msg: " + throwable.getMessage()
            }
        },new Action() {
            @Override
            void run() throws Exception {
                println " PublishSubject receive complete signal ..."
            }
        })


        subject.onNext(" publishSubject 3")
        subject.onNext(" publishSubject 4")

        subject.onComplete()
    }

    void testPublishSubjectThread(){
        PublishSubject<String> subject = PublishSubject.create()
        subject.onNext("publishSubject 1")
        subject.onNext("publishSubject 2")
        subject.subscribeOn(Schedulers.io()).subscribe(new Consumer<String>() {
            @Override
            void accept(String s) throws Exception {
                println " PublishSubject receive msg: $s"
            }
        },new Consumer<Throwable>() {
            @Override
            void accept(Throwable throwable) throws Exception {
                println "PublishSubject receive error msg: " + throwable.getMessage()
            }
        },new Action() {
            @Override
            void run() throws Exception {
                println " PublishSubject receive complete signal ..."
            }
        })
   //     Thread.sleep(3000L)
        subject.onNext("GOOD1")
        subject.onNext("GAD")
        subject.onComplete()

        Thread.sleep(3000L)

    }

    void testObservable(){
       Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("GOOD")
                emitter.onNext("GAD")
                emitter.onComplete()
            }
        }).subscribeOn(Schedulers.io()).subscribe(new Consumer<String>() {
           @Override
           void accept(String s) throws Exception {
               println " PublishSubject receive msg: $s"
           }
       },new Consumer<Throwable>() {
           @Override
           void accept(Throwable throwable) throws Exception {
               println "PublishSubject receive error msg: " + throwable.getMessage()
           }
       },new Action() {
           @Override
           void run() throws Exception {
               println " PublishSubject receive complete signal ..."
           }
       })

        Thread.sleep(3000L)
    }
}
