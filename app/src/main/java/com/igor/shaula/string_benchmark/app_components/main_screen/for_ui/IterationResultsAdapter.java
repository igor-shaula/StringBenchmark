package com.igor.shaula.string_benchmark.app_components.main_screen.for_ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.igor.shaula.string_benchmark.annotations.TypeDoc;

import java.util.List;

@TypeDoc(createdBy = "shaula", createdOn = "23.06.2018", purpose = "")
public class IterationResultsAdapter extends RecyclerView.Adapter<IterationResultsAdapter.ViewHolder> {

    @NonNull
    private List<OneIterationResultModel> iterationResultModels;

    public void updateIterationsResult(List<OneIterationResultModel> resultModelList) {
        iterationResultModels = resultModelList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView methodName;
        private TextView methodResult;

        private ViewHolder(@NonNull ViewGroup viewGroup) {
            super(viewGroup);
            methodName = viewGroup.findViewById(android.R.id.text1);
            methodResult = viewGroup.findViewById(android.R.id.text2);
        }
    }

    public IterationResultsAdapter(@NonNull List<OneIterationResultModel> iterationResultModels) {
        this.iterationResultModels = iterationResultModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.methodName.setText(String.valueOf(iterationResultModels.get(position).getTestedMethodName()));
        holder.methodResult.setText(String.valueOf(iterationResultModels.get(position).getTestedMethodTime()));
    }

    @Override
    public int getItemCount() {
        return iterationResultModels.size();
    }
}