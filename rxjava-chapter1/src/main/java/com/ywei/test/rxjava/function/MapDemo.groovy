package com.ywei.test.rxjava.function

import com.google.common.collect.Lists
import com.google.common.collect.Maps
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.observables.GroupedObservable

import java.util.concurrent.TimeUnit

/**
 * @Description  变换操作Demo
 * @author yingzhou.wei
 * @date 2018/8/4
 *
 */
class MapDemo extends GroovyTestCase{

    /**
     * Map: 对Observable发射的每一项数据应用一个函数，执行变换操作
     *
     */
    void testMap(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1)
                emitter.onNext(2)
                emitter.onNext(3)
                emitter.onComplete()
            }
        }).map(new Function<Integer, String>() {
            @Override
            String apply(@NonNull Integer integer) throws Exception {
                return "This is result " + integer
            }
        }).subscribe(new Observer<String>() {

            @Override
            void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            void onNext(@NonNull String s) {
                println "receive message: " + s
            }

            @Override
            void onError(@NonNull Throwable e) {

            }

            @Override
            void onComplete() {
                println "receive  complete signal "
            }
        })
    }

    /**
     * FlatMap: 将一个发射数据的Observable变换为多个Observables，
     *          然后将它们发射的数据合并后放进一个单独的Observable, 不能保证事件的顺序
     */
    void testFlatMap(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                def range = 0..4
                range.each {it -> emitter.onNext(it)}
                emitter.onComplete()
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            ObservableSource<String> apply(@NonNull Integer integer) throws Exception {
                List<String> list = new ArrayList<>();
                [0,1,2,3].each {it ->
                    list.add( "我是事件 " + integer + "拆分后的子事件" + it)
                }
                //随机生成一个时间
                int delayTime = (int) (1 + Math.random() * 10);
                return Observable.fromIterable(list).delay(delayTime, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            void accept(String s) throws Exception {
                println "accept: " + s ;
            }
        } )
        Thread.sleep(5000)
    }

    /**
     * ConcatMap: ConcatMap 与 FlatMap 的唯一区别就是 concatMap 保证了顺序
     */
    void testConcatMap(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                def range = 0..3
                range.each {it -> emitter.onNext(it)}
                emitter.onComplete()
            }
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            ObservableSource<String> apply(@NonNull Integer cnt) throws Exception {
                def list = Lists.newArrayList()

                [0,1,2,3].each {it ->

                    list.add( "我是事件 " + cnt + "拆分后的子事件" + it)
                }
                //随机生成一个时间
                int delayTime = (int) (1 + Math.random() * 10);
                return Observable.fromIterable(list).delay(delayTime, TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            void accept(String result) throws Exception {
                println "concatmap receive msg: " + result
            }
        })
        Thread.sleep(5000)
    }

    void testReduce(){
        Observable.just(1,2,3,4,56,7,9)
        .reduce(new BiFunction<Integer, Integer, String>() {
            @Override
            String apply(@NonNull Integer integer, @NonNull Integer integer2) throws Exception {
                return null
            }
        })
    }

    /**
     *  Buffer : 期收集Observable的数据放进一个数据包裹，然后发射这些数据包裹，而不是一次发射一个值。
     */
    def void testBuffer(){
     /*   Observable.just(11,3,2,4,5,6,7,10,34)
                .buffer(3,2).subscribe(new Consumer<List<Integer>>() {
            @Override
            void accept(List<Integer> integers) throws Exception {

                def strBuffer= "test";
                integers.each {cnt ->
                    strBuffer = strBuffer + cnt

                    println cnt + "----"
                }
                println "buffer value: " + strBuffer
            }
        })*/
        Observable.just(11,3,2,4,5,6,7,10,34)
                .buffer(3,2).subscribe(new Observer<List<Integer>>() {
            Map map = Maps.newHashMap()
            int cnt = 1;
            @Override
            void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            void onNext(@NonNull List<Integer> integers) {
                map.put(cnt,integers)
                cnt ++
            }

            @Override
            void onError(@NonNull Throwable e) {

            }

            @Override
            void onComplete() {
                println "buffer completed . map: $map"
            }
        }
        )
    }

    void testBufferCount(){
        Observable.just(11,3,2,4,5,6,7,10,34)
                .buffer(3).subscribe(new Consumer<List<Integer>>() {
            @Override
            void accept(List<Integer> integers) throws Exception {
                integers.each {cnt ->
                    println "buffer count , cnt = " + cnt
                }
                println "buffer count end.: "
            }
        })
    }

    /**
     * Scan: 连续地对数据序列的每一项应用一个函数，然后连续发射结果
     *      对原始Observable发射的第一项数据应用一个函数，然后将那个函数的结果作为自己的第一项数据发射。
     *     它将函数的结果同第二项数据一起填充给这个函数来产生它自己的第二项数据。它持续进行这个过程来产生剩余的数据序列
     */
    void testScan(){
        Observable.just(1,3,4,5,6)
                .scan(new BiFunction<Integer, Integer, Integer>() {
            @Override
            Integer apply(@NonNull Integer cnt, @NonNull Integer count) throws Exception {
                return cnt + count;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            void accept(Integer integer) throws Exception {
                println "scan " + integer
            }
        })

        /* Observable.just(1,3,4,5,6).scan((cnt,count )->{
              cnt + count
         }).subscribe(cnt -> println "scan receive msg: " + cnt)*/
    }

    /**
     * GroupBy: 将原始Observable分拆为一些Observables集合，它们中的每一个发射原始Observable数据序列的一个子序列。
     *        哪个数据项由哪一个Observable发射是由一个函数判定的，这个函数给每一项指定一个Key，
     *        Key相同的数据会被同一个Observable发射
     *
     */
    void testGroupByFunc2(){
        Observable.range(0, 10).groupBy(new Function<Integer, String>() {
            @Override
            String apply(@NonNull Integer cnt) throws Exception {
                  cnt %2
            }
        }, new Function<Integer, String>() {

            @Override
            String apply(@NonNull Integer integer) throws Exception {
                return  "($integer)"
            }
        }).subscribe(new Consumer<GroupedObservable<String, String>>() {
            @Override
            void accept(GroupedObservable<String, String> group) throws Exception {
                println group.buffer(10).subscribe(new Consumer<List<String>>() {
                    @Override
                    void accept(List<String> strings) throws Exception {
                        println strings

                    }
                })
               /* group.subscribe(new Consumer<String>() {
                    @Override
                    void accept(String value) throws Exception {
                        println "key: $group.key --> value: $value"
                    }
                })*/
            }
        })
    /*    Observable.range(0, 10)
                .groupBy(integer -> integer % 2, integer -> "(" + integer + ")")
                .subscribe(group -> {
            group.subscribe(integer -> System.out.println(
                    "key:" + group.getKey() + "==>value:" + integer));
        });*/
    }

    void testGroupByFunc1(){
        Observable.range(0, 10).groupBy(new Function<Integer, String>() {
            @Override
            String apply(@NonNull Integer cnt) throws Exception {
                return cnt %2
            }
        }).subscribe(new Consumer<GroupedObservable<String, String>>() {
            @Override
            void accept(GroupedObservable<String, String> group) throws Exception {
                println group.buffer(10).subscribe(new Consumer<List<String>>() {
                    @Override
                    void accept(List<String> strings) throws Exception {
                        println strings

                    }
                })
                /* group.subscribe(new Consumer<String>() {
                     @Override
                     void accept(String value) throws Exception {
                         println "key: $group.key --> value: $value"
                     }
                 })*/
            }
        })
    }

    /**
     * Window: 将来自原始Observable的数据分解为一个Observable窗口，发射这些窗口，而不是每次发射一项数据
     */
    void testWindow(){
        Observable.just(1,2,3,4,5,6,7,8)
                .window(3).subscribe(new Consumer<Observable<Integer>>() {
            @Override
            void accept(Observable<Integer> observable) throws Exception {
                observable.subscribe( new Observer<Integer>() {
                    List list = Lists.newArrayList()
                    @Override
                    void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    void onNext(@NonNull Integer cnt) {
                        list.add(cnt)
                    }

                    @Override
                    void onError(@NonNull Throwable e) {

                    }

                    @Override
                    void onComplete() {
                        println "this emmiter has emite $list.size param, the value is : $list"
                    }
                })
            }
        })
    }

    void testWindowSkip(){
        Observable.just(1,2,3,4,5,6,7,8)
                .window(3,4).subscribe(new Consumer<Observable<Integer>>() {
            @Override
            void accept(Observable<Integer> observable) throws Exception {
                observable.subscribe( new Observer<Integer>() {
                    List list = Lists.newArrayList()
                    @Override
                    void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    void onNext(@NonNull Integer cnt) {
                        list.add(cnt)
                    }

                    @Override
                    void onError(@NonNull Throwable e) {

                    }

                    @Override
                    void onComplete() {
                        println "this emmiter has emite $list.size param, the value is : $list"
                    }
                })
            }
        })
    }



}
