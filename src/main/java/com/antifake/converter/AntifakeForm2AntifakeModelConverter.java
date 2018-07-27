package com.antifake.converter;

import com.antifake.form.AntifakeForm;
import com.antifake.model.Antifake; ;

public class AntifakeForm2AntifakeModelConverter {
public static Antifake converte(AntifakeForm antifakeForm) {
		
		Antifake antifake = new Antifake();
		
		antifake.setCompanyCode(antifakeForm.getCompanyCode());
		antifake.setCompanyId(antifakeForm.getCompanyId());
		antifake.setCount(antifakeForm.getCount());
		antifake.setPrivateKey(antifakeForm.getPrivateKey());
		antifake.setProductCode(antifakeForm.getProductCode());
		antifake.setTemplate(antifakeForm.getTemplate());
		antifake.setCipherId(antifakeForm.getCipherId());
		antifake.setStatus(antifakeForm.getStatus());
		antifake.setBegain(antifakeForm.getBegain());
		antifake.setEnd(antifakeForm.getEnd());
		antifake.setBatch(antifakeForm.getBatch());
		
		
		return antifake;
	}
}
