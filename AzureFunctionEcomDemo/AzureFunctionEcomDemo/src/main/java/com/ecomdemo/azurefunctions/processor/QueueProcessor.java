package com.ecomdemo.azurefunctions.processor;



import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.queue.CloudQueue;
import com.microsoft.azure.storage.queue.CloudQueueMessage;

import org.springframework.cloud.function.adapter.azure.AzureSpringBootRequestHandler;

import java.util.Optional;

public class OrderProcessorFunction extends AzureSpringBootRequestHandler<String, Void> {

    @FunctionName("orderProcessor")
    public void processOrder(
            @QueueTrigger(name = "message", queueName = "orderqueue", connection = "AzureWebJobsStorage") String orderMessage,
            ExecutionContext context) {

        CloudStorageAccount storageAccount;
        CloudQueue queue;

        try {
            storageAccount = CloudStorageAccount.parse(System.getenv("AzureWebJobsStorage"));
            queue = storageAccount.createCloudQueueClient().getQueueReference("orderqueue");

            // Process the order message
            handleOrder(orderMessage);

            // Delete the message from the queue after processing
            CloudQueueMessage message = queue.retrieveMessage();
            if (message != null) {
                queue.deleteMessage(message);
            }
        } catch (Exception e) {
            context.getLogger().severe("Error processing order: " + e.getMessage());
        }
    }

    private void handleOrder(String orderMessage) {
        // Implement your order processing logic here
        System.out.println("Processing order: " + orderMessage);
    }
}

