package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {

    private long idLoan;
    private int amount;
    private int payments;
    private String numberAccount;

    public LoanApplicationDTO(long idLoan, int amount, int payments, String numberAccount) {
        this.idLoan = idLoan;
        this.amount = amount;
        this.payments = payments;
        this.numberAccount = numberAccount;
    }


    public long getIdLoan() {
        return idLoan;
    }

    public void setIdLoan(long idLoan) {
        this.idLoan = idLoan;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPayments() {
        return payments;
    }

    public void setPayments(int payments) {
        this.payments = payments;
    }

    public String getNumberAccount() {
        return numberAccount;
    }

    public void setNumberAccount(String numberAccount) {
        this.numberAccount = numberAccount;
    }
}
