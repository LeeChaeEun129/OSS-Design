package com.example.subwise;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.ViewHolder> {

    private List<Subscription> subscriptionList;
    private OnEditClickListener editListener;
    private OnDeleteClickListener deleteListener;

    public interface OnEditClickListener {
        void onEditClick(Subscription sub);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Subscription sub);
    }

    public SubscriptionAdapter(List<Subscription> subscriptionList,
                               OnEditClickListener editListener,
                               OnDeleteClickListener deleteListener) {
        this.subscriptionList = subscriptionList;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subscription, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Subscription sub = subscriptionList.get(position);
        holder.txtServiceName.setText(sub.getServiceName());
        holder.txtPrice.setText("₩" + sub.getPrice());
        holder.txtBillingDate.setText("결제일: " + sub.getBillingDate());

        // ✅ 카테고리 표시
        holder.txtCategory.setText("카테고리: " + sub.getCategory());

        // ✅ 파티 정보 표시
        if (sub.getPartyName() != null && !sub.getPartyName().isEmpty()) {
            holder.txtPartyInfo.setText("파티: " + sub.getPartyName() + " (" + sub.getPartyCount() + "명)");
        } else {
            holder.txtPartyInfo.setText("파티 없음");
        }

        holder.btnEdit.setOnClickListener(v -> editListener.onEditClick(sub));
        holder.btnDelete.setOnClickListener(v -> deleteListener.onDeleteClick(sub));
    }

    @Override
    public int getItemCount() {
        return subscriptionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtServiceName, txtPrice, txtBillingDate, txtCategory, txtPartyInfo;
        Button btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtServiceName = itemView.findViewById(R.id.txtServiceName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtBillingDate = itemView.findViewById(R.id.txtBillingDate);
            txtCategory = itemView.findViewById(R.id.txtCategory);   // ✅ 추가
            txtPartyInfo = itemView.findViewById(R.id.txtPartyInfo); // ✅ 추가
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
