package com.example.subwise;

public class ValueConverterPage {
    private int savingAmount;       // 총 절약 금액
    private String selectedItem;    // 선택된 상품 이름
    private int itemPrice;          // 상품 단가
    private int convertedQuantity;  // 환산된 수량

    private int selectedItemPrice;
    private int partyCount = 1;

    public ValueConverterPage() {
        this.selectedItemPrice = selectedItemPrice;
    }

    // 절약 금액 설정
    public void setSavingData(int saving) {
        this.savingAmount = saving;
    }

    public void setPartyCount(int count) {
        this.partyCount = count;
    }

    // 환산 수량 계산
    public int calculateQuantity() {
        if (itemPrice > 0) {
            int effectiveSaving = savingAmount;
            if (partyCount > 1) {
                effectiveSaving = savingAmount / partyCount; // ✅ 파티 인원수만큼 나눔
            }
            convertedQuantity = effectiveSaving / itemPrice;
        } else {
            convertedQuantity = 0;
        }
        return convertedQuantity;
    }

    public int getSelectedItemPrice() {
        return selectedItemPrice;
    }

    // Getter
    public String getSelectedItem() { return selectedItem; }
    public int getConvertedQuantity() { return convertedQuantity; }
}
