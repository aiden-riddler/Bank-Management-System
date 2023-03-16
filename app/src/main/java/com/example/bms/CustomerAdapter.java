package com.example.bms;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder>{
    private ArrayList<Customer> customers;
    private ArrayList<Account> accounts;
    private Context context;
    private AccountAdapter accountAdapter;
    private UserEmailPhoneIds userEmailPhoneIds = new UserEmailPhoneIds();
    private ArrayList<Branch> branches = new ArrayList<>();

    private User user = new User();

    public CustomerAdapter(ArrayList<Customer> customers, ArrayList<Account> accounts, Context context) {
        this.customers = customers;
        this.accounts = accounts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Customer customer = customers.get(position);
        String firstname = InputValidation.toTitleCase(customer.getFirstName());
        String lastname = InputValidation.toTitleCase(customer.getLastName());

        holder.fullname.setText( firstname + " " + lastname);
        holder.customerid.setText(customer.getCustomerId());
        holder.phone.setText(customer.getPhone());
        holder.email.setText(customer.getEmail());

        ArrayList<Account> customerAccounts = new ArrayList<>();
        for (Account account:accounts) {
            if (account.getCustomer().equals(customer.getCustomerId()))
                customerAccounts.add(account);
        }

        accountAdapter = new AccountAdapter(customerAccounts, context);
        accountAdapter.setBranches(branches);
        holder.accountsRecycler.setHasFixedSize(true);
        holder.accountsRecycler.setLayoutManager(new LinearLayoutManager(context));
        holder.accountsRecycler.setAdapter(accountAdapter);
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public void setBranches(ArrayList<Branch> branches) {
        this.branches = branches;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView fullname;
        TextView customerid;
        TextView phone;
        TextView email;
        ImageView delete;
        ImageView edit;
        RecyclerView accountsRecycler;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fullname = itemView.findViewById(R.id.fullname);
            customerid = itemView.findViewById(R.id.customerid);
            phone = itemView.findViewById(R.id.phone);
            email = itemView.findViewById(R.id.email);
            accountsRecycler = itemView.findViewById(R.id.accountsRecycler);
            delete = itemView.findViewById(R.id.delete);
            delete.setOnClickListener(this);
            edit = itemView.findViewById(R.id.edit);
            edit.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == delete.getId()) {
                Customer customer = customers.get(getAdapterPosition());
                String firstname = InputValidation.toTitleCase(customer.getFirstName());
                String lastname = InputValidation.toTitleCase(customer.getLastName());
                new MaterialAlertDialogBuilder(context)
                        .setTitle("Alert")
                        .setMessage("Delete Customer \nID:" + customers.get(getAdapterPosition()).getCustomerId() + " and \nName: " + firstname + " " + lastname + "? This action is permanent.")
                        .setNeutralButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                CustomerController.removeCustomer(customer, context, user);
                                customers.remove(getAdapterPosition());
                                notifyDataSetChanged();
                            }
                        }).show();
            } else if (view.getId() == edit.getId()){
                Customer customer = customers.get(getAdapterPosition());
                String firstname = InputValidation.toTitleCase(customer.getFirstName());
                String lastname = InputValidation.toTitleCase(customer.getLastName());
                new MaterialAlertDialogBuilder(context)
                        .setTitle("Alert")
                        .setMessage("Edit Customer \nID:" + customers.get(getAdapterPosition()).getCustomerId() + "\nName: " + firstname + " " + lastname)
                        .setNeutralButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                Intent intent = new Intent(context, CustomerAdd.class);
                                intent.putExtra("Customer", customer);
                                intent.putExtra("UserEmailIDs", userEmailPhoneIds);
                                context.startActivity(intent);
                            }
                        }).show();
            }
        }
    }
    public void setCustomers(ArrayList<Customer> customers){
        this.customers = customers;
        notifyDataSetChanged();
    }
    public void setAccounts(ArrayList<Account> accounts){
        this.accounts = accounts;
        notifyDataSetChanged();
    }

    public void setUserEmailPhoneIds(UserEmailPhoneIds userEmailPhoneIds) {
        this.userEmailPhoneIds = userEmailPhoneIds;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
