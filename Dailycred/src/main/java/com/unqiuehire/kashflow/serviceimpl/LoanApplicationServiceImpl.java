package com.unqiuehire.kashflow.serviceImpl;

import com.unqiuehire.kashflow.constant.ApiStatus;
import com.unqiuehire.kashflow.constant.StatusEnum;
import com.unqiuehire.kashflow.constant.MessageConstants;
import com.unqiuehire.kashflow.dto.requestdto.LoanApplicationRequestDto;
import com.unqiuehire.kashflow.dto.responsedto.ApiResponse;
import com.unqiuehire.kashflow.dto.responsedto.LoanApplicationResponseDto;
import com.unqiuehire.kashflow.entity.LoanApplication;
import com.unqiuehire.kashflow.exception.ResourceNotFoundException;
import com.unqiuehire.kashflow.repository.LoanApplicationRepository;
import com.unqiuehire.kashflow.service.LoanApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public class LoanApplicationServiceImpl implements LoanApplicationService {
    @Autowired
    private LoanApplicationRepository repository;

    @Override
    public ApiResponse<LoanApplicationResponseDto> createApplication(LoanApplicationRequestDto dto) {

        LoanApplication app = new LoanApplication();

        app.setBorrowerId(dto.getBorrowerId());
        app.setLenderId(dto.getLenderId());
        app.setPlanId(dto.getPlanId());
        app.setLoanAmount(dto.getLoanAmount());
        app.setStatus(StatusEnum.PENDING);
        app.setApplicationDate(LocalDate.now());

        LoanApplication saved = repository.save(app);

        return new ApiResponse<>(
                ApiStatus.SUCCESS,
                MessageConstants.LOAN_APPLICATION_CREATED.getMessage(),
                mapToDto(saved)
        );
    }

    @Override
    public ApiResponse<LoanApplicationResponseDto> getById(Long id) {

        LoanApplication app = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        MessageConstants.LOAN_APPLICATION_NOT_FOUND.getMessage()
                ));

        return new ApiResponse<>(
                ApiStatus.SUCCESS,
                MessageConstants.LOAN_APPLICATION_FOUND.getMessage(),
                mapToDto(app)
        );
    }

    @Override
    public ApiResponse<List<LoanApplicationResponseDto>> getByBorrower(Long borrowerId) {

        List<LoanApplication> list = repository.findByBorrowerId(borrowerId);

        if (list.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No applications found for borrower: " + borrowerId
            );
        }

        List<LoanApplicationResponseDto> dtoList = list.stream()
                .map(this::mapToDto)
                .toList();

        return new ApiResponse<>(
                ApiStatus.SUCCESS,
                MessageConstants.LOAN_APPLICATIONS_FOUND.getMessage(),
                dtoList
        );
    }


    @Override
    public ApiResponse<List<LoanApplicationResponseDto>> getByLender(Long lenderId) {

        List<LoanApplication> list = repository.findByLenderId(lenderId);

        if (list.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No applications found for lender: " + lenderId
            );
        }

        List<LoanApplicationResponseDto> dtoList = list.stream()
                .map(this::mapToDto)
                .toList();

        return new ApiResponse<>(
                ApiStatus.SUCCESS,
                MessageConstants.LOAN_APPLICATIONS_FOUND.getMessage(),
                dtoList
        );
    }
    @Override
    public ApiResponse<String> cancelApplication(Long id) {

        LoanApplication app = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        MessageConstants.LOAN_APPLICATION_NOT_FOUND.getMessage()
                ));

        if (app.getStatus() == StatusEnum.CANCELLED) {
            throw new IllegalArgumentException("Application already cancelled");
        }

        app.setStatus(StatusEnum.CANCELLED);
        repository.save(app);

        return new ApiResponse<>(
                ApiStatus.SUCCESS,
                MessageConstants.LOAN_APPLICATION_CANCELLED.getMessage(),
                "Cancelled application id: " + id
        );
    }
    private LoanApplicationResponseDto mapToDto(LoanApplication app) {

        LoanApplicationResponseDto dto = new LoanApplicationResponseDto();

        dto.setApplicationId(app.getApplicationId());
        dto.setBorrowerId(app.getBorrowerId());
        dto.setLenderId(app.getLenderId());
        dto.setPlanId(app.getPlanId());
        dto.setLoanAmount(app.getLoanAmount());
        dto.setStatus(app.getStatus().name());
        dto.setApplicationDate(app.getApplicationDate().toString());

        return dto;
    }
}