package com.achulkov.loftcoin.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import io.reactivex.Observable;

import java.util.List;

public interface CurrencyRepo {

    @NonNull
    LiveData<List<Currency>> availableCurrencies();

    @NonNull
    Observable<Currency> currency();

    void updateCurrency(@NonNull Currency currency);

}
