package com.ywei.test.rxjava.observable

import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import io.reactivex.CompletableObserver
import io.reactivex.CompletableOnSubscribe
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.Observable

import java.util.concurrent.TimeUnit

/**
 * @Description 演示 Completable
 *
 * Completable 从来不发射数据，只处理 onComplete 和 onError 事件。
 *
 * @author yingzhou.wei
 * @date 2018/8/5
 *
 */
class CompletableDemo extends GroovyTestCase{

    def void testCompletable(){
        Completable.create(new CompletableOnSubscribe() {
            @Override
            void subscribe(@NonNull CompletableEmitter emitter) throws Exception {
                try {
                    println " prepare complete data "
                    emitter.onComplete();  //
                    //  throw new Exception("test Completable");
                }catch (Exception e){
                    emitter.onError(e)
                }
            }
        }).subscribe(new CompletableObserver() {
            @Override
            void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            void onComplete() {
                println "Completable complete"
            }

            @Override
            void onError(@NonNull Throwable e) {
                println "Completable error. "+ e.getMessage()
            }
        })
    }

    def void testCompletableConsumer(){
        Completable.create(new CompletableOnSubscribe() {
            @Override
            void subscribe(@NonNull CompletableEmitter emitter) throws Exception {
                try {
                    emitter.onComplete();  // onComplete(). onError() 同时只能发送一个,不可同时发送;否则会报错
                   // emitter.onComplete();
                   // throw new Exception("test Completable");
                }catch (Exception e){
                    emitter.onError(e)
                }
            }
        }).subscribe(new Action() {
            @Override
            void run() throws Exception {
                println "Completable complete"
            }
        },new Consumer<Throwable>() {
            @Override
            void accept(Throwable e) throws Exception {
                println "Completable error. "+ e.getMessage()
            }
        })
    }

    void testCompletableAction(){
        Completable.fromAction(new Action() {
            @Override
            void run() throws Exception {
                println "Completable complete"
            }
        }).subscribe()
    }

    // onComplete()执行完成后,会执行 andThen()
    void testCompletableThen() {
        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter emitter) throws Exception {

                try {
                    TimeUnit.SECONDS.sleep(3);
                    emitter.onComplete();
                } catch (InterruptedException e) {
                    emitter.onError(e);
                }
            }
        }).andThen(Observable.just(1,3,5,6))
                .subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                System.out.println(integer);
            }
        });

    }
}
