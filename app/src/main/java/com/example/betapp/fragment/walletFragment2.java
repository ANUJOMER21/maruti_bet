package com.example.betapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.betapp.api.ApiCall;
import com.example.betapp.api.ApiResponse;
import com.example.betapp.misc.CommonSharedPrefernces;
import com.example.betapp.model.WebsiteSettings;
import com.example.betapp.model.user;
import com.example.betapp.R;
import com.google.gson.JsonObject;
import com.shreyaspatil.easyupipayment.EasyUpiPayment;
import com.shreyaspatil.easyupipayment.listener.PaymentStatusListener;

import com.shreyaspatil.easyupipayment.model.PaymentApp;
import com.shreyaspatil.easyupipayment.model.TransactionDetails;


import java.util.HashMap;
import java.util.Random;

public class walletFragment2 extends Fragment implements PaymentStatusListener {
    private String param1;
    private String param2;
    private static String ARG_PARAM1 = "param1";
    private static String ARG_PARAM2 = "param2";
    private CardView successAddmoneyCard;
    private ImageView successIconWithdraw;
    private TextView successWithdrawLabel;
    private TextView successWithdrawDescription;
    private AppCompatButton successAddMoneyBtn;

    private LinearLayout layoutAddFunds;
    private TextView txtWalletBalance;
    private EditText walletAmountAdd;
    private TextView txtMinAmount;
    private CardView layoutGoogle;
    private TextView txtGooglePayNote;
    private CardView layoutPhonepe;
    private CardView layoutphonepe2;
    private TextView txtPhonePeNote;
    private CardView layoutPaytm;
    private TextView txtPaytmNote;
    private CardView layoutOther;
    private TextView txtOtherNote;

    private com.example.betapp.api.ApiCall ApiCall;
    private user userId;
    private int min_Deposit = Integer.MIN_VALUE;
    private String upi = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            param1 = getArguments().getString(ARG_PARAM1);
            param2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        initview(view);
        ApiCall = new ApiCall();
        updateWallet();


        ApiCall.getupiId(new ApiCall.WebseiteSetting() {
            @Override
            public void onWebsiteSettingReceived(String wh) {
                upi = wh;
                Log.d("upi_wh", wh);
            }

            @Override
            public void onFailure(String error) {
                Log.d("upierror", error);
                upi = "";
            }
        });

        ApiCall.apiconfig(new ApiCall.WebseiteSettingInterface() {
            @Override
            public void onWebsiteSettingReceived(WebsiteSettings wh) {
                min_Deposit = Integer.parseInt(wh.getMin_deposit());
            }

            @Override
            public void onFailure(String error) {
                min_Deposit = Integer.MAX_VALUE;
            }
        });

