package com.example.voucherservice.service;

import org.springframework.stereotype.Component;
import com.corereqbeans.BillPayRequestForAll;


@Component
public interface VoucherService {

	public void topup(BillPayRequestForAll billreq,String reqString);
}