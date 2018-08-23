package com.ywei.test.rxjava.observable

import io.reactivex.Maybe
import io.reactivex.MaybeEmitter
import io.reactivex.MaybeObserver
import io.reactivex.MaybeOnSubscribe
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * @Description 演示 Maybe
 *   Maybe: 能够发射0或者1个数据，要么成功，要么失败。有点类似于Optional
 * @author yingzhou.wei
 * @date 2018/8/5
 *
 */
class MaybeDemo extends GroovyTestCase{

    void testMaybe(){
        Maybe.create(new MaybeOnSubscribe<String>() {
            @Override
            void subscribe(@NonNull MaybeEmitter<String> emitter) throws Exception {
              //  emitter.onComplete()
                emitter.onSuccess("test Maybe ")  //只会发一次数据，即使后面跟onComplete(),也不会发射
                //  emitter.onComplete()
            }
        }).subscribe(new MaybeObserver<String>() {
            @Override
            void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            void onSuccess(@NonNull String s) {
                println "Maybe receive msg: $s"
            }

            @Override
            void onError(@NonNull Throwable e) {
                println "Maybe receive error :" + e.getMessage()
            }

            @Override
            void onComplete() {
                println "Maybe receive complete singal"
            }
        })
    }

    void testMaybeConsumer(){
        Maybe.create(new MaybeOnSubscribe<String>() {
            @Override
            void subscribe(@NonNull MaybeEmitter<String> emitter) throws Exception {
                //  emitter.onComplete()
                emitter.onSuccess("test Maybe ")  //只会发一次数据，即使后面跟onComplete(),也不会发射
                //  emitter.onComplete()
            }
        }).subscribe(new Consumer<String>() {
            @Override
            void accept(String s) throws Exception {
                println "Maybe receive msg: $s"
            }
        },new Consumer<Throwable>() {
            @Override
            void accept(Throwable throwable) throws Exception {
                println "Maybe receive error :" + e.getMessage()
            }
        }, new Action() {
            @Override
            void run() throws Exception {
                println "Maybe receive complete singal"
            }
        })
    }

}
