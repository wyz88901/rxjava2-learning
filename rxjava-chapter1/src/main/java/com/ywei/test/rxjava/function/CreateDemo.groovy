package com.ywei.test.rxjava.function

import com.google.common.collect.ImmutableList
import io.reactivex.*
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

/**
 * @Description 显示创建操作,变换操作
 *    使用Observer或Comsumer作为观察者,演示各种类型的创建操作
 *             Observer 包含onNext(), onError(),onComplete 三个方法
 *             onError(), onComplete()不能同时使用,
 *
 *
 * @author yingzhou.wei
 * @date 2018/7/28
 *
 */
class CreateDemo extends GroovyTestCase {


    /**
     * Create
     * 使用一个函数从头开始创建一个Observable
     * 演示了Create创建Observable, 解除订阅、onNext(), onError(), onComplete() 四中情形
     * step1：初始化 Observable
     * step2：初始化 Observer
     * step3：建立订阅关系
     */
    void testCreate() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
               try {
                   def range = 1..10
                   range.each { cnt ->
                       println "生产者 发送数据. value : " + cnt
                       emitter.onNext(cnt)
                       if(cnt == 7 ){
                         //  throw new Exception("测试发送异常") // error , onComplete不能同时使用
                           emitter.onComplete()
                           println  "生产者 发送数据结束"
                       }
                   }
               }catch (e){
                   emitter.onError(e)
                   println "生产者 发送数据异常. error : " + e.getMessage()
               }
            }
        }).subscribe(new Observer<Integer>() {
            Disposable disposable
            @Override
            void onSubscribe(@NonNull Disposable d) {
                disposable =d
            }

            @Override
            void onNext(@NonNull Integer value) { // 正常接收
                println "消费者收到生产者的信息. value:"+ value
                if(value == 5){      // 解除订阅,不再接收后面的信息
                    disposable.dispose()
                }
            }
            @Override
            void onError(@NonNull Throwable e) {  //异常处理
                Thread.sleep(1000)
                println "消费者收到生产者发送的异常. msg:" + e.getMessage()
            }

            @Override
            void onComplete() {  //结束处理
                println "消费者收到生产者发送的结束信息。"
            }
        })
    }

    /** Just
     * 创建一个发射指定值的Observable, Just将单个数据转换为发射那个数据的Observable。
     * Just只是简单的原样发射，将数组或Iterable当做单个数据
     */
    void testJust(){
        Observable.just(1, 2, 3)
                .subscribe(new Observer<Integer>() {
            @Override
            void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(Integer value) {
                System.out.println("Next: " + value);
            }

            @Override
            public void onError(Throwable error) {
                System.err.println("Error: " + error.getMessage());
            }

            @Override
            void onComplete() {
                println "just complete"
            }
        });
    }

    /**
     * Defer: 直到有观察者订阅时才创建Observable，并且为每个观察者创建一个新的Observable
     */
    void testDefer(){
        Observable.defer(new Callable<ObservableSource<? extends String>>(){
            @Override
            ObservableSource<? extends String> call() throws Exception {
                return Observable.just("hello","yyy","okkk")
            }
        }).subscribe(new Consumer<String>() {
            @Override
            void accept(String so) throws Exception {
                println "defer receive msg:" + so
            }
        })

    }


    /**
     *
     * ==========================================================================
     *                   from系列
     * ==========================================================================
     *  FromIterable : 遍历集合,将其它种类的对象和数据类型转换为Observable,
     *
     */
    void testFromIterable(){
        def items = [ 0,1, 2, 3, 4, 5 ] // 等价于下面list
      //  List items = ImmutableList.of( 0, 1, 2, 3, 4, 5);
        Observable.fromIterable(items).subscribe(new Observer<Integer>() {
            @Override
            void onSubscribe(@NonNull Disposable d) {

            }
            @Override
             void onNext(Integer value) {
                System.out.println("Next: " + value);
            }
            @Override
            void onError(Throwable error) {
                System.err.println("Error: " + error.getMessage());
            }
            @Override
            void onComplete() {
                println "fromIterable complete"
            }
        });
    }

    /**
     *  FromArray: 遍历数组
     */
    void testFromArray(){
        List<Integer> list =  ImmutableList.of( 0, 1, 2, 3, 4, 5);
        Observable.fromArray(list.toArray()).subscribe(new Consumer<Integer>() {
            @Override
            void accept(Integer cnt) throws Exception {
                println "fromArray receive msg: " + cnt
            }
        })
    }

    /**
     * FromCallable: Calls a Callable and emits its resulting single value or signals its exception
     */
    void testFromCallable(){
        Observable.fromCallable(new Callable<Object>() {
            @Override
            Object call() throws Exception {
                return ImmutableList.of( 0, 1, 2, 3, 4, 5).toArray()
            }
        }).subscribe(new Consumer<Object>() {
            @Override
            void accept(Object cnt) throws Exception {
                println "FromCallable receive msg: " + cnt
            }
        })
        /*Observable.fromCallable(() -> ImmutableList.of( 0, 1, 2, 3, 4, 5)).subscribe(cnt -> println cnt)

        Observable.fromCallable(() -> Arrays.asList("hello", "gaga"))
                .subscribe(strings -> System.out.println(strings))*/
    }

    /**
     *  FromFuture:
     */
     void testFromFuture(){
         Observable.fromFuture(Observable.just("hello").toFuture())
         .subscribe(new Consumer<String>() {
             @Override
             void accept(String s) throws Exception {
                 println "FromFuture receive msg: " + s
             }
         })
     }
    /**
     *
     * ==========================================================================
     *                   from系列 结束
     * ==========================================================================
     */



    /**
     * Interval : 返回一个Observable，它按固定的时间间隔发射一个无限递增的整数序列。
     */
    void testInterval(){
        Observable.interval(3L,TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
            @Override
            void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(Long value) {
                System.out.println("Next: " + value);
            }

            @Override
            public void onError(Throwable error) {
                System.err.println("Error: " + error.getMessage());
            }

            @Override
            void onComplete() {
                println "just complete"
            }
        });
        Thread.sleep(10000L)
    }

    /**
     * Repeat: 重复的发射数据
     * repeat( ) //无限重复
     * repeat( int time ) //设定重复的次数
     *
     */
    void testRepeat(){
        Observable
                .just(1, 2)
                .repeat( 3 ) //重复3次
                .subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                println "accept: " + integer ;
            }
        });
    }

    /**
     * Range: 发射一个范围内的有序整数序列，你可以指定范围的起始和长度。
     * 参数:
     * start:起始值
     * count:一个是范 围的数据的数目。0不发送 ，负数 异常
     */
    void testRange(){
        Observable.range(1,10).subscribe(new Observer<Integer>() {
            Disposable disposable;
            @Override
            void onSubscribe(@NonNull Disposable d) {
                this.disposable = d
            }

            @Override
            void onNext(@NonNull Integer value) {
                println "range receive message value: "+ value
                /* if(value == 8)
                     disposable.dispose()*/
            }

            @Override
            void onError(@NonNull Throwable e) {

            }

            @Override
            void onComplete() {
                println "receive final signal"
            }
        })
    }

    /**
     * delay : 延迟发射数据
     */
    void testDelay(){
          Observable
                .just(1, 2, 3)
                .delay(3, TimeUnit.SECONDS)  //延迟3秒钟，然后在发射数据
                .subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                println(  "accept: " + integer);
            }
        })
        Thread.sleep(5000)
    }

    /**
     *  Timer: 它在延迟一段给定的时间后发射一个简单的数字0
     */
    void testTimer(){
        Observable.timer(1000, TimeUnit.MILLISECONDS)
              .subscribe(new Consumer<Long>() {
            @Override
            void accept(Long aLong) throws Exception {
                println aLong
            }
        })
        Thread.sleep(3000)
    }


}