        ApiCall.deposittypr(new ApiCall.DepositType() {
            @Override
            public void onTypeGet(HashMap<String, Boolean> map) {
                if (map.get("amazon") == Boolean.TRUE) {
                    layoutGoogle.setVisibility(View.GONE );
                } else {
                    layoutGoogle.setVisibility(View.GONE);
                }
                if (map.get("paytm") == Boolean.TRUE) {
                    layoutPaytm.setVisibility(View.VISIBLE);
                } else {
                    layoutPaytm.setVisibility(View.GONE);
                }
                if (map.get("gpay") == Boolean.TRUE) {
                    layoutOther.setVisibility(View.VISIBLE);
                } else {
                    layoutOther.setVisibility(View.GONE);
                }
                if (map.get("phonepe") == Boolean.TRUE) {
                    layoutphonepe2.setVisibility(View.VISIBLE);
                } else {
                    layoutphonepe2.setVisibility(View.GONE);
                }
                if (map.get("bhim") == Boolean.TRUE) {
                    layoutPhonepe.setVisibility(View.VISIBLE);
                } else {
                    layoutPhonepe.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(String error) {
                layoutGoogle.setVisibility(View.GONE);
                layoutPaytm.setVisibility(View.GONE);
                layoutphonepe2.setVisibility(View.GONE);
                layoutPhonepe.setVisibility(View.GONE);
                layoutOther.setVisibility(View.GONE);
            }
        });

        layoutGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (walletAmountAdd.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter amount to deposit", Toast.LENGTH_SHORT).show();
                } else {
                    payment(PaymentApp.AMAZON_PAY, walletAmountAdd.getText().toString());
                }
            }
        });

        layoutPaytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (walletAmountAdd.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter amount to deposit", Toast.LENGTH_SHORT).show();
                } else {
                    payment(PaymentApp.PAYTM, walletAmountAdd.getText().toString());
                }
            }
        });

        layoutPhonepe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (walletAmountAdd.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter amount to deposit", Toast.LENGTH_SHORT).show();
                } else {
                    payment(PaymentApp.BHIM_UPI, walletAmountAdd.getText().toString());
                }
            }
        });

        layoutOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (walletAmountAdd.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter amount to deposit", Toast.LENGTH_SHORT).show();
                } else {
                    payment(PaymentApp.GOOGLE_PAY, walletAmountAdd.getText().toString());
                }
            }
        });

        layoutphonepe2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (walletAmountAdd.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter amount to deposit", Toast.LENGTH_SHORT).show();
                } else {
                    payment(PaymentApp.PHONE_PE, walletAmountAdd.getText().toString());
                }
            }
        });

        return view;
    }

    private String amt = "0";
    private PaymentApp pt;
    private String time="0";

    private void payment(PaymentApp paymenttype, String amount) {
        amt = amount;
        pt = paymenttype;
        time=String.valueOf(System.nanoTime());
        if (Integer.parseInt(amount) < min_Deposit) {
            Toast.makeText(getActivity(), "Minimum Deposit is " + min_Deposit, Toast.LENGTH_SHORT).show();
        } else {
            try {
                Random random = new Random();
                String random10DigitNumber = String.valueOf(random.ints(0, 10).limit(10).toArray());
                double amt = Double.parseDouble(amount);
                EasyUpiPayment.Builder eupi = new EasyUpiPayment.Builder(getActivity())
                        .with(paymenttype)
                        .setPayeeVpa(upi)
                        .setPayeeName(getResources().getString(R.string.app_name))
                        .setTransactionId(time)
                        .setTransactionRefId(random10DigitNumber)
                        .setPayeeMerchantCode("5200")
                        .setDescription(time)
                        .setAmount(String.valueOf(amt));
                EasyUpiPayment easyUpiPayment = eupi.build();
                easyUpiPayment.setPaymentStatusListener(this);
                easyUpiPayment.startPayment();
            } catch (Exception e) {
                Toast.makeText(getActivity(),"App not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private void addmoney(String amount, PaymentApp paymenttype, String transactionId) {
        Random random = new Random();
        String random10DigitNumber = time;

        ApiCall.depositApi(userId.getId(), amount, paymenttype.name(), random10DigitNumber, new ApiResponse() {
            @Override
            public void onSuccess(JsonObject jsonObject) {
                if (jsonObject.get("status").toString().equals("\"success\"")) {
                    Toast.makeText(getActivity(), "Amount Deposited", Toast.LENGTH_SHORT).show();
                    CustomDialogFragment customDialogFragment = new CustomDialogFragment();
                    customDialogFragment.show(getChildFragmentManager(), "CustomDialogFragment");
                    updateWallet();
                }
            }

            @Override
            public void onFailure(String failure) {
                Toast.makeText(getActivity(), failure, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initview(View view) {
        userId = new CommonSharedPrefernces((Context) getActivity()).getuser();
        if (view != null) {
            successAddmoneyCard = view.findViewById(R.id.successAddmoneyCard);
            successIconWithdraw = view.findViewById(R.id.successIconWithdraw);
            successWithdrawLabel = view.findViewById(R.id.successWithdrawLable);
            successWithdrawDescription = view.findViewById(R.id.succesWithdrawDiscription);
            successAddMoneyBtn = view.findViewById(R.id.successAddMoneyBtn);
            layoutphonepe2 = view.findViewById(R.id.layoutPhonepe2);
            layoutAddFunds = view.findViewById(R.id.layout_addFunds);
            txtWalletBalance = view.findViewById(R.id.txtWalletBalance);
            walletAmountAdd = view.findViewById(R.id.wallet_amount_add);
            txtMinAmount = view.findViewById(R.id.txt_minamount);
            layoutGoogle = view.findViewById(R.id.layoutGoogle);
            txtGooglePayNote = view.findViewById(R.id.txt_googlepaynote);
            layoutPhonepe = view.findViewById(R.id.layoutPhonepe);
            txtPhonePeNote = view.findViewById(R.id.txt_phonepenote);
            layoutPaytm = view.findViewById(R.id.layoutPaytm);
            txtPaytmNote = view.findViewById(R.id.txt_paytmnote);
            layoutOther = view.findViewById(R.id.layoutOther);
            txtOtherNote = view.findViewById(R.id.txt_othernote);
        }
        successAddMoneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                successAddmoneyCard.setVisibility(View.GONE);
                walletAmountAdd.setText("");
            }
        });
    }

    private void updateWallet() {
        Log.d("asdf",userId.getId());
        ApiCall.walletApi(userId.getId(), new ApiResponse() {
            @Override
            public void onSuccess(JsonObject jsonObject) {
                if (jsonObject != null) {
                    if (jsonObject.get("status").toString().equals("\"success\"")) {
                        String b = jsonObject.get("walletBalance").toString().replace("\"", "");
                        txtWalletBalance.setText(b);
                        LocalBroadcastManager.getInstance((Context) getActivity()).sendBroadcast(new Intent("wallet"));
                    } else {
                        txtWalletBalance.setText("0");
                    }
                }
            }

            @Override
            public void onFailure(String failure) {
                txtWalletBalance.setText("0");
            }
        });
    }

    public static walletFragment newInstance(String param1, String param2) {
        walletFragment fragment = new walletFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onTransactionCancelled() {
        Toast.makeText(getActivity(), "Failed to deposit", Toast.LENGTH_SHORT).show();
    }

    private TransactionDetails Tsd;

    private void onTransactionSuccess() {
        addmoney(amt, pt, Tsd.getTransactionId());
    }

    private void onTransactionSubmitted() {
        Log.d("wallet", "submitted");
    }

    private void onTransactionFailed() {
        Toast.makeText(getActivity(), "Transaction Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {
        Tsd = transactionDetails;
        switch (transactionDetails.getTransactionStatus()) {
            case SUCCESS:
                onTransactionSuccess();
                break;
            case FAILURE:
                onTransactionFailed();
                break;
            case SUBMITTED:
                onTransactionSubmitted();
                break;
        }
    }
}
