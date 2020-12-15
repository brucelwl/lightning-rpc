package com.bruce.lightning.rpc.local;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class LocalServicesMapping {

    public static ConcurrentHashMap<String, Set<Object>> providerInvokers = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Set<Object>> consumerInvokers = new ConcurrentHashMap<>();







}
