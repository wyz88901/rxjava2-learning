package com.ywei.test.rxjava;


import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author yingzhou.wei
 * @Description 第一个rxjava 程序
 * @date 2018/8/18
 */
public class HelloWorld {

    /**
     * RXJava 使用三部曲:
     * 1. 创建Observable
     * 2. 创建Observer
     * 3. 使用subscribe()订阅
     *
     */

    public static void main(String[] args){
        observable();
        hello();
        lambdaHello();;

    }

    // 普通的rxjava 表达式
    public static void observable(){
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("hello world");
                emitter.onComplete();
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println(" subscibe " );
            }
            @Override
            public void onNext(String s) {
                System.out.println("onNext: " + s);
            }
            @Override
            public void onError(Throwable e) {
                System.out.println("onError: " + e.getMessage());
            }
            @Override
            public void onComplete() {
                System.out.println("onComplete " );
            }
        });
    }

    // 简单一点的rxjava
    public static void hello(){
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("hello world");
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println(s);
            }
        });
    }

    // 再简单一点的rxjava
    public static void justHello(){
        Observable.just("just hello world").subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println(s);
            }
        });
    }


    // 用Lambda表达式实现rxjava
    public static void lambdaHello(){
        Observable.create(emitter -> emitter.onNext("lambda, hello world"))
            .subscribe(System.out::println);
    }




}
