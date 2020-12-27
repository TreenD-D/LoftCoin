package com.achulkov.loftcoin.util;

import androidx.annotation.NonNull;

import com.achulkov.loftcoin.data.Wallet;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BalanceFormatter implements Formatter<Wallet> {

    @Inject
    BalanceFormatter() {
    }

    @NonNull
    @Override
    public String format(@NonNull Wallet value) {
        return String.format(Locale.getDefault(),"%.2f %s",value.balance(),value.coin().symbol());
    }

}
