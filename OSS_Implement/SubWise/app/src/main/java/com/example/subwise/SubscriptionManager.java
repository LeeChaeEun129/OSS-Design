package com.example.subwise;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionManager {
    private static List<Subscription> subscriptions = new ArrayList<>();

    // 구독 추가
    public static void addSubscription(Subscription sub) {
        subscriptions.add(sub);
    }

    // 전체 구독 리스트 반환
    public static List<Subscription> getAllSubscriptions() {
        return subscriptions;
    }
}
