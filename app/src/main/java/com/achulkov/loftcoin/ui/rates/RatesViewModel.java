package com.achulkov.loftcoin.ui.rates;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.achulkov.loftcoin.data.Coin;
import com.achulkov.loftcoin.data.CoinsRepo;
import com.achulkov.loftcoin.data.CurrencyRepo;
import com.achulkov.loftcoin.data.SortBy;
import com.achulkov.loftcoin.util.RxSchedulers;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class RatesViewModel extends ViewModel {

    private final Subject<Boolean> isRefreshing = BehaviorSubject.create();

    private final Subject<Class<?>> pullToRefresh = BehaviorSubject.createDefault(Void.TYPE);

    private final Subject<SortBy> sortBy = BehaviorSubject.createDefault(SortBy.PRICE);

    private final Subject<Throwable> error = PublishSubject.create();

    private final Subject<Class<?>> onRetry = PublishSubject.create();

    private final AtomicBoolean forceUpdate = new AtomicBoolean();

    private final Observable<List<Coin>> coins;

    private final RxSchedulers schedulers;

    private int sortingIndex = 1;

    // AppComponent(BaseComponent) -> MainComponent -> Fragment(BaseComponent) -> RatesComponent -> RatesViewModel()

    @Inject
    public RatesViewModel(CoinsRepo coinsRepo, CurrencyRepo currencyRepo, RxSchedulers schedulers) {
        this.schedulers = schedulers;
        //    t           f            f          f         f          t
        // (f|t) -> forceRefresh -> currency -> sortBy -> query -> listings
        // USD -> RUB -> USD -> USD -> USD -> EUR -> EUR
        this.coins = pullToRefresh
                .map((ptr) -> CoinsRepo.Query.builder())
                .switchMap((qb) -> currencyRepo.currency()
                        .map((c) -> qb.currency(c.code()))
                )
                .doOnNext((qb) -> forceUpdate.set(true))
                .doOnNext((qb) -> isRefreshing.onNext(true))
                .switchMap((qb) -> sortBy.map(qb::sortBy))
                .map((qb) -> qb.forceUpdate(forceUpdate.getAndSet(false)))
                .map(CoinsRepo.Query.Builder::build)
                .switchMap((q) -> coinsRepo.listings(q)
                                .doOnError(error::onNext)
                                .retryWhen((e) -> onRetry)
//                .onErrorReturnItem(Collections.emptyList())
                )
                .doOnEach((ntf) -> isRefreshing.onNext(false))
                .replay(1)
                .autoConnect();
    }

    @NonNull
    Observable<List<Coin>> coins() {
        return coins.observeOn(schedulers.main());
    }

    @NonNull
    Observable<Boolean> isRefreshing() {
        return isRefreshing.observeOn(schedulers.main());
    }

    @NonNull
    Observable<Throwable> onError() {
        return error.observeOn(schedulers.main());
    }

    final void refresh() {
        pullToRefresh.onNext(Void.TYPE);
    }

    void switchSortingOrder() {
        sortBy.onNext(SortBy.values()[sortingIndex++ % SortBy.values().length]);
    }

    void retry() {
        onRetry.onNext(Void.class);
    }
}
