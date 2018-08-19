package com.ywei.test.rxjava

import io.reactivex.Notification
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @Description  do操作符
 * @author yingzhou.wei
 * @date 2018/8/18
 *
 */
class DoTest extends GroovyTestCase{


    def void testDo(){
        Observable.just("hello world")
             .doOnNext(new Consumer<String>() {
                    @Override
                    void accept(String s) throws Exception {
                        println "doOnext $s"
                    }
            }).doAfterNext(new Consumer<String>() {
                @Override
                void accept(String s) throws Exception {
                    println "doAfterNext $s"
                }
        }).doOnComplete(new Action() {
            @Override
            void run() throws Exception {
                println "doOnComplete  "
        }
        }).doOnSubscribe(new Consumer<Disposable>() { // 订阅之后回调
            @Override
            void accept(Disposable disposable) throws Exception {
                println "doOnSubscribe  "
            }
        }).doAfterTerminate(new Action() {
            @Override
            void run() throws Exception {
                println "doAfterTerminate  "
            }
        }).doFinally(new Action() {
            @Override
            void run() throws Exception {
                println "doFinally  "
            }
        }).doOnEach(new Observer<String>() { //Observable每发射一个数据就会触发这个回调
            @Override
            void onSubscribe(@NonNull Disposable d) {
                println "doOnEach --> onSubscribe  "
            }

            @Override
            void onNext(@NonNull String s) {
                println "doOnEach --> onNext  "
            }

            @Override
            void onError(@NonNull Throwable e) {
                println "doOnEach --> onError  "
            }

            @Override
            void onComplete() {
                println "doOnEach --> onComplete  "
            }
        }).doOnLifecycle(new Consumer<Disposable>() {
            @Override
            void accept(Disposable disposable) throws Exception {
                println "doOnLifecycle:   " + disposable.isDisposed()
            }
        },new Action() {
            @Override
            void run() throws Exception {
                println "doOnLifecycl run:  "
            }
        }).subscribe(new Consumer<String>() {
            @Override
            void accept(String s) throws Exception {
                println "receive msg: $s"
            }
        })
    }

}
