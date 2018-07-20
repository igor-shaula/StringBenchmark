package com.igor.shaula.string_benchmark.android_related.main_screen.for_ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.igor.shaula.string_benchmark.R;
import com.igor.shaula.string_benchmark.logic_engine.OneIterationResultModel;
import com.igor.shaula.string_benchmark.utils.annotations.TypeDoc;

import java.util.List;

@TypeDoc(createdBy = "shaula", createdOn = "23.06.2018", purpose = "")
public final class IterationResultsAdapterWithListX extends
        RecyclerView.Adapter<IterationResultsAdapterWithListX.ViewHolder> {

    @Nullable
    private List<OneIterationResultModel> iterationResultModels;

    public IterationResultsAdapterWithListX(@NonNull List<OneIterationResultModel> iterationResultList) {
        this.iterationResultModels = iterationResultList;
    }

    public void updateIterationsResult(@Nullable List<OneIterationResultModel> resultModelList) {
        iterationResultModels = resultModelList;
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
        final OneIterationResultModel oneIterationResultModel = iterationResultModels.get(position);
        holder.methodName.setText(String.valueOf(oneIterationResultModel.getTestedMethodName()));
        holder.methodResult.setText(String.valueOf(oneIterationResultModel.getTestedMethodTime()));
    }

    @Override
    public int getItemCount() {
        return iterationResultModels != null ? iterationResultModels.size() : 0;
    }

    static final class ViewHolder extends RecyclerView.ViewHolder {

        private TextView methodName;
        private TextView methodResult;

        private ViewHolder(@NonNull ViewGroup viewGroup) {
            super(viewGroup);
            methodName = viewGroup.findViewById(R.id.tvMethodName);
            methodResult = viewGroup.findViewById(R.id.tvMethodResult);
        }
    }
}