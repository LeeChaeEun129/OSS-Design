package com.example.subwise;

public class Subscription {
    private int id;
    private String serviceName;   // ✅ DB 컬럼과 동일
    private int price;            // ✅ DB 컬럼과 동일
    private String billingDate;
    private String category;
    private String partyName;
    private int partyCount;
    private boolean essential;
    private boolean shared;

    // ✅ 생성자 (DB 컬럼과 동일, 9개 인자)
    public Subscription(int id, String serviceName, int price, String billingDate,
                        String category, String partyName, int partyCount,
                        boolean essential, boolean shared) {
        this.id = id;
        this.serviceName = serviceName;
        this.price = price;
        this.billingDate = billingDate;
        this.category = category;
        this.partyName = partyName;
        this.partyCount = partyCount;
        this.essential = essential;
        this.shared = shared;
    }

    // ✅ Getter
    public int getId() { return id; }
    public String getServiceName() { return serviceName; }
    public int getPrice() { return price; }
    public String getBillingDate() { return billingDate; }
    public String getCategory() { return category; }
    public String getPartyName() { return partyName; }
    public int getPartyCount() { return partyCount; }
    public boolean isEssential() { return essential; }
    public boolean isShared() { return shared; }
}
