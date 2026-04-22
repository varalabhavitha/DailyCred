package com.unqiuehire.kashflow.dto.responsedto;

import com.unqiuehire.kashflow.constant.EmployeeType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanApplicationResponseDto {

    private Long applicationId;
    private Long borrowerId;
    private Long lenderId;
    private Long planId;
    private Double loanAmount;

    private Integer age;
    private Double monthlyIncome;
    private EmployeeType employeeType;
    private String pinCode;
    private Boolean isEducated;
    private String certificates;
    private String collateral;

    private String status;
    private String applicationDate;
    private String rejectionReason;
    private Boolean isLoanCreated;
}