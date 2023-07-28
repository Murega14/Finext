package com.example.finext;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.finext.databinding.ActivityMainBinding;
import com.example.finext.fragments.BillpaymentFragment;
import com.example.finext.fragments.BudgetFragment;
import com.example.finext.fragments.DashboardFragment;
import com.example.finext.fragments.ExpenseFragment;
import com.example.finext.views.ExpenseView;
import com.example.finext.views.BillsView;
import com.example.finext.views.BudgetView;
import com.example.finext.views.Insights;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.finext.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new DashboardFragment());

        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.expense) {
                startActivity(new Intent(MainActivity.this, ExpenseView.class));
            } else if (item.getItemId() == R.id.budgets) {
                startActivity(new Intent(MainActivity.this, BudgetView.class));
            } else if (item.getItemId() == R.id.bills) {
                startActivity(new Intent(MainActivity.this, BillsView.class));
            } else if (item.getItemId() == R.id.insights) {
                startActivity(new Intent(MainActivity.this, Insights.class));
            }
            return true;
        });

        binding.fab.setOnClickListener(view -> showBottomDialog());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void showBottomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout videoLayout = dialog.findViewById(R.id.expense);
        LinearLayout shortsLayout = dialog.findViewById(R.id.budgets);
        LinearLayout liveLayout = dialog.findViewById(R.id.bills);

        videoLayout.setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(MainActivity.this, "Add an expense", Toast.LENGTH_SHORT).show();
            replaceFragment(new ExpenseFragment());
        });

        shortsLayout.setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(MainActivity.this, "Add a budget", Toast.LENGTH_SHORT).show();
            replaceFragment(new BudgetFragment());
        });

        liveLayout.setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(MainActivity.this, "Add a bill", Toast.LENGTH_SHORT).show();
            replaceFragment(new BillpaymentFragment());
        });

        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

}
