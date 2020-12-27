package com.achulkov.loftcoin.util;

import androidx.annotation.NonNull;

import com.achulkov.loftcoin.data.Transaction;
import com.achulkov.loftcoin.data.Wallet;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

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
        final DecimalFormat format = (DecimalFormat) NumberFormat.getCurrencyInstance();
        final DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
        symbols.setCurrencySymbol(transaction.coin().symbol());
        format.setDecimalFormatSymbols(symbols);
        return format.format(transaction.amount());
    }

}