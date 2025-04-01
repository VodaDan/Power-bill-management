package com.pm.billingservice.mapper;

import com.pm.billingservice.dto.BillRequestDTO;
import com.pm.billingservice.dto.BillResponseDTO;
import com.pm.billingservice.model.Bill;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.UUID;

public class BillMapper {

    public static BillResponseDTO toDTO(Bill bill) {
        BillResponseDTO billResponseDTO = new BillResponseDTO();
        billResponseDTO.setCustomerId(bill.getCustomerId().toString());
        billResponseDTO.setId(bill.getId().toString());
        billResponseDTO.setAmount(String.valueOf(bill.getAmount()));
        billResponseDTO.setDueDate(bill.getDueDate().toString());
        billResponseDTO.setIssueDate(bill.getIssueDate().toString());
        return billResponseDTO;
    }

    public static Bill toModel(BillRequestDTO billRequestDTO) {
        Bill bill = new Bill();
        bill.setCustomerId(UUID.fromString(billRequestDTO.getCustomerId()));
        bill.setAmount(Double.valueOf(billRequestDTO.getAmount()));
        bill.setDueDate(LocalDate.parse(billRequestDTO.getDueDate()));
        bill.setIssueDate(LocalDate.parse(billRequestDTO.getIssueDate()));
        return bill;
    }
}
