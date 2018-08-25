package com.ywei.test.rxjava.creation

import com.google.common.collect.ImmutableList
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

/**
 * @Description  演示创建操作符 From 系列
 * @author yingzhou.wei
 * @date 2018/8/5
 *
 */
class FromDemo extends GroovyTestCase {

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
        Observable.fromCallable(new Callable<List>() {
            @Override
            List call() throws Exception {
                return ImmutableList.of( 0, 1, 2, 3, 4, 5).toArray()
            }
        }).subscribe(new Consumer<List>() {
            @Override
            void accept(List list) throws Exception {
                println "FromCallable receive msg:"
                list.each {cnt -> println cnt}

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


    void testFromFutureCallable(){
        ExecutorService executorService = Executors.newFixedThreadPool(10)
        Future<String> future = executorService.submit(new  MyCallable())

        //   Thread.sleep(2000L)
        Observable.fromFuture(future)
        //  Observable.fromFuture(future,3,TimeUnit.SECONDS)
                .subscribe(new Consumer<String>() {
            @Override
            void accept(String s) throws Exception {
                println "接收future msg. $s"
            }
        },new Consumer<Throwable>() {
            @Override
            void accept(Throwable throwable) throws Exception {
                println "接收future error msg:"+throwable.getMessage()
            }
        })
    }


}
