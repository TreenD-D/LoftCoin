package com.achulkov.loftcoin.util;

import androidx.annotation.NonNull;

import com.achulkov.loftcoin.data.Transaction;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TransactionFormatter implements Formatter<Transaction> {

    @Inject
    TransactionFormatter() {
    }

    @NonNull
    @Override
    public String format(@NonNull Transaction transaction) {
        return String.format(Locale.getDefault(),"%.2f %s",transaction.amount(),transaction.coin().symbol());
    }


}