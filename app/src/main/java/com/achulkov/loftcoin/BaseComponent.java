package com.achulkov.loftcoin;

import android.content.Context;

import com.achulkov.loftcoin.data.CoinsRepo;
import com.achulkov.loftcoin.data.CurrencyRepo;
import com.achulkov.loftcoin.util.ImageLoader;
import com.achulkov.loftcoin.data.WalletsRepo;
import com.achulkov.loftcoin.util.Notifier;
import com.achulkov.loftcoin.util.RxSchedulers;

public interface BaseComponent {
    Context context();
    CoinsRepo coinsRepo();
    CurrencyRepo currencyRepo();
    ImageLoader imageLoader();
    WalletsRepo walletsRepo();
    RxSchedulers schedulers();
    Notifier notifier();

}
