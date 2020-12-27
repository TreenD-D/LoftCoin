package com.achulkov.loftcoin.ui.rates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.achulkov.loftcoin.BaseComponent;
import com.achulkov.loftcoin.R;
import com.achulkov.loftcoin.databinding.FragmentRatesBinding;
import io.reactivex.disposables.CompositeDisposable;


import javax.inject.Inject;

public class RatesFragment extends Fragment {

    private final RatesComponent component;

    private final CompositeDisposable disposable = new CompositeDisposable();

    private FragmentRatesBinding binding;

    private RatesAdapter adapter;

    private RatesViewModel viewModel;

    @Inject
    public RatesFragment(BaseComponent baseComponent) {
        component = DaggerRatesComponent.builder()
                .baseComponent(baseComponent)
                .build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, component.viewModelFactory())
                .get(RatesViewModel.class);
        adapter = component.ratesAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rates, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        binding = FragmentRatesBinding.bind(view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setReverseLayout(true);
        binding.recycler.setLayoutManager(layoutManager);
        binding.recycler.setAdapter(adapter);
        binding.recycler.setHasFixedSize(true);
        binding.refresher.setOnRefreshListener(viewModel::refresh);
        disposable.add(viewModel.coins().subscribe(adapter::submitList));
        disposable.add(viewModel.onError().subscribe(e -> {
            Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", v -> viewModel.retry())
                    .show();
        }));
        disposable.add(viewModel.isRefreshing().subscribe(binding.refresher::setRefreshing));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.rates, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.currency_dialog == item.getItemId()) {
            NavHostFragment
                    .findNavController(this)
                    .navigate(R.id.currency_dialog);
            return true;
        } else if (R.id.sort_dialog == item.getItemId()) {
            viewModel.switchSortingOrder();
            binding.recycler.scrollToPosition(0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        binding.recycler.setAdapter(null);
        disposable.clear();
        super.onDestroyView();
    }

}
