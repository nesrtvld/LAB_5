package com.example.lab_5;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder> {

    private List<String> currencyList;

    public CurrencyAdapter(List<String> currencyList) {
        this.currencyList = currencyList;
    }

    @NonNull
    @Override
    public CurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_item, parent, false);
        return new CurrencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyViewHolder holder, int position) {
        String currencyData = currencyList.get(position);
        String[] parts = currencyData.split(" - ");
        holder.textViewCurrency.setText(parts[0]);
        holder.textViewRate.setText(parts[1]);
    }

    @Override
    public int getItemCount() {
        return currencyList.size();
    }

    static class CurrencyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCurrency;
        TextView textViewRate;

        public CurrencyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCurrency = itemView.findViewById(R.id.textViewCurrency);
            textViewRate = itemView.findViewById(R.id.textViewRate);
        }
    }
}
