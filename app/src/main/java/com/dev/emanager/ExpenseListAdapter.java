package com.dev.emanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ExpenseListAdapter extends ArrayAdapter<Expense> {

    private Context mContext;
    private int mResource;

    public ExpenseListAdapter(Context context, int resource, List<Expense> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the expense information
        String name = getItem(position).getName();
        double amount = getItem(position).getAmount();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView expenseNameTextView = convertView.findViewById(R.id.expense_name_textview);
        TextView expenseAmountTextView = convertView.findViewById(R.id.expense_amount_textview);

        expenseNameTextView.setText(name);
        expenseAmountTextView.setText(String.format("₹ %.2f", amount));

        return convertView;
    }
}
