package com.igor.shaula.string_benchmark.app_components.main_screen;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.igor.shaula.string_benchmark.annotations.TypeDoc;

import java.util.List;

@TypeDoc(createdBy = "shaula", createdOn = "23.06.2018", purpose = "")
class IterationResultsAdapter extends RecyclerView.Adapter<IterationResultsAdapter.ViewHolder> {

    private List<OneIterationResultModel> iterationResultModels;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case - temporary \\
        public TextView mTextView;

        public ViewHolder(TextView textView) {
            super(textView);
            mTextView = textView;
        }
    }

    public IterationResultsAdapter(List<OneIterationResultModel> iterationResultModels) {
        this.iterationResultModels = iterationResultModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mTextView.setText(String.valueOf(iterationResultModels.get(position).getTestedMethodTime()));
    }

    @Override
    public int getItemCount() {
        return iterationResultModels.size();
    }
}