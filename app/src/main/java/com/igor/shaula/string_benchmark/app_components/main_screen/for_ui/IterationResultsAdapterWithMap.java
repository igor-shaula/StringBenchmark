package com.igor.shaula.string_benchmark.app_components.main_screen.for_ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.igor.shaula.string_benchmark.R;
import com.igor.shaula.string_benchmark.annotations.TypeDoc;

import java.util.Map;

@TypeDoc(createdBy = "shaula", createdOn = "23.06.2018", purpose = "")
public final class IterationResultsAdapterWithMap extends
        RecyclerView.Adapter<IterationResultsAdapterWithMap.ViewHolder> {

    private int currentIterationIndex;
    @NonNull
    private Map<String, Long> iterationResultModels;

    public IterationResultsAdapterWithMap(@NonNull Map<String, Long> iterationResultList) {
        this.iterationResultModels = iterationResultList;
    }

    public void updateIterationsResult(@NonNull Map<String, Long> resultModelList) {
        iterationResultModels = resultModelList;
    }

    public void updateIterationsResult(@NonNull Map<String, Long> resultModelMap, int currentIterationIndex) {
        this.iterationResultModels = resultModelMap;
        this.currentIterationIndex = currentIterationIndex;
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
        int keyPosition = 0;
        String keyAtPosition = null;
        for (final String key : iterationResultModels.keySet()) {
            if (position == keyPosition) {
                keyAtPosition = key;
            }
            keyPosition++;
        }
        holder.methodName.setText(keyAtPosition);
//        holder.methodName.setText(String.valueOf(iterationResultModels.get(keyAtPosition)));
        holder.methodResult.setText(String.valueOf(
                iterationResultModels.get(keyAtPosition) / currentIterationIndex + 1
        ));
    }

    @Override
    public int getItemCount() {
        return iterationResultModels.size();
    }
}