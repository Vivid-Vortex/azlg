package com.demo.ecom.functionapp;

//import com.microsoft.azure.functions.annotation.*;
//import com.microsoft.azure.functions.*;
//
//import java.util.UUID;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.QueueTrigger;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.table.*;

import java.util.UUID;


/**
 * Azure Functions with Azure Storage Queue trigger.
 */
public class QueueTriggerJava {
    /**
     * This function will be invoked when a new message is received at the specified path. The message contents are provided as input to this function.
     */

    final String SECRECT_NAME = "storageConnectionString";
    final String KEY_VAULT_URL = "https://ecomkeyvalut002.vault.azure.net/";

    // Define your Azure Storage connection string
    private static final String storageConnectionString = "get_the_connection_string_from_azure_portal";
//    @FunctionName("QueueTriggerJava") // This name should be same as class name of this java file
//    public void run(
//        @QueueTrigger(name = "message", queueName = "ecomstorageaccountqueue001", connection = "AzureWebJobsStorage") String message,
//        final ExecutionContext context
//    ) {
//        context.getLogger().info("Java Queue trigger function processed a message: " + message);
//    }

    @FunctionName("QueueToTableFunction")
    public void run(
            @QueueTrigger(name = "message", queueName = "ecomstorageaccountqueue001", connection = "AzureWebJobsStorage") String message,
            final ExecutionContext context
    ) {
        context.getLogger().info("Java Queue trigger function processed a message: " + message);

        try {
            // Parse the message or process it as needed
            // For simplicity, let's assume the message is in JSON format
            // You may need to deserialize it based on your message structure

//            context.getLogger().info("CALLING 'GETSECRECT' METHOD WITH THE VALUES: " + SECRECT_NAME + " AND " + KEY_VAULT_URL);
//            final String connectionStringSecrect = getSecret(SECRECT_NAME, KEY_VAULT_URL, context);
//            context.getLogger().info("'GETSECRECT' RETURNED THE FUNCTION WITH THE VALUE AS: " + connectionStringSecrect);

            // Create a CloudStorageAccount object using the connection string
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

            // Create a CloudTableClient object to interact with the Table service
            CloudTableClient tableClient = storageAccount.createCloudTableClient();

            // Create a CloudTable object that represents the Azure table
            CloudTable cloudTable = tableClient.getTableReference("ecomprocessedorders");

            // Create a new dynamic table entity and set its properties
            // Here, you would map the message data to the properties of your table entity
            // For demonstration, we generate a random partition key and row key
            String partitionKey = UUID.randomUUID().toString();
            String rowKey = UUID.randomUUID().toString();
            DynamicTableEntity entity = new DynamicTableEntity(partitionKey, rowKey);
            entity.getProperties().put("MessageData", new EntityProperty(message));

            // Create an operation to insert the entity into the table
            TableOperation insertOperation = TableOperation.insertOrReplace(entity);

            // Execute the insert operation
            cloudTable.execute(insertOperation);

            context.getLogger().info("Message data inserted into Azure Table Storage successfully.");

        } catch (Exception e) {
            // Log any exceptions that occur during processing
            context.getLogger().severe("An error occurred: " + e.getMessage());
        }
    }

//    private static String getSecret(String secretName, String keyVaultUrl, final ExecutionContext context) {
//        KeyVaultSecret secretValue = null;
//        try {
//            context.getLogger().info("------------------getSecret method started-------------");
//            // Authenticate with Azure Key Vault using managed identity
//            SecretClient secretClient = new SecretClientBuilder()
//                    .vaultUrl(keyVaultUrl)
//                    .credential(new DefaultAzureCredentialBuilder().build())
//                    .buildClient();
//            context.getLogger().info("------------------We have a Secrect client Object, having the value as : " + secretClient.listPropertiesOfSecrets());
//
//            // Retrieve the secret from Azure Key Vault
//            secretValue = secretClient.getSecret(secretName);
//
//            context.getLogger().info("------------------We have got the secrect using the  Secrect client Object. Secrect is : " + secretValue);
//
//            context.getLogger().info("------------------getSecret method ended-------------");
//            // Return the secret value
//
//        } catch (HttpResponseException ex) {
//            context.getLogger().severe("Error retrieving secret" + ex.getMessage() + " - HTTP Status Code: " + ex.getResponse().getStatusCode());
//        } catch (Exception ex) {
//            context.getLogger().severe("Error retrieving secret (Timeout: " + ex.getMessage());
//            throw new RuntimeException(ex);
//        }
//        return secretValue.getValue();
//    }
}
