package com.prx.mercury.kafka.consumer;

import com.prx.mercury.jpa.nosql.entity.EmailMessageDocument;
import com.prx.mercury.kafka.to.EmailMessageTO;
import org.apache.commons.lang.NotImplementedException;

import java.util.List;
import java.util.Optional;

/// This interface defines the operations for consuming email message data from Kafka and storing it in a cache.
/// The operations include finding all email messages, finding an email message by id, saving an email message,
/// saving all email messages, deleting an email message by id, deleting all email messages, and updating an email message.
/// The default implementations of these methods throw a NotImplementedException.
public interface EmailMessageConsumerService {

    /// Finds all email messages in the cache.
    ///
    /// @return A list of all email messages in the cache.
    /// @throws NotImplementedException if the method is not implemented.
    default List<EmailMessageDocument> findAll() {
        throw new NotImplementedException();
    }

    /// Finds an email message in the cache by its id.
    ///
    /// @param id The id of the email message to find.
    /// @return An Optional containing the found email message, or an empty Optional if no email message was found with the given id.
    /// @throws NotImplementedException if the method is not implemented.
    default Optional<EmailMessageTO> findById(String id) {
        throw new NotImplementedException();
    }

    /// Saves an email message in the cache.
    ///
    /// @param emailMessageTO The email message to save.
    /// @return The saved email message.
    /// @throws NotImplementedException if the method is not implemented.
    default EmailMessageTO save(EmailMessageTO emailMessageTO) {
        throw new NotImplementedException();
    }

    /// Saves a list of email messages in the cache.
    ///
    /// @param emailMessageTOS The list of email messages to save.
    /// @return The list of saved email messages.
    /// @throws NotImplementedException if the method is not implemented.
    default List<EmailMessageTO> saveAll(List<EmailMessageTO> emailMessageTOS) {
        throw new NotImplementedException();
    }

    /// Deletes an email message from the cache by its id.
    ///
    /// @param id The id of the email message to delete.
    /// @throws NotImplementedException if the method is not implemented.
    default void deleteById(String id) {
        throw new NotImplementedException();
    }

    /// Deletes all email messages from the cache.
    ///
    /// @throws NotImplementedException if the method is not implemented.
    default void deleteAll() {
        throw new NotImplementedException();
    }

    /// Updates an email message in the cache.
    ///
    /// @param emailMessageTO The email message to update.
    /// @return The updated email message.
    /// @throws NotImplementedException if the method is not implemented.
    default EmailMessageTO update(EmailMessageTO emailMessageTO) {
        throw new NotImplementedException();
    }
}
