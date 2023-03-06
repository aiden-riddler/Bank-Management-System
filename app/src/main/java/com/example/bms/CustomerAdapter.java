package com.example.bms;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        holder.accountsRecycler.setHasFixedSize(true);
        holder.accountsRecycler.setLayoutManager(new LinearLayoutManager(context));
        holder.accountsRecycler.setAdapter(accountAdapter);
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
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
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                                    CustomerController.removeCustomer(customer, context);
                                    customers.remove(getAdapterPosition());
                                    notifyDataSetChanged();
                                }
                            });

                }
            });
            edit = itemView.findViewById(R.id.edit);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Customer customer = customers.get(getAdapterPosition());
                    String firstname = InputValidation.toTitleCase(customer.getFirstName());
                    String lastname = InputValidation.toTitleCase(customer.getLastName());
                    new MaterialAlertDialogBuilder(context)
                            .setTitle("Alert")
                            .setMessage("Edit Customer \nID:" + customers.get(getAdapterPosition()).getCustomerId() + " and \nName: " + firstname + " " + lastname + "? This action is permanent.")
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
                                    context.startActivity(intent);
                                }
                            });

                }
            });
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


}
