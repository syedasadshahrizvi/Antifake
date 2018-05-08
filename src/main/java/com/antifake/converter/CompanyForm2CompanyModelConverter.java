package com.antifake.converter;

import com.antifake.form.CompanyForm;
import com.antifake.model.Company;

public class CompanyForm2CompanyModelConverter {
	
	public static Company converte(CompanyForm companyForm) {
		
		Company company = new Company();
		company.setUserId(companyForm.getUserId());
		company.setCompanyName(companyForm.getCompanyname());
		company.setRegisterId(companyForm.getRegisterId());
		company.setLevel(companyForm.getLevel());
		company.setBusinessLicense(companyForm.getBusinessLicense());
		
		return company;
	}
	
}
