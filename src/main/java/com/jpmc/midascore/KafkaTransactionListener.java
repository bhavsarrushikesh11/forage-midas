package com.jpmc.midascore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmc.midascore.component.Transaction;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KafkaTransactionListener {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRecordRepository transactionRecordRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-group")
    public void listen(String message) {
        try {
            // Convert JSON to Transaction object
            Transaction transaction = objectMapper.readValue(message, Transaction.class);

            // Find sender and recipient
            UserRecord sender = userRepository.findById(transaction.getSenderId());
            UserRecord recipient = userRepository.findById(transaction.getRecipientId());

            if (sender != null && recipient != null) {
                if (sender.getBalance() >= transaction.getAmount()) {

                    // Call incentive API
                    IncentiveResponse response = restTemplate.postForObject(
                            "http://localhost:8080/incentive",
                            transaction,
                            IncentiveResponse.class
                    );

                    double incentive = (response != null) ? response.getAmount() : 0;

                    // Update balances
                    sender.setBalance(sender.getBalance() - transaction.getAmount());
                    recipient.setBalance((float) (recipient.getBalance() + transaction.getAmount() + incentive));

                    // Save transaction record with incentive
                    TransactionRecord record = new TransactionRecord(transaction.getAmount(), sender, recipient, incentive);
                    transactionRecordRepository.save(record);

                    // Save updated users
                    userRepository.save(sender);
                    userRepository.save(recipient);

                    System.out.println("Transaction successful: " + transaction.getAmount() + ", incentive: " + incentive);
                } else {
                    System.out.println("Transaction failed: Insufficient balance.");
                }
            } else {
                System.out.println("Transaction failed: Invalid sender or recipient.");
            }
        } catch (Exception e) {
            System.err.println("Error processing transaction: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

