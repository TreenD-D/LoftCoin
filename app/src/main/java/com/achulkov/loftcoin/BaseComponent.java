package com.achulkov.loftcoin;

import android.content.Context;

import com.achulkov.loftcoin.data.CoinsRepo;
import com.achulkov.loftcoin.data.CurrencyRepo;
import com.achulkov.loftcoin.util.ImageLoader;

public interface BaseComponent {
    Context context();
    CoinsRepo coinsRepo();
    CurrencyRepo currencyRepo();
    ImageLoader imageLoader();
}
