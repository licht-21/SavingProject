package com.example.savingproject.UI.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.savingproject.MODEL.Deposit;
import com.example.savingproject.databinding.ItemDepositBinding;
import com.example.savingproject.util.DateFormatUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DepositAdapter extends RecyclerView.Adapter<DepositAdapter.ViewHolder> {

    private List<Deposit> deposits = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDepositBinding binding = ItemDepositBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Deposit deposit = deposits.get(position);
        holder.binding.depositAmountText.setText(String.format(Locale.getDefault(),
                "+₱%,.2f", deposit.getAmount()));
        holder.binding.depositDateText.setText(DateFormatUtil.formatDisplay(deposit.getCreatedAt()));

        String note = deposit.getNote();
        if (note != null && !note.isEmpty()) {
            holder.binding.depositNoteText.setVisibility(View.VISIBLE);
            holder.binding.depositNoteText.setText(note);
        } else {
            holder.binding.depositNoteText.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return deposits.size();
    }

    public void updateData(List<Deposit> newDeposits) {
        deposits = newDeposits != null ? newDeposits : new ArrayList<>();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemDepositBinding binding;

        ViewHolder(ItemDepositBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
