package com.ywei.test.rxjava.observable

import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleObserver
import io.reactivex.SingleOnSubscribe
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

/**
 * @Description 演示 Single
 *              Single 使用onSuccess()发射数据，而且只发射单个数据或错误事件，即使后续再发射数据也不会处理
 * @author yingzhou.wei
 * @date 2018/8/5
 *
 */

class SingleDemo  extends GroovyTestCase{
    def void  testSingle(){
        Single.create(new SingleOnSubscribe<String>() {
            @Override
            void subscribe(@NonNull SingleEmitter<String> emitter) throws Exception {
                try {
                    emitter.onSuccess("test single...")
                    //  emitter.onSuccess("test single 2...")
                    //  throw new Exception("test single error")
                }catch (Exception e){
                    emitter.onError(e)
                }

                //  emitter.onSuccess("test single2...")
            }
        }).subscribe(new SingleObserver<String>() {
            @Override
            void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            void onSuccess(@NonNull String s) {
                println "single receive success msg: $s"
            }

            @Override
            void onError(@NonNull Throwable e) {
                println "single receive error msg: " + e.getMessage()
            }
        })
    }

    def void  testSingleConsumer(){
        Single.create(new SingleOnSubscribe<String>() {
            @Override
            void subscribe(@NonNull SingleEmitter<String> emitter) throws Exception {
                emitter.onSuccess("test single...")
            }
        }).subscribe(new Consumer<String>() {
            @Override
            void accept(String s) throws Exception {
                println "single receive success msg: $s"
            }
        },new Consumer<Throwable>() {
            @Override
            void accept(Throwable throwable) throws Exception {
                println "single receive error msg: " + e.getMessage()
            }
        })
    }
}
