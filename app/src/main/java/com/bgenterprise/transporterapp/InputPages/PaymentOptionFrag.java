package com.bgenterprise.transporterapp.InputPages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bgenterprise.transporterapp.AppExecutors;
import com.bgenterprise.transporterapp.Database.TransporterDatabase;
import com.bgenterprise.transporterapp.R;
import com.bgenterprise.transporterapp.SessionManager;
import com.bgenterprise.transporterapp.Utility;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentOptionFrag extends DialogFragment {
    @BindView(R.id.input_payment_option)
    TextInputLayout input_payment_option;

    @BindView(R.id.input_bg_card_number)
    TextInputLayout input_bg_card_number;

    @BindView(R.id.input_bank_name)
    TextInputLayout input_bank_name;

    @BindView(R.id.input_account_number)
    TextInputLayout input_account_number;

    @BindView(R.id.input_account_name)
    TextInputLayout input_account_name;

    @BindView(R.id.btn_submit_payment_option)
    MaterialButton btn_submit_payment_option;

    @BindView(R.id.btn_cancel_payment_option)
    MaterialButton btn_cancel_payment_option;

    @BindView(R.id.atv_payment_option)
    AutoCompleteTextView atv_payment_option;

    @BindView(R.id.atv_bank_name)
    AutoCompleteTextView atv_bank_name;

    @BindView(R.id.edit_bg_card_number)
    TextInputEditText edit_bg_card_number;

    @BindView(R.id.edit_account_number)
    TextInputEditText edit_account_number;

    @BindView(R.id.edit_account_name)
    TextInputEditText edit_account_name;

    private SessionManager sessionM;
    private String driver_id;
    TransporterDatabase transportdb;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_option, container, false);
        setCancelable(false);
        ButterKnife.bind(this, view);
        sessionM =  new SessionManager(getActivity());
        transportdb = TransporterDatabase.getInstance(getActivity());
        driver_id = "";

        Bundle pbundle = this.getArguments();
        if(pbundle != null){
            driver_id = pbundle.getString("driver_id", "");
        }

        initPaymentOption();
        initBankOption();

        atv_payment_option.setOnItemClickListener((adapterView, view1, i, l) -> {
            switch (atv_payment_option.getText().toString()){
                case "Cash":
                    CashOptionSelected();
                    clearErrorMsg();
                    break;
                case "BG Prepaid Card":
                    PrepaidOptionSelected();
                    clearErrorMsg();
                    break;
                case "Bank Account":
                    BankOptionSelected();
                    clearErrorMsg();
                    break;
                default:
                    Toast.makeText(getActivity(), "Non-recognized option", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    @OnClick(R.id.btn_submit_payment_option)
    void submit(){
        if(!isEmptyInputs()){
            if(!driver_id.equals("")){
                //Means its from Profile page. Save directly
                AppExecutors.getInstance().diskIO().execute(() -> {
                    transportdb.getDriverDao().updatePaymentOption(atv_payment_option.getText().toString(),
                            edit_bg_card_number.getText().toString(),
                            edit_account_number.getText().toString(),
                            edit_account_name.getText().toString(),
                            atv_bank_name.getText().toString(),
                            driver_id,
                            "no");

                    getActivity().runOnUiThread(() -> getActivity().recreate());
                });

            }else {
                //Save values to shared preferences.
                sessionM.SET_PAYMENT_DETAILS(atv_payment_option.getText().toString(),
                        edit_bg_card_number.getText().toString(),
                        edit_account_number.getText().toString(),
                        edit_account_name.getText().toString(),
                        atv_bank_name.getText().toString());
            }

            dismiss();
        }
    }

    @OnClick(R.id.btn_cancel_payment_option)
    void cancel(){
        dismiss();
    }

    private void initPaymentOption(){
        ArrayAdapter<String> paymentOption =
                new ArrayAdapter<>(getActivity(),
                        R.layout.dropdown_menu_popup_item,
                        Utility.payment_option);
        atv_payment_option.setAdapter(paymentOption);
    }

    private void initBankOption(){
        ArrayAdapter<String> paymentOption =
                new ArrayAdapter<>(getActivity(),
                        R.layout.dropdown_menu_popup_item,
                        Utility.bank_names);
        atv_bank_name.setAdapter(paymentOption);
    }

    private void CashOptionSelected(){
        //Do Nothing. Just save cash as option.
        //Adjust Visibilities
        input_account_name.setVisibility(View.GONE);
        input_account_number.setVisibility(View.GONE);
        input_bank_name.setVisibility(View.GONE);
        input_bg_card_number.setVisibility(View.GONE);

        //Adjust Values
        edit_account_name.setText("N/A");
        edit_account_number.setText("N/A");
        atv_bank_name.setText("N/A");
        edit_bg_card_number.setText("N/A");
    }

    private void PrepaidOptionSelected(){
        //Adjust Visibilities
        input_bg_card_number.setVisibility(View.VISIBLE);
        input_account_name.setVisibility(View.GONE);
        input_account_number.setVisibility(View.GONE);
        input_bank_name.setVisibility(View.GONE);

        //Adjust Values
        edit_bg_card_number.setText("");
        edit_account_name.setText("N/A");
        edit_account_number.setText("N/A");
        atv_bank_name.setText("N/A");
    }

    private void BankOptionSelected(){
        //Adjust Visibilities
        input_account_name.setVisibility(View.VISIBLE);
        input_account_number.setVisibility(View.VISIBLE);
        input_bank_name.setVisibility(View.VISIBLE);
        input_bg_card_number.setVisibility(View.GONE);

        //Adjust Values
        edit_account_name.setText("");
        edit_account_number.setText("");
        atv_bank_name.setText("");
        edit_bg_card_number.setText("N/A");
    }

    private void clearErrorMsg(){
        input_payment_option.setErrorEnabled(false);
        input_account_number.setErrorEnabled(false);
        input_account_name.setErrorEnabled(false);
        input_bank_name.setErrorEnabled(false);
        input_bg_card_number.setErrorEnabled(false);
    }

    private boolean isEmptyInputs(){
        //First clear all previous errors
        clearErrorMsg();

        if(atv_payment_option.getText().toString().isEmpty()){
            input_payment_option.setError("This input is required");
            return true;
        }

        //Check for prepaid card
        if(atv_payment_option.getText().toString().equals("BG Prepaid Card")){
            if(edit_bg_card_number.getText().toString().isEmpty() || edit_bg_card_number.getText().toString().equals("N/A")){
                input_bg_card_number.setError("This input is required");
                return true;
            }
        }

        //Check for bank account
        if(atv_payment_option.getText().toString().equals("Bank Account")){
            if(atv_bank_name.getText().toString().isEmpty() || atv_bank_name.getText().toString().equals("N/A")){
                input_bank_name.setError("This input is required");
                return true;
            } else if(edit_account_name.getText().toString().isEmpty() || edit_account_name.getText().toString().equals("N/A")){
                input_account_name.setError("This input is required");
                return true;
            } else if(edit_account_number.getText().toString().isEmpty() || edit_account_number.getText().toString().equals("N/A")){
                input_account_number.setError("This input is required");
                return true;
            } else if(edit_account_number.getText().toString().length() != 10){
                input_account_number.setError("Account number must be 10 digits");
                return true;
            }
        }

        return false;
    }

}
