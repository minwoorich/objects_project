package com.objects.marketbridge.common.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
public class KakaoPaySubsApproveRequest {

    private String cid;
    private String sid;
    private String partnerOrderId;
    private String partnerUserId;
    private String itemName;
    private Long quantity;
    private Long totalAmount;
    private Long taxFreeAmount;


    @Builder
    public KakaoPaySubsApproveRequest(String cid, String sid, String partnerOrderId, String partnerUserId, String itemName, Long quantity, Long totalAmount, Long taxFreeAmount) {
        this.cid = cid;
        this.sid = sid;
        this.partnerOrderId = partnerOrderId;
        this.partnerUserId = partnerUserId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.taxFreeAmount = taxFreeAmount;
    }

    public MultiValueMap<String, String> toMultiValueMap() {

        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();

        requestMap.add("cid", cid);
        requestMap.add("sid", sid);
        requestMap.add("partner_order_id", partnerOrderId);
        requestMap.add("partner_user_id",  partnerUserId);
        requestMap.add("item_name",  itemName);
        requestMap.add("quantity",  quantity.toString());
        requestMap.add("total_amount",  totalAmount.toString());
        requestMap.add("tax_free_amount",  taxFreeAmount.toString());

        return requestMap;
    }
}
