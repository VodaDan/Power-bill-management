package com.pm.customerservice.proto;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BillingGrpcServiceClientUnitTests {

    private BillingGrpcServiceClient client;
    private BillingServiceGrpc.BillingServiceBlockingStub blockingStub;


    @BeforeEach
    public void setup() {
        blockingStub = mock(BillingServiceGrpc.BillingServiceBlockingStub.class);
        client = new BillingGrpcServiceClient(blockingStub);
    }

    @Test
    public void CreateBillingReturnsBillingResponse () {

        String customerId = "123";
        String amount = "100";

        BillingResponse expectedResponse = BillingResponse.newBuilder()
                .setIssueDate("2025-02-02")
                .setDueDate("2025-02-02")
                .build();

        when(blockingStub.createBilling(any(BillingRequest.class))).thenReturn(expectedResponse);

        BillingResponse actualResponse = client.createBilling(customerId, amount);

        assertEquals(actualResponse,expectedResponse);
        ArgumentCaptor<BillingRequest> captor = ArgumentCaptor.forClass(BillingRequest.class);
        verify(blockingStub, times(1)).createBilling(captor.capture());

        BillingRequest capturedRequest = captor.getValue();
        assertEquals(customerId, capturedRequest.getCustomerId());
        assertEquals(amount, capturedRequest.getAmount());
    }

}
