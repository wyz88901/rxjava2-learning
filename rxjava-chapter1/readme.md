知识点汇总:

RxJava使用步骤:
 1. 创建Observable
 2. 创建Observer
 3. 使用subscribe()订阅


Hot Observable
 1. 无论有没有观察者进行订阅，事件始终都会发生
 2. Hot Observable与订阅者们的关系是一对多的关系


Cold Observable
  1. 是只有观察者订阅了，才开始执行发射数据流的代码
  2. Cold Observable与订阅者的关系是一对一的关系

Cold Observable转换成Hot Observable
  使用Observable的publish(),生产ConnectableObservable
 
Hot Observable转换成Cold Observable
 使用ConnectableObservable的refCount()


RxJava 2.0五种观察者模式:
    Observable 和 Observer
    Flowable 和 Subscriber
    Single 和SingleObserver
    Completable和CompletableObserver
    Maybe 和MaybeObserver

Observable:	能够发射0或n个数据，并以成功或错误事件终止。
Flowable: 能够发射0或n个数据，并以成功或错误事件终止。支持Backpressure，可以控制数据源发射的速度。
Single:	只发射单个数据或错误事件。
Completable: 它从来不发射数据，只处理 onComplete 和 onError 事件。
Maybe: 能够发射0或者1个数据，要么成功，要么失败。有点类似于Optional


Subject定义: 
 1. Subject是一个代理，它既是Observer，也是Observable。
 2. 作为一个Observer一个Observer，它可以订阅一个或多个Observable;
 3. 它作为一个Observer一个Observable，它又可以被其他的Observer订阅。
 4. 它可以传递/转发作为Observer收到的值，也可以主动发射值
 5. Subject不是线程安全的,在多线程情况下,需要使用Subject.create().toSerialized()
Subject分类:
 1. AysyncSubject: 
     1) Observer会接受AsyncSubject的onComplete()之前的最后一个数据
     2) AsyncSubject的onComplete()必须要调用才会开始发射数据，否则观察者将不接受任务数据
 2. BehaviorSubject:
     1) Observer会先接受到BehaviorSubject被订阅之前的最后一个数据,再接收订阅之后发射过来的数据
     2) 如果BehaviorSubject被订阅之前没有发射任何数据,则会发射一个默认数据
 3. ReplaySubject:
     1) ReplaySubject会发射所有来自原始Observable的数据给观察者,无论它们何时发射
     2) 可以通过createWithSize()来限制缓存数据的数量
     3) 可以通过createWithTime()来限制缓存的时间
 4. PublishSubject: 
     1) Observer只接收PublishSubject被订阅之后发送的数据