package com.bbs.fragments.calculators;

import java.text.DecimalFormat;

import com.bbs.api.BBSApiSettings;
import com.bbs.fragments.BBSFragmentBase;
import com.bbs.taxapp.MainActivity;
import com.bbs.taxapp.R;
import com.bbs.utils.UiUtil;
import com.bbs.widgets.BBSCalculatorResult;
import com.bbs.widgets.BBSCalculatorSubResult;
import com.bbs.widgets.BBSHeader.BBSHeaderButtonType;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class BBSFragmentBudget extends BBSFragmentBase{
	public static String PAGEID = "HomeBudget"; 
	
	private BBSCalculatorResult wid_result_expenses;
	private BBSCalculatorResult wid_result_savings;
	private BBSCalculatorSubResult wid_result_mortgage;
	private BBSCalculatorSubResult wid_result_utilities;
	private BBSCalculatorSubResult wid_result_insurance;
	private BBSCalculatorSubResult wid_result_general;
	
	private EditText txt_incomeMonth;
	private EditText txt_incomeSpouse;
	private EditText txt_mortgageHome;
	private EditText txt_mortgageAuto;
	private EditText txt_mortgageAuto2;
	private EditText txt_mortgageCredit;
	private EditText txt_mortgageDebt;
	private EditText txt_utilElectric;
	private EditText txt_utilGas;
	private EditText txt_utilOil;
	private EditText txt_utilSewer;
	private EditText txt_utilGarbage;
	private EditText txt_utilTel;
	private EditText txt_utilCellular;
	private EditText txt_utilTV;
	private EditText txt_insuranceAuto;
	private EditText txt_insuranceLife;
	private EditText txt_insuranceHome;
	private EditText txt_insuranceHealth;
	private EditText txt_generalFood;
	private EditText txt_generalGas;
	private EditText txt_generalDonation;
	private EditText txt_generalHome;
	private EditText txt_generalMedical;
	private EditText txt_generalChild;
	private EditText txt_generalOther;
	
	private Button btn_clear;
	private Button btn_calc;
	
	//constructor
	public BBSFragmentBudget() {
		super();
		
		mPageID = PAGEID;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainActivity = (MainActivity) getActivity();
		
		RelativeLayout view = (RelativeLayout)inflater.inflate(R.layout.fragment_calc_budget, container, false);
		
		//create header first
		createHeader((ViewGroup) view.findViewById(R.id.header), 
				getString(R.string.home_budget), 
				BBSHeaderButtonType.BBSButtonBack, 
				null);
		//bring header to front
		view.bringChildToFront(view.findViewById(R.id.header));
		
		wid_result_expenses = (BBSCalculatorResult) view.findViewById(R.id.wid_result_expenses);
		wid_result_savings = (BBSCalculatorResult) view.findViewById(R.id.wid_result_savings);
		wid_result_mortgage = (BBSCalculatorSubResult) view.findViewById(R.id.wid_result_mortgage);
		wid_result_utilities = (BBSCalculatorSubResult) view.findViewById(R.id.wid_result_utilities);
		wid_result_insurance = (BBSCalculatorSubResult) view.findViewById(R.id.wid_result_insurance);
		wid_result_general = (BBSCalculatorSubResult) view.findViewById(R.id.wid_result_general);
		
		wid_result_expenses.setViewValues(R.drawable.ico_money, "0.00", R.string.monthly_expenses);
		wid_result_savings.setViewValues(R.drawable.ico_money, "0.00", R.string.monthly_savings);
		wid_result_mortgage.setViewValues(R.drawable.ico_percent, "0.00", R.string.mortgage);
		wid_result_utilities.setViewValues(R.drawable.ico_percent, "0.00", R.string.utilities);
		wid_result_insurance.setViewValues(R.drawable.ico_percent, "0.00", R.string.insurance);
		wid_result_general.setViewValues(R.drawable.ico_percent, "0.00", R.string.general_expenses);
		
		txt_incomeMonth = (EditText) view.findViewById(R.id.income_text);
		txt_incomeSpouse = (EditText) view.findViewById(R.id.spouse_text);
		txt_mortgageHome = (EditText) view.findViewById(R.id.house_payment_text);
		txt_mortgageAuto = (EditText) view.findViewById(R.id.auto_text);
		txt_mortgageAuto2 = (EditText) view.findViewById(R.id.auto2_text);
		txt_mortgageCredit = (EditText) view.findViewById(R.id.credit_text);
		txt_mortgageDebt = (EditText) view.findViewById(R.id.debt_text);
		txt_utilElectric = (EditText) view.findViewById(R.id.electric_text);
		txt_utilGas = (EditText) view.findViewById(R.id.gas_text);
		txt_utilOil = (EditText) view.findViewById(R.id.oil_text);
		txt_utilSewer = (EditText) view.findViewById(R.id.sewer_text);
		txt_utilGarbage = (EditText) view.findViewById(R.id.garbage_text);
		txt_utilTel = (EditText) view.findViewById(R.id.telephone_text);
		txt_utilCellular = (EditText) view.findViewById(R.id.cellular_text);
		txt_utilTV = (EditText) view.findViewById(R.id.tv_text);
		txt_insuranceAuto = (EditText) view.findViewById(R.id.ins_auto_text);
		txt_insuranceLife = (EditText) view.findViewById(R.id.life_text);
		txt_insuranceHome = (EditText) view.findViewById(R.id.home_text);
		txt_insuranceHealth = (EditText) view.findViewById(R.id.health_text);
		txt_generalFood = (EditText) view.findViewById(R.id.food_text);
		txt_generalGas = (EditText) view.findViewById(R.id.auto_gas_text);
		txt_generalDonation = (EditText) view.findViewById(R.id.donations_text);
		txt_generalHome = (EditText) view.findViewById(R.id.maintenance_text);
		txt_generalMedical = (EditText) view.findViewById(R.id.medical_text);
		txt_generalChild = (EditText) view.findViewById(R.id.child_care_text);
		txt_generalOther = (EditText) view.findViewById(R.id.other_expenses_text);
		
		btn_clear = (Button) view.findViewById(R.id.btn_clear);
		UiUtil.applyButtonEffect(btn_clear, new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				initValues();
			}
		});
		
		btn_calc = (Button) view.findViewById(R.id.btn_calc);
		UiUtil.applyButtonEffect(btn_calc, new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				float income_month = getFloat(txt_incomeMonth);
				float income_spouse = getFloat(txt_incomeSpouse);
				
				float mort_home = getFloat(txt_mortgageHome);
				float mort_auto = getFloat(txt_mortgageAuto);
				float mort_auto2 = getFloat(txt_mortgageAuto2);
				float mort_credit = getFloat(txt_mortgageCredit);
				float mort_debt = getFloat(txt_mortgageDebt);
				
				float util_electric = getFloat(txt_utilElectric);
				float util_gas = getFloat(txt_utilGas);
				float util_oil = getFloat(txt_utilOil);
				float util_sewer = getFloat(txt_utilSewer);
				float util_garbage = getFloat(txt_utilGarbage);
				float util_tel = getFloat(txt_utilTel);
				float util_cellular = getFloat(txt_utilCellular);
				float util_tv = getFloat(txt_utilTV);
				
				float insu_auto = getFloat(txt_insuranceAuto);
				float insu_life = getFloat(txt_insuranceLife);
				float insu_home = getFloat(txt_insuranceHome);
				float insu_health = getFloat(txt_insuranceHealth);
				
				float gen_food = getFloat(txt_generalFood);
				float gen_gas = getFloat(txt_generalGas);
				float gen_don = getFloat(txt_generalDonation);
				float gen_home = getFloat(txt_generalHome);
				float gen_medical = getFloat(txt_generalMedical);
				float gen_child = getFloat(txt_generalChild);
				float gen_other = getFloat(txt_generalOther);
				
				float sum_income = income_month + income_spouse;
				float sum_mort = mort_home + mort_auto + mort_auto2 + mort_credit + mort_debt;
				float sum_util = util_electric + util_gas + util_oil + util_sewer + util_garbage + util_tel + util_cellular + util_tv;
				float sum_insu = insu_auto + insu_life + insu_home + insu_health;
				float sum_gen = gen_food + gen_gas + gen_don + gen_home + gen_medical + gen_child + gen_other;
				float sum_pay = sum_mort + sum_util + sum_insu + sum_gen;
				
				DecimalFormat formatter = new DecimalFormat("#,###.00");
				
				wid_result_expenses.setResultValue(Float.toString(sum_pay));
				wid_result_savings.setResultValue(formatter.format(sum_income - sum_pay));
				wid_result_mortgage.setResultValue(BBSApiSettings.getInstance(mMainActivity).calcBudget(sum_income, sum_mort));
				wid_result_utilities.setResultValue(BBSApiSettings.getInstance(mMainActivity).calcBudget(sum_income, sum_util));
				wid_result_insurance.setResultValue(BBSApiSettings.getInstance(mMainActivity).calcBudget(sum_income, sum_insu));
				wid_result_general.setResultValue(BBSApiSettings.getInstance(mMainActivity).calcBudget(sum_income, sum_gen));
			}
		});
		
		initValues();
		
		mRootLayout = view;
		return view;
	}
	
	private float getFloat(EditText txt_input) {
		float result = 0;
		if (!txt_input.getText().toString().equals("")) 
			result = Float.parseFloat(txt_input.getText().toString()); 
		return result;
	}
	
	private void initValues() {
		wid_result_expenses.setResultValue("0.00");
		wid_result_savings.setResultValue("0.00");
		wid_result_mortgage.setResultValue("0.00");
		wid_result_utilities.setResultValue("0.00");
		wid_result_insurance.setResultValue("0.00");
		wid_result_general.setResultValue("0.00");
		
		txt_incomeMonth.setText("");
		txt_incomeSpouse.setText("");
		
		txt_mortgageHome.setText("");
		txt_mortgageAuto.setText("");
		txt_mortgageAuto2.setText("");
		txt_mortgageCredit.setText("");
		txt_mortgageDebt.setText("");
		
		txt_utilElectric.setText("");
		txt_utilGas.setText("");
		txt_utilOil.setText("");
		txt_utilSewer.setText("");
		txt_utilGarbage.setText("");
		txt_utilTel.setText("");
		txt_utilCellular.setText("");
		txt_utilTV.setText("");
		
		txt_insuranceAuto.setText("");
		txt_insuranceLife.setText("");
		txt_insuranceHome.setText("");
		txt_insuranceHealth.setText("");
		
		txt_generalFood.setText("");
		txt_generalGas.setText("");
		txt_generalDonation.setText("");
		txt_generalHome.setText("");
		txt_generalMedical.setText("");
		txt_generalChild.setText("");
		txt_generalOther.setText("");
	}
	
	@Override
	public void onNavBarLeftButtonClicked() {
		mMainActivity.popLastPage();
	}
}
