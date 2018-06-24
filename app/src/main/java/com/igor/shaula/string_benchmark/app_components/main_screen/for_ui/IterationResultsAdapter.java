package com.igor.shaula.string_benchmark.app_components.main_screen.for_ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.igor.shaula.string_benchmark.R;
import com.igor.shaula.string_benchmark.annotations.TypeDoc;

import java.util.List;

@TypeDoc(createdBy = "shaula", createdOn = "23.06.2018", purpose = "")
public class IterationResultsAdapter extends RecyclerView.Adapter<IterationResultsAdapter.ViewHolder> {

    @Nullable
    private List<OneIterationResultModel> iterationResultModels;

    public void updateIterationsResult(List<OneIterationResultModel> resultModelList) {
        iterationResultModels = resultModelList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView methodName;
        private TextView methodResult;

        private ViewHolder(@NonNull ViewGroup viewGroup) {
            super(viewGroup);
            methodName = viewGroup.findViewById(R.id.tvMethodName);
            methodResult = viewGroup.findViewById(R.id.tvMethodResult);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_view, parent, false);
        return new ViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (iterationResultModels == null) {
            return;
        }
        holder.methodName.setText(String.valueOf(iterationResultModels.get(position).getTestedMethodName()));
        holder.methodResult.setText(String.valueOf(iterationResultModels.get(position).getTestedMethodTime()));
    }

    @Override
    public int getItemCount() {
        return iterationResultModels != null ? iterationResultModels.size() : 0;
    }
}