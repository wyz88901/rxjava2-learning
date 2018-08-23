package com.ywei.test.rxjava.subject

import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.subjects.AsyncSubject

/**
 * @Description 演示 AsyncSubject
 *                  Observer会接受AsyncSubject的onComplete()之前的最后一个数据
 *                  AsyncSubject的onComplete()必须要调用才会开始发射数据，否则观察者将不接受任务数据
 * @author yingzhou.wei
 * @date 2018/8/5
 *
 */
class AsyncSubjectDemo extends GroovyTestCase{
    void testAsyncSubject(){
        AsyncSubject<String> subject = AsyncSubject.create();
        subject.onNext("asyncSubject1")
        subject.onNext("asyncSubject2")
       // subject.onComplete()

        subject.subscribe(new Consumer<String>() {
            @Override
            void accept(String s) throws Exception {
                println "AsyncSubject receive msg: $s"
            }
        },new Consumer<Throwable>() {
            @Override
            void accept(Throwable throwable) throws Exception {
                println "AsyncSubject receive error msg: " + throwable.getMessage()
            }
        },new Action() {
            @Override
            void run() throws Exception {
                println "AsyncSubject receive complete singal"
            }
        })
        subject.onNext("asyncSubject3")
        subject.onNext("asyncSubject4")
        subject.onComplete()
    }

}
