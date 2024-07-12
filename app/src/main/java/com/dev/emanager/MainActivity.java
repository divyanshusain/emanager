package com.dev.emanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView totalBudgetTextView;
    private TextView remainingBudgetTextView;
    private EditText totalBudgetEditText;
    private List<Expense> expenseList;
    private double totalBudget;
    private double remainingBudget;
    private ListView expenseListView;
    private ExpenseListAdapter expenseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalBudgetTextView = findViewById(R.id.total_budget_textview);
        remainingBudgetTextView = findViewById(R.id.remaining_budget_textview);
        totalBudgetEditText = findViewById(R.id.total_budget_edittext);
        Button setBudgetButton = findViewById(R.id.set_budget_button);
        expenseListView = findViewById(R.id.expense_listview);

        // Initialize expense list
        expenseList = new ArrayList<>();
        expenseAdapter = new ExpenseListAdapter(this, R.layout.expense_list_item, expenseList);
        expenseListView.setAdapter(expenseAdapter);

        setBudgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String totalBudgetStr = totalBudgetEditText.getText().toString().trim();
                if (!totalBudgetStr.isEmpty()) {
                    totalBudget = Double.parseDouble(totalBudgetStr);
                    remainingBudget = totalBudget;
                    updateBudgetViews();
                    totalBudgetEditText.setEnabled(false); // Disable editing after setting budget
                    setBudgetButton.setEnabled(false); // Disable button after setting budget
                }
            }
        });

        findViewById(R.id.add_expense_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddExpenseDialog();
            }
        });

        findViewById(R.id.edit_expense_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditExpenseDialog();
            }
        });
    }

    private void showAddExpenseDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_expense, null);
        dialogBuilder.setView(dialogView);

        final EditText expenseNameEditText = dialogView.findViewById(R.id.expense_name_edittext);
        final EditText expenseAmountEditText = dialogView.findViewById(R.id.expense_amount_edittext);

        dialogBuilder.setTitle("Add Expense");
        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String expenseName = expenseNameEditText.getText().toString().trim();
                String expenseAmountStr = expenseAmountEditText.getText().toString().trim();

                if (!expenseName.isEmpty() && !expenseAmountStr.isEmpty()) {
                    double expenseAmount = Double.parseDouble(expenseAmountStr);
                    Expense expense = new Expense(expenseName, expenseAmount);
                    expenseList.add(expense);

                    remainingBudget -= expenseAmount;
                    updateBudgetViews();
                    expenseAdapter.notifyDataSetChanged(); // Update the list view
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void showEditExpenseDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_edit_expense, null);
        dialogBuilder.setView(dialogView);

        final EditText expenseNameEditText = dialogView.findViewById(R.id.expense_name_edittext);
        final EditText expenseAmountEditText = dialogView.findViewById(R.id.expense_amount_edittext);

        dialogBuilder.setTitle("Edit Expense");
        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String expenseName = expenseNameEditText.getText().toString().trim();
                String expenseAmountStr = expenseAmountEditText.getText().toString().trim();

                if (!expenseName.isEmpty() && !expenseAmountStr.isEmpty()) {
                    double newExpenseAmount = Double.parseDouble(expenseAmountStr);

                    // Find the expense to edit
                    Expense selectedExpense = null;
                    for (Expense expense : expenseList) {
                        if (expense.getName().equals(expenseName)) {
                            selectedExpense = expense;
                            break;
                        }
                    }

                    if (selectedExpense != null) {
                        remainingBudget += selectedExpense.getAmount(); // Add back the previous amount
                        remainingBudget -= newExpenseAmount; // Deduct the updated amount
                        selectedExpense.setAmount(newExpenseAmount);
                        updateBudgetViews();
                        expenseAdapter.notifyDataSetChanged(); // Update the list view
                    }
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void updateBudgetViews() {
        totalBudgetTextView.setText(String.format("Total Budget: ₹ %s", formatCurrency(totalBudget)));
        remainingBudgetTextView.setText(String.format("Remaining Budget: ₹ %s", formatCurrency(remainingBudget)));
    }

    private String formatCurrency(double amount) {
        DecimalFormat formatter = new DecimalFormat("#,##0.00");
        return formatter.format(amount);
    }
}
