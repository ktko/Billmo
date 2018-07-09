package com.example.katieko.finalproject;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by katieko on 4/29/17.
 */

public class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.ViewHolder> {
    ArrayList<PaymentPlan> plans;

    public PlansAdapter(ArrayList<PaymentPlan> plans) {
        this.plans = plans;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View planListItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plans_list_item, parent, false);
        return new ViewHolder(planListItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PaymentPlan plan = plans.get(position);
        String billTotal = "$" + String.format("%.2f", plan.getFinalTotal());

        holder.titleView.setText(plan.getTitle());
        holder.finalTotalView.setText(billTotal);
        holder.descriptionView.setText(plan.getDescription());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra("plan", plan.getSearchCode());

                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleView;
        public TextView finalTotalView;
        public TextView descriptionView;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            titleView = (TextView) itemView.findViewById(R.id.titleTextView);
            finalTotalView = (TextView) itemView.findViewById(R.id.finalTotalTextView);
            descriptionView = (TextView) itemView.findViewById(R.id.descriptionTextView);
        }
    }
}
