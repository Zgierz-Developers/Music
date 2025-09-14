package com.example.miuzik;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.ViewHolder> {
    private List<String> items;

    public FilesAdapter(List<String> items) {
        this.items = items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textItem);
        }
    }

    @NonNull
    @Override
    public FilesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);
        return new FilesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilesAdapter.ViewHolder holder, int position) {
        holder.textView.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<String> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }
}
