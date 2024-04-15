package com.demo.ecom.functionapp;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with Azure Storage Queue trigger.
 */
public class QueueTriggerJava {
    /**
     * This function will be invoked when a new message is received at the specified path. The message contents are provided as input to this function.
     */
    @FunctionName("QueueTriggerJava") // This name should be same as class name of this java file
    public void run(
        @QueueTrigger(name = "message", queueName = "ecomstorageaccountqueue001", connection = "AzureWebJobsStorage") String message,
        final ExecutionContext context
    ) {
        context.getLogger().info("Java Queue trigger function processed a message: " + message);
    }
}
