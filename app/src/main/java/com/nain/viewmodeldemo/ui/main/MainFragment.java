package com.nain.viewmodeldemo.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.nain.viewmodeldemo.R;
import com.nain.viewmodeldemo.databinding.FragmentMainBinding;

import java.util.Locale;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private FragmentMainBinding binding;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final Observer<Float> resultObserver = new Observer<Float>(){

            @Override

            public void onChanged(@Nullable final Float result){

                binding.resultText.setText(String.format(Locale.ENGLISH, "%.2f", result));

            }

        };

        mViewModel.getResult().observe(getViewLifecycleOwner(), resultObserver);

        binding.convertButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                if (!binding.dollarText.getText().toString().equals("")){

                    mViewModel.setAmount(String.format(Locale.ENGLISH, "%s", binding.dollarText.getText()));


                } else {

                    binding.resultText.setText("No Value");

                }


            }

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}