package southwest.monsoon.module.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Flow;

@Slf4j
public abstract class SimpleSubscriber<T> implements Flow.Subscriber<T> {
    private Flow.Subscription subscription;

    public SimpleSubscriber(Flow.Publisher<T> publisher) {
        publisher.subscribe(this);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        log.info("Subscription created. {}", subscription);
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(T item) {
        onChange(item);
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("SimpleSubscriber Throwable", throwable);
    }

    @Override
    public void onComplete() {
        log.info("Subscription completed. {}", subscription);
    }

    public abstract void onChange(T item);
}
