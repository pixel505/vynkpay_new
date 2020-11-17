package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vynkpay.activity.PayeerAddressActivity;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.HomeActivity;
import com.vynkpay.databinding.ActivityKycBinding;
import com.vynkpay.models.GetKycStatusResponse;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.utils.M;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KycActivity extends AppCompatActivity {
    ActivityKycBinding binding;
    KycActivity ac;
    boolean shouldAllowBack;
    Dialog dialog1;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_kyc);
        ac = KycActivity.this;
        dialog1 = M.showDialog(KycActivity.this, "", false, false);
        sp = getSharedPreferences("PREFS_APP_CHECK", Context.MODE_PRIVATE);

        clicks();
        getKyc();
        getKycStatus();
    }

    private void getKycStatus() {
        if (getIntent().getStringExtra("statusKyc") != null) {
            shouldAllowBack = false;
            String status = getIntent().getStringExtra("statusKyc");


            if (status.equals("1")) {
                binding.toolbarLayout.toolbarnew.setNavigationIcon(null);

                RelativeLayout.LayoutParams params = new
                        RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 40, 0); // setMargins(left, top, right, bottom)
                binding.toolbarLayout.toolbarTitlenew.setLayoutParams(params);
                binding.kycImage.setBackgroundResource(R.drawable.unscusses);
                binding.kycText.setText(R.string.kycpending);
                binding.startVerificationBtn.setText(R.string.home);
                binding.startVerificationBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       finish();
                    }
                });
            }
        } else {
            shouldAllowBack = true;
        }
    }

    private void getKyc() {
        dialog1.show();
        MainApplication.getApiService().kycStatusMethod(Prefes.getAccessToken(KycActivity.this)).enqueue(new Callback<GetKycStatusResponse>() {
            @Override
            public void onResponse(Call<GetKycStatusResponse> call, Response<GetKycStatusResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    dialog1.dismiss();

                    try {
                        if (response.body().getStatus().equalsIgnoreCase("true")) {
                            Log.d("kycc",new Gson().toJson(response.body().getData().getUserdata()));
                            if (response.body().getData().getUserdata().getCountryCode().equals("91")) {

                                GetKycStatusResponse.Data.KycStatus getKycStatus = response.body().getData().getKycStatus();
                                GetKycStatusResponse.Data.BankDetails getBankDetails = response.body().getData().getBankDetails();
                                Log.d("bankDetail",new Gson().toJson(getBankDetails));


                                if (getKycStatus.getStaus().equals("2")) {    // kyc done  Approved


                                    binding.kycImage.setBackgroundResource(R.drawable.kycsuccess);
                                    binding.kycText.setText(response.body().getData().getKycStatus().getMessage());
                                    binding.startVerificationBtn.setText(R.string.checkdocu);

                                    if (getBankDetails != null) {

                                        if (getBankDetails.getIsactive().equals("1")) {
                                            binding.bankdetailsBtnn.setVisibility(View.VISIBLE);
                                            binding.bankdetailsBtnn.setVisibility(View.VISIBLE);
                                            binding.bankdetailsBtnn.setText("Bank Details Pending");
                                        }
                                        binding.bankdetailsBtnn.setVisibility(View.VISIBLE);


                                        String AccountType = response.body().getData().getBankDetails().getAccountType();
                                        String NameInBank = response.body().getData().getBankDetails().getNameInBank();
                                        String AccountNumber = response.body().getData().getBankDetails().getAccountNumber();
                                        String IfscCode = response.body().getData().getBankDetails().getIfscCode();
                                        String BankName = response.body().getData().getBankDetails().getBankName();
                                        String BranchAddress = response.body().getData().getBankDetails().getBranchAddress();
                                        String ChequeReceipt = response.body().getData().getBankDetails().getChequeReceipt();
                                        binding.bankdetailsBtnn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                startActivity(new Intent(KycActivity.this, KycBankRejectedActivity.class)
                                                        .putExtra("AccountType", AccountType)
                                                        .putExtra("NameInBank", NameInBank)
                                                        .putExtra("AccountNumber", AccountNumber)
                                                        .putExtra("IfscCode", IfscCode)
                                                        .putExtra("BankName", BankName)
                                                        .putExtra("BranchAddress", BranchAddress)
                                                        .putExtra("ChequeReceipt", ChequeReceipt)
                                                        .putExtra("status", getBankDetails.getIsactive())

                                                );

                                                finish();

                                            }
                                        });
                                    } else {
                                        binding.bankdetailsBtnn.setVisibility(View.VISIBLE);
                                        binding.bankdetailsBtnn.setText("Bank Details Rejected");

                                        binding.bankdetailsBtnn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                startActivity(new Intent(KycActivity.this, KycBankActivity.class).putExtra("status", "4"));
                                            }
                                        });
                                    }
                                    binding.startVerificationBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            if (getBankDetails != null) {
                                                String AccountType = response.body().getData().getBankDetails().getAccountType();
                                                String NameInBank = response.body().getData().getBankDetails().getNameInBank();
                                                String AccountNumber = response.body().getData().getBankDetails().getAccountNumber();
                                                String IfscCode = response.body().getData().getBankDetails().getIfscCode();
                                                String BankName = response.body().getData().getBankDetails().getBankName();
                                                String BranchAddress = response.body().getData().getBankDetails().getBranchAddress();
                                                String ChequeReceipt = response.body().getData().getBankDetails().getChequeReceipt();

                                                startActivity(new Intent(ac, KycSuccessActiviy.class).putExtra(
                                                        "nationIdStatus", response.body().getData().getUserdata().getNationalIdStatus())
                                                        .putExtra("addressProffStatus", response.body().getData().getUserdata().getAddressIdStatus())
                                                        .putExtra("addressProffPath", response.body().getData().getUserdata().getAddressproofPath())
                                                        .putExtra("nationProffPath", response.body().getData().getUserdata().getNationalidPath())

                                                        .putExtra("AccountType", AccountType)
                                                        .putExtra("NameInBank", NameInBank)
                                                        .putExtra("AccountNumber", AccountNumber)
                                                        .putExtra("IfscCode", IfscCode)
                                                        .putExtra("BankName", BankName)
                                                        .putExtra("BranchAddress", BranchAddress)
                                                        .putExtra("ChequeReceipt", ChequeReceipt)
                                                        .putExtra("bankStatus", getBankDetails.getIsactive())
                                                );

                                                finish();
                                            } else {
                                                startActivity(new Intent(ac, KycSuccessActiviy.class).putExtra(
                                                        "nationIdStatus", response.body().getData().getUserdata().getNationalIdStatus())
                                                        .putExtra("addressProffStatus", response.body().getData().getUserdata().getAddressIdStatus())
                                                        .putExtra("addressProffPath", response.body().getData().getUserdata().getAddressproofPath())
                                                        .putExtra("nationProffPath", response.body().getData().getUserdata().getNationalidPath())
                                                        .putExtra("bankStatus", "4"));
                                                finish();
                                            }
                                        }
                                    });

                                }     // approved   done

                                else if (getKycStatus.getStaus().equals("0")) {   // have to do kyc
                                    binding.kycImage.setBackgroundResource(R.drawable.kycverify);
                                    binding.kycText.setText(response.body().getData().getKycStatus().getMessage());
                                    binding.startVerificationBtn.setText(R.string.startverify);
                                    binding.startVerificationBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(ac, KycUploadActiviy.class));
                                        }
                                    });
                                }  // new


                                else if (getKycStatus.getStaus().equals("1")) {          // waiitng from admin //

                                    // have to do kyc  pending from admin
                                    binding.kycImage.setBackgroundResource(R.drawable.kycverify);
                                    binding.kycText.setText(response.body().getData().getKycStatus().getMessage());
                                    binding.startVerificationBtn.setText(R.string.home);
                                    binding.startVerificationBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            if (sp.getString("value", "").equals("Global") && Prefes.getisIndian(ac).equalsIgnoreCase("NO")) {
                                                startActivity(new Intent(ac, HomeActivity.class).putExtra("Country", "Global"));
                                                finish();
                                            } else if (sp.getString("value", "").equals("Global") && Prefes.getisIndian(ac).equalsIgnoreCase("YES")) {
                                                startActivity(new Intent(ac, HomeActivity.class).putExtra("Country", "India"));
                                                finish();
                                            } else if (sp.getString("value", "").equals("India") && Prefes.getisIndian(ac).equalsIgnoreCase("YES")) {
                                                startActivity(new Intent(ac, HomeActivity.class).putExtra("Country", "India"));
                                                finish();
                                            } else if (sp.getString("value", "").equals("India") && Prefes.getisIndian(ac).equalsIgnoreCase("NO")) {
                                                startActivity(new Intent(ac, HomeActivity.class).putExtra("Country", "Global"));
                                                finish();
                                            }

                                        }
                                    });
                                    if (getBankDetails != null) {
                                        binding.bankdetailsBtnn.setVisibility(View.VISIBLE);
                                        if (getBankDetails.getIsactive().equals("1")) {
                                            binding.bankdetailsBtnn.setVisibility(View.VISIBLE);
                                            binding.bankdetailsBtnn.setVisibility(View.VISIBLE);
                                            binding.bankdetailsBtnn.setText("Bank Details Pending");
                                        }
                                        binding.bankdetailsBtnn.setVisibility(View.VISIBLE);
                                        String AccountType = response.body().getData().getBankDetails().getAccountType();
                                        String NameInBank = response.body().getData().getBankDetails().getNameInBank();
                                        String AccountNumber = response.body().getData().getBankDetails().getAccountNumber();
                                        String IfscCode = response.body().getData().getBankDetails().getIfscCode();
                                        String BankName = response.body().getData().getBankDetails().getBankName();
                                        String BranchAddress = response.body().getData().getBankDetails().getBranchAddress();
                                        String ChequeReceipt = response.body().getData().getBankDetails().getChequeReceipt();
                                        binding.bankdetailsBtnn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                startActivity(new Intent(KycActivity.this, KycBankRejectedActivity.class)
                                                        .putExtra("AccountType", AccountType)
                                                        .putExtra("NameInBank", NameInBank)
                                                        .putExtra("AccountNumber", AccountNumber)
                                                        .putExtra("IfscCode", IfscCode)
                                                        .putExtra("BankName", BankName)
                                                        .putExtra("BranchAddress", BranchAddress)
                                                        .putExtra("ChequeReceipt", ChequeReceipt)
                                                        .putExtra("status", getBankDetails.getIsactive())

                                                );

                                                finish();

                                            }
                                        });


                                    } else {
                                        binding.bankdetailsBtnn.setText("Bank Details Rejected");
                                        binding.bankdetailsBtnn.setVisibility(View.VISIBLE);
                                        binding.bankdetailsBtnn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                startActivity(new Intent(KycActivity.this, KycBankActivity.class).putExtra("status", "4"));
                                            }
                                        });
                                    }
                                }    //pending done


                                else if (getKycStatus.getStaus().equals("3")) {   // Rejected
                                    GetKycStatusResponse.Data.BankDetails data1 = response.body().getData().getBankDetails();

                                    binding.kycImage.setBackgroundResource(R.drawable.unscusses);
                                    binding.kycText.setText(response.body().getData().getKycStatus().getMessage());
                                    binding.startVerificationBtn.setText(R.string.reverifi);


                                    if (data1 != null) {
                                        binding.bankdetailsBtnn.setVisibility(View.VISIBLE);
                                        if (getBankDetails.getIsactive().equals("1")) {
                                            binding.bankdetailsBtnn.setVisibility(View.VISIBLE);
                                            binding.bankdetailsBtnn.setVisibility(View.VISIBLE);
                                            binding.bankdetailsBtnn.setText("Bank Details Pending");
                                        }
                                        binding.bankdetailsBtnn.setVisibility(View.VISIBLE);
                                        String AccountType = response.body().getData().getBankDetails().getAccountType();
                                        String NameInBank = response.body().getData().getBankDetails().getNameInBank();
                                        String AccountNumber = response.body().getData().getBankDetails().getAccountNumber();
                                        String IfscCode = response.body().getData().getBankDetails().getIfscCode();
                                        String BankName = response.body().getData().getBankDetails().getBankName();
                                        String BranchAddress = response.body().getData().getBankDetails().getBranchAddress();
                                        String ChequeReceipt = response.body().getData().getBankDetails().getChequeReceipt();
                                        binding.bankdetailsBtnn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                startActivity(new Intent(KycActivity.this, KycBankRejectedActivity.class)
                                                        .putExtra("AccountType", AccountType)
                                                        .putExtra("NameInBank", NameInBank)
                                                        .putExtra("AccountNumber", AccountNumber)
                                                        .putExtra("IfscCode", IfscCode)
                                                        .putExtra("BankName", BankName)
                                                        .putExtra("BranchAddress", BranchAddress)
                                                        .putExtra("ChequeReceipt", ChequeReceipt)
                                                        .putExtra("status", getBankDetails.getIsactive())

                                                );

                                                finish();

                                            }
                                        });
                                    } else {
                                        binding.bankdetailsBtnn.setVisibility(View.VISIBLE);
                                        binding.bankdetailsBtnn.setText("Bank Details Rejected");

                                        binding.bankdetailsBtnn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                startActivity(new Intent(KycActivity.this, KycBankActivity.class).putExtra("status", "4"));
                                            }
                                        });
                                    }


                                    binding.startVerificationBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            if (data1 != null) {

                                                Toast.makeText(ac, data1.getIsactive(), Toast.LENGTH_SHORT).show();
                                                String AccountType = response.body().getData().getBankDetails().getAccountType();
                                                String NameInBank = response.body().getData().getBankDetails().getNameInBank();
                                                String AccountNumber = response.body().getData().getBankDetails().getAccountNumber();
                                                String IfscCode = response.body().getData().getBankDetails().getIfscCode();
                                                String BankName = response.body().getData().getBankDetails().getBankName();
                                                String BranchAddress = response.body().getData().getBankDetails().getBranchAddress();
                                                String ChequeReceipt = response.body().getData().getBankDetails().getChequeReceipt();

                                                startActivity(new Intent(ac, KycRejectedActiviy.class).putExtra(
                                                        "nationIdStatus", response.body().getData().getUserdata().getNationalIdStatus())
                                                        .putExtra("addressProffStatus", response.body().getData().getUserdata().getAddressIdStatus())
                                                        .putExtra("addressProffPath", response.body().getData().getUserdata().getAddressproofPath())
                                                        .putExtra("nationProffPath", response.body().getData().getUserdata().getNationalidPath())

                                                        .putExtra("AccountType", AccountType)
                                                        .putExtra("NameInBank", NameInBank)
                                                        .putExtra("AccountNumber", AccountNumber)
                                                        .putExtra("IfscCode", IfscCode)
                                                        .putExtra("BankName", BankName)
                                                        .putExtra("BranchAddress", BranchAddress)
                                                        .putExtra("ChequeReceipt", ChequeReceipt)
                                                        .putExtra("bankstatus", data1.getIsactive())
                                                );
                                                finish();
                                            } else {
                                                startActivity(new Intent(ac, KycRejectedActiviy.class).putExtra(
                                                        "nationIdStatus", response.body().getData().getUserdata().getNationalIdStatus())
                                                        .putExtra("addressProffStatus", response.body().getData().getUserdata().getAddressIdStatus())
                                                        .putExtra("addressProffPath", response.body().getData().getUserdata().getAddressproofPath())
                                                        .putExtra("nationProffPath", response.body().getData().getUserdata().getNationalidPath())
                                                        .putExtra("bankstatus", "4")
                                                );
                                                finish();
                                            }


                                        }
                                    });
                                }   // Rejected
                            }//  indian

                            else {
                                Log.e("countryCode", "onResponse: " + new Gson().toJson(response.body().getData().getUserdata()));

                                GetKycStatusResponse.Data.KycStatus getKycStatus = response.body().getData().getKycStatus();
                                GetKycStatusResponse.Data.BankDetails getBankDetails = response.body().getData().getBankDetails();
                                if (getKycStatus.getStaus().equals("0")) {   // have to do kyc
                                    binding.kycImage.setBackgroundResource(R.drawable.kycverify);
                                    binding.kycText.setText(response.body().getData().getKycStatus().getMessage());
                                    binding.startVerificationBtn.setText(R.string.startverify);
                                    binding.startVerificationBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(ac, KycForignUploadActivity.class));
                                        }
                                    });
                                }  // new


                                else if (getKycStatus.getStaus().equals("1")) {          // waiitng from admin //

                                    // have to do kyc  pending from admin
                                    binding.kycImage.setBackgroundResource(R.drawable.kycverify);
                                    binding.kycText.setText(response.body().getData().getKycStatus().getMessage());
                                    binding.startVerificationBtn.setText(R.string.home);
                                    binding.startVerificationBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            finish();

                                        }
                                    });

                                    binding.bitAddress.setVisibility(View.VISIBLE);
                                    binding.ethAddress.setVisibility(View.VISIBLE);
                                    binding.prefectMoneyAddress.setVisibility(View.VISIBLE);
                                    binding.PayeerAddress.setVisibility(View.VISIBLE);
                                    binding.bitAddress.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(KycActivity.this, BtcActivity.class).putExtra("bit", response.body().getData().getUserdata().getBitAddress()));
                                        }
                                    });
                                    binding.ethAddress.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(KycActivity.this, ETHActivity.class).putExtra("eth", response.body().getData().getUserdata().getEthAddress()));
                                        }
                                    });
                                    binding.prefectMoneyAddress.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(KycActivity.this, PerfectMoneyActivity.class).putExtra("perfect", response.body().getData().getUserdata().getPemAddress()));
                                        }
                                    });
                                    binding.PayeerAddress.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(KycActivity.this, PayeerAddressActivity.class).putExtra("payeer", response.body().getData().getUserdata().getPayeerAccount()));
                                        }
                                    });

                                }    //pending from admin


                                if (getKycStatus.getStaus().equals("2")) {    // kyc done  Approved


                                    binding.kycImage.setBackgroundResource(R.drawable.kycsuccess);
                                    binding.kycText.setText(response.body().getData().getKycStatus().getMessage());
                                    binding.startVerificationBtn.setText(R.string.checkdocu);
                                    binding.startVerificationBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(ac, KycForeignSuccessActiviy.class).putExtra(
                                                    "nationIdStatus", response.body().getData().getUserdata().getNationalIdStatus())
                                                    .putExtra("addressProffStatus", response.body().getData().getUserdata().getAddressIdStatus())
                                                    .putExtra("addressProffPath", response.body().getData().getUserdata().getAddressproofPath())
                                                    .putExtra("nationProffPath", response.body().getData().getUserdata().getNationalidPath())


                                            );

                                            finish();

                                        }
                                    });

                                    binding.bitAddress.setVisibility(View.VISIBLE);
                                    binding.ethAddress.setVisibility(View.VISIBLE);
                                    binding.prefectMoneyAddress.setVisibility(View.VISIBLE);
                                    binding.PayeerAddress.setVisibility(View.VISIBLE);

                                    binding.bitAddress.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(KycActivity.this, BtcActivity.class).putExtra("bit", response.body().getData().getUserdata().getBitAddress()));
                                        }
                                    });

                                    binding.ethAddress.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(KycActivity.this, ETHActivity.class).putExtra("eth", response.body().getData().getUserdata().getEthAddress()));
                                        }
                                    });

                                    binding.prefectMoneyAddress.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(KycActivity.this, PerfectMoneyActivity.class).putExtra("perfect", response.body().getData().getUserdata().getPemAddress()));
                                        }
                                    });

                                    binding.PayeerAddress.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(KycActivity.this, PayeerAddressActivity.class).putExtra("payeer", response.body().getData().getUserdata().getPayeerAccount()));
                                        }
                                    });

                                }     // approved   done


                                else if (getKycStatus.getStaus().equals("3")) {   // Rejected
                                    GetKycStatusResponse.Data.BankDetails data1 = response.body().getData().getBankDetails();

                                    binding.kycImage.setBackgroundResource(R.drawable.unscusses);
                                    binding.kycText.setText(response.body().getData().getKycStatus().getMessage());
                                    binding.startVerificationBtn.setText(R.string.reverifi);
                                    binding.startVerificationBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {


                                            startActivity(new Intent(ac, KycForeignRejectedActiviy.class).putExtra(
                                                    "nationIdStatus", response.body().getData().getUserdata().getNationalIdStatus())
                                                    .putExtra("addressProffStatus", response.body().getData().getUserdata().getAddressIdStatus())
                                                    .putExtra("addressProffPath", response.body().getData().getUserdata().getAddressproofPath())
                                                    .putExtra("nationProffPath", response.body().getData().getUserdata().getNationalidPath())
                                            );
                                            finish();

                                        }


                                    });

                                    binding.bitAddress.setVisibility(View.VISIBLE);
                                    binding.ethAddress.setVisibility(View.VISIBLE);
                                    binding.prefectMoneyAddress.setVisibility(View.VISIBLE);
                                    binding.PayeerAddress.setVisibility(View.VISIBLE);

                                    binding.bitAddress.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(KycActivity.this, BtcActivity.class).
                                                    putExtra("bit", response.body().getData().getUserdata().getBitAddress()));
                                        }
                                    });

                                    binding.ethAddress.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(KycActivity.this, ETHActivity.class).putExtra("eth", response.body().getData().getUserdata().getEthAddress()));
                                        }
                                    });

                                    binding.prefectMoneyAddress.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(KycActivity.this, PerfectMoneyActivity.class).putExtra("perfect", response.body().getData().getUserdata().getPemAddress()));
                                        }
                                    });

                                    binding.PayeerAddress.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(KycActivity.this, PayeerAddressActivity.class).putExtra("payeer", response.body().getData().getUserdata().getPayeerAccount()));
                                        }
                                    });

                                }   // Rejected
                            }   // not indian
                        }else {
                            Toast.makeText(KycActivity.this,response.body().getMessage()!=null?response.body().getMessage():"Error",Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetKycStatusResponse> call, Throwable t) {
                dialog1.dismiss();
            }

        });

    }

    private void clicks() {
        // toolbar
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.kyc);
    }

    @Override
    public void onBackPressed() {
        if (shouldAllowBack == true) {
            super.onBackPressed();
        } else {

        }
    }


}
