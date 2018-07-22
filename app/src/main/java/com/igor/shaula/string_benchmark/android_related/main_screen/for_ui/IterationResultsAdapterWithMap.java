package com.igor.shaula.string_benchmark.android_related.main_screen.for_ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.igor.shaula.string_benchmark.R;
import com.igor.shaula.string_benchmark.utils.U;
import com.igor.shaula.string_benchmark.utils.annotations.MeDoc;
import com.igor.shaula.string_benchmark.utils.annotations.TypeDoc;

import java.util.HashMap;
import java.util.Map;

@TypeDoc(createdBy = "shaula", createdOn = "23.06.2018", purpose = "")
public final class IterationResultsAdapterWithMap extends
        RecyclerView.Adapter<IterationResultsAdapterWithMap.ViewHolder> {

    private int currentIterationIndex;
    @NonNull
    private Map<String, Long> iterationResultModels;

    public IterationResultsAdapterWithMap() {
        iterationResultModels = new HashMap<>(0);
    }

    public void updateIterationsResult(@NonNull Map<String, Long> iterationResultMap,
                                       int currentIterationIndex) {
        this.iterationResultModels = iterationResultMap;
        this.currentIterationIndex = currentIterationIndex;
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
        holder.methodResult.setText(
                U.adaptForUser(ViewHolder.unitsOfMeasurement,
                        iterationResultModels.get(keyAtPosition) / (currentIterationIndex + 1)));
    }

    @Override
    public int getItemCount() {
        return iterationResultModels.size();
    }

    static final class ViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private TextView methodName;
        @NonNull
        private TextView methodResult;

        @SuppressWarnings("NullableProblems")
        // it's created once the first constructor is invoked \\
        @NonNull
        private static String[] unitsOfMeasurement; // nanos - micros - millis - seconds \\

        @MeDoc("unitsOfMeasurement created here will be later used in onBindViewHolder")
        private ViewHolder(@NonNull ViewGroup viewGroup) {
            super(viewGroup);
            methodName = viewGroup.findViewById(R.id.tvMethodName);
            methodResult = viewGroup.findViewById(R.id.tvMethodResult);
            // creation of this string-array should be done only once - at the first invocation \\
            final Context context = viewGroup.getContext();
            unitsOfMeasurement = new String[]{
                    context.getString(R.string.nanos),
                    context.getString(R.string.micros),
                    context.getString(R.string.millis),
                    context.getString(R.string.seconds)
            };
        }
    }
}