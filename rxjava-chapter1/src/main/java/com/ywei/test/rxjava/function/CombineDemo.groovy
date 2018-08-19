package com.ywei.test.rxjava.function

import com.ywei.test.rxjava.function.domain.User
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.ObservableSource
import io.reactivex.annotations.NonNull
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function

import java.util.concurrent.TimeUnit

/**
 * @Description 结合操作Demo
 * @author yingzhou.wei
 * @date 2018/8/4
 *
 */
class CombineDemo extends GroovyTestCase{

    void testJoin(){
        Observable.just(1,3,5,7,9,11)
                .join( Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(7),
                new Function<Integer, ObservableSource<Object>>() {
                    @Override
                    ObservableSource<Object> apply(@NonNull Integer integer) throws Exception {
                        return Observable.just(integer)
                    }
                }, new Function<Long, ObservableSource<Object>>() {
            @Override
            ObservableSource<Object> apply(@NonNull Long aLong) throws Exception {
                return Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                        emitter.onNext("hello")
                        emitter.onNext("gaga")
                        emitter.onComplete()
                    }
                })
            }
        },new BiFunction<Integer, Long, Object>() {
            @Override
            Object apply(@NonNull Integer cnt, @NonNull Long aLong) throws Exception {
                return null
            }
        })
    }

    /**
     *  ZIP: 数据合并操作
     */
    void testZip(){
        Observable.zip(getStringObservable(), getIntegerObservable(), new BiFunction<String, Integer, User>() {
            @Override
            User apply(@NonNull String s, @NonNull Integer integer) throws Exception {
                def user = new User()
                user.remark = s
                user.age = integer
                if(integer%2==0)
                    user.sex = '男'
                else
                    user.sex = '女'
                user.name="test" + integer
                user.mobile = '139678789'+ integer
                user.id = UUID.randomUUID().toString().replaceAll("-","")
                return user
            }
        }).subscribe(new Consumer<User>() {
            @Override
            void accept(User s) throws Exception {
                println "zip accept: " + s;
            }
        });
    }

    //创建 String 发射器
    private Observable<String> getStringObservable() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("A");
                e.onNext("B");
                e.onNext("C");
            }
        });
    }

    //创建 String 发射器
    private Observable<Integer> getIntegerObservable() {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onNext(4);
                e.onNext(5);
            }
        });
    }
}
