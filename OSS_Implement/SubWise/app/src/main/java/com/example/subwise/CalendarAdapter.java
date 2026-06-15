package com.example.subwise;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {
    private List<DayItem> dayList;

    public CalendarAdapter(List<DayItem> dayList) {
        this.dayList = dayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DayItem day = dayList.get(position);

        if (day.getDay() == -1) {
            // 빈칸 처리
            holder.txtDay.setText("");
            holder.txtServiceName.setText("");
            holder.viewCategoryBar.setBackgroundColor(Color.TRANSPARENT);
        } else {
            // 정상 날짜 처리
            holder.txtDay.setText(String.valueOf(day.getDay()));
            holder.txtServiceName.setText(day.getServiceName() != null ? day.getServiceName() : "");
            holder.viewCategoryBar.setBackgroundColor(day.getCategoryColor());
        }
    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDay, txtServiceName;
        View viewCategoryBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDay = itemView.findViewById(R.id.txtDay);
            txtServiceName = itemView.findViewById(R.id.txtServiceName);
            viewCategoryBar = itemView.findViewById(R.id.viewCategoryBar);
        }
    }
}
